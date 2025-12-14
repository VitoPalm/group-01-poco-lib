package poco.company.group01pocolib.db;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Pattern;


/**
 * @class   DB
 * @brief   Allows creation and simple handling of a database (UTF-8 encoded file).
 * @details This class is based on the assumption that the entries on the DB are separated on different lines, but
 *          makes no further assumptions on how fields for a given entry are defined or separated. It allows reading,
 *          writing, and removing specific lines from a database file, it also includes a few methods to edit, remove
 *          and return lines based on regex patterns.
 *          <br><br>
 *          The class preloads the DB lines into an internal cache for faster access. The original file is always
 *          modified when writing or removing lines, and the cache is rebuilt accordingly. The class is built with the
 *          idea of never having an edit on the DB file without updating the cache, so the two are always in sync.
 *          <br><br>
 *          The reason for an additional data support for the application (which already obtains data permanence between
 *          sessions through serialization of objects) is reliability and, most importantly, the ability to support
 *          future changes to the base data models. The serialized objects are not human-readable, and any change to the
 *          data models would make it not possible to retrieve previously saved data.
 *          <br><br>
 *          The DB class, being reliant on a simple text file, allows easy "legacy support" by any future versions of
 *          the application, as well as easy manual editing of the data if needed. (and, if needed, importing and
 *          exporting data from and to other machines through these simple text files).
 */
public class DB implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String DBPath;
    private String lineSeparator;
    private final ArrayList<String> cache;
    private String DBFileHash;

    private static final int INITIAL_CACHE_CAPACITY = 45000;

    /**
     * @brief   Constructs a new DB object with the specified file path.
     * @details This constructor initializes the DB object with the provided file path. It also preloads the lines
     *          from the DB file into an internal cache for faster access and calculates the initial hash of the DB file.
     *
     * @param   DBPath The path to the database file.
     */
    public DB(Path DBPath) {
        this.DBPath = DBPath.toString();
        this.lineSeparator = detectLineSeparator();

        // Preload lines into cache
        this.cache = new ArrayList<>(INITIAL_CACHE_CAPACITY);
        this.buildCache();

        // Calculate initial file hash
        updateDBFileHash();
    }

    /**
     * @brief   Constructs a new DB object with the specified file path.
     * @details This constructor initializes the DB object with the provided file path. It also preloads the lines
     *          from the DB file into an internal cache for faster access and calculates the initial hash of the DB file.
     *
     * @param   DBPath The path to the database file.
     */
    public DB(String DBPath) {
        this.DBPath = DBPath;
        this.lineSeparator = detectLineSeparator();

        // Preload lines into cache
        this.cache = new ArrayList<>(INITIAL_CACHE_CAPACITY);
        this.buildCache();

        // Calculate initial file hash
        updateDBFileHash();
    }

    /**
     * @brief   Gets the path to the DB file as a `String`.
     * @return  The path to the DB file.
     */
    public String getDBPath() {
        return DBPath;
    }

    /**
     * @brief   Gets the path to the DB file as a `Path` object.
     * @return  The path to the DB file.
     */
    public Path getDBPathAsPath() {
        return FileSystems.getDefault().getPath(this.DBPath);
    }

    /**
     * @brief   Sets the path to the DB file from a `String`.
     * @param   DBPath The new path to the DB file.
     */
    public void setDBPath(String DBPath) {
        this.DBPath = DBPath;
    }

    /**
     * @brief   Sets the path to the DB file from a `Path` object.
     * @param   DBPath The new path to the DB file.
     */
    public void setDBPath(Path DBPath) {
        this.DBPath = DBPath.toString();
    }

    /**
     * @brief   Gets the stored hash of the DB file.
     * @return  The stored hash of the DB file.
     */
    public String getDBFileHash() {
        return DBFileHash;
    }

    /**
     * @brief   Updates the stored hash of the DB file.
     * @details This method recalculates the hash of the DB file based on its current content. It uses the internal
     *          cache of lines to compute the hash efficiently. If the cache is empty and cannot be built, it sets the
     *          hash to `null`, indicating an issue with the file.
     */
    public void updateDBFileHash() {
        if (this.cache.isEmpty() && !this.buildCache()) {
            // If cache build fails, there's something wrong with the file, so we set the hash to null
            DBFileHash = null;
            return;
        }

        this.DBFileHash = Hash.getFileHashFromLines(this.cache, this.lineSeparator);
    }

    /**
     * @brief   Updates the stored hash of the DB file and returns it.
     * @details This method recalculates the hash of the DB file based on its current content and returns the updated
     *          hash. It uses the internal cache of lines to compute the hash efficiently. If the cache is empty and
     *          cannot be built, it sets the hash to `null`, indicating an issue with the file.
     *
     * @return  The updated hash of the DB file.
     */
    public String updateAndGetDBFileHash() {
        updateDBFileHash();

        return this.DBFileHash;
    }

    /**
     * @brief   Forces recalculation of the DB file hash directly from the file.
     * @details This method recalculates the hash of the DB file by reading its content directly from the file,
     *          bypassing the internal cache. This can be useful in scenarios where the file may have been modified
     *          externally, and we want to ensure that the stored hash reflects the actual content of the file.
     *          <br><br>
     *          If the newly calculated hash differs from the stored hash, it rebuilds the internal cache to keep it in
     *          sync with the file.
     *
     * @return  The newly calculated hash of the DB file.
     */
    public String forceHashOnFile() {
        String forcedHash = Hash.getFileHash(this.getDBPathAsPath());

        if (!forcedHash.equals(DBFileHash)) {
            // If the hash has changed, we need to rebuild the cache to keep it in sync with the file
            this.buildCache();
        }

        this.DBFileHash = forcedHash;

        return forcedHash;
    }

    /**
     * @brief   Detects the line separator used in a file.
     * @details This method reads the file at the specified path and detects the line separator used. This can be useful
     *          to ensure cross-platform compatibility when transferring DB files between different operating systems.
     *          <br><br>
     *          Normally this wouldn't be necessary, but to improve efficiency when calculating file hashes, in order to
     *          use the `Hash.getFileHashFromLines` method, while getting the same result as `Hash.getFileHash`, we need
     *          to know the exact line separator used in the file.
     *
     * @return  The detected line separator as a `String` ("\n", "\r\n", or "\r"). If no line breaks are found, returns
     *          the system's default line separator.
     */
    public String detectLineSeparator() {
        try (InputStream stream = Files.newInputStream(this.getDBPathAsPath())) {
            int b;
            while ((b = stream.read()) != -1) {
                // Unix/Linux/MacOS line ending
                if (b == '\n') return "\n";
                if (b == '\r') {
                    int next = stream.read();
                    // Windows line ending
                    if (next == '\n') return "\r\n";
                    // Legacy Mac line ending
                    return "\r";
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return System.lineSeparator(); // Default if no line breaks found
    }


    /**
     * @brief   Clears the entire DB file and the internal cache.
     * @details This method removes all entries from the database file and clears the internal cache of lines.
     *          Use with caution as this operation is irreversible.
     */
    public void clear() {
        this.cache.clear();
        this.updateDBFromCache(); // Writes an empty string to the file, clearing it
    }

    /**
     * @brief   Builds the cache of lines from the DB file.
     * @details This method reads all lines from the database file and stores them in an internal cache for faster
     *          access. It clears any existing cached lines before rebuilding the cache.
     *
     * @return  `true` if the cache was successfully built, `false` otherwise.
     */
    public boolean buildCache() {
        this.cache.clear();

        try (BufferedReader reader = Files.newBufferedReader(this.getDBPathAsPath(), StandardCharsets.UTF_8)) {
            String line;

            while ((line = reader.readLine()) != null) {
                cache.add(line);
            }

            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * @brief   Updates the DB file from cache.
     * @details This method completely overwrites the database file using the lines saved in cache. Use with caution.
     *
     * @return  `true` if the DB file was successfully updated, `false` otherwise.
     */
    private boolean updateDBFromCache() {
        StringBuilder updatedDB = new StringBuilder();

        for(String line : this.cache) {
            updatedDB.append(line).append(System.lineSeparator());
        }

        try {
            Files.writeString(this.getDBPathAsPath(), updatedDB.toString(), StandardCharsets.UTF_8);
            return true;

        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * @brief   Reads the N-th line from the cached DB.
     * @details This method retrieves the N-th line from the cached lines of DB. In case the cache is empty, it
     *          re-builds the cache and then gets the line.
     *
     * @param   N Index of line to read (0-based).
     * @return  Content of N-th line, or `null` if line does not exist.
     */
    public String readNthLine(int N) {
        if (this.cache.isEmpty() && !this.buildCache()) return null;

        if (N < 0 || N >= this.cache.size()) return null;

        return this.cache.get(N);
    }

    /**
     * @brief   Helper method to write a line at the N-th position with configurable behavior.
     * @details This method writes a new line at the N-th position in the database file. Depending on the `shift`
     *          parameter, it either shifts existing lines down or replaces the existing line at that position. It also
     *          updates the internal cache. (The updated cache is used to rebuild the DB file, overwriting its previous
     *          content completely).
     *
     * @param   N       Index of line to write (0-based).
     * @param   newLine The new line to be added at index N.
     * @param   shift   If `true`, shifts existing lines down; if `false`, replaces the existing line.
     * @return  `true` if the line was successfully written, `false` otherwise.
     */
    private boolean writeNthLine(int N, String newLine, boolean shift) {
        boolean wroteLine = true;

        if (this.cache.isEmpty() && !this.buildCache()) return false;

        if (N < 0 || N > this.cache.size()) return false;       // Appending a line is supported

        String overWrittenLine = null;
        if (shift || N == this.cache.size()) {
            this.cache.add(N, newLine);
        } else {
            overWrittenLine = this.cache.set(N, newLine);
        }

        if (!this.updateDBFromCache()) {
            wroteLine = false;

            // Rollback cache change to avoid conflicts
            if (shift) {
                this.cache.remove(N);
            } else {
                this.cache.set(N, overWrittenLine);
            }
        }

        return wroteLine;
    }

    /**
     * @brief   Writes a new line at the N-th position in DB, shifting existing lines down.
     *
     * @param   N       Index of line to write (0-based).
     * @param   newLine The new line to be added at index N.
     * @return  `true` if the line was successfully written, `false` otherwise.
     */
    public boolean writeNthLineWShift(int N, String newLine) {
        return writeNthLine(N, newLine, true);
    }

    /**
     * @brief   Writes a new line at the N-th position in DB, replacing the existing line.
     *
     * @param   N       Index of line to write (0-based).
     * @param   newLine The new line to be added at index N.
     * @return  `true` if the line was successfully written, `false` otherwise.
     */
    public boolean writeNthLineReplace(int N, String newLine) {
        return writeNthLine(N, newLine, false);
    }

    /**
     * @brief   Removes the N-th line from DB.
     * @details This method removes the N-th line from the database file and updates the internal cache accordingly. It
     *          returns the removed string, or `null` if the line did not exist. (The updated cache is used to
     *          rebuild the DB file, overwriting its previous content completely).
     *
     * @param   N Index of line to remove (0-based).
     * @return  String removed, or `null` if the line did not exist.
     */
    public String removeNthLine(int N) {
        if (this.cache.isEmpty() && !this.buildCache()) return null;
        if (N < 0 || N >= this.cache.size()) return null;

        String removedLine = this.cache.remove(N);

        if (!this.updateDBFromCache()) {
            // Rollback cache change to avoid conflicts
            this.cache.add(N, removedLine);
            removedLine = null;
        }

        return removedLine;
    }

    /**
     * @brief   Finds the first instance of a regex pattern in DB.
     * @details This method searches through the cached lines of DB to find the first occurrence of a specified regex
     *          pattern. It returns the index of the line where the pattern is first found. If the pattern is not found,
     *          it returns `-1`. If the cache is empty, it re-builds the cache before searching.
     *
     * @param   pattern Regex pattern to search for.
     * @return  Index of line where pattern is first found, `-1` if not found.
     */
    public int findFirstInstanceOfPattern(Pattern pattern) {
        if (this.cache.isEmpty() && !this.buildCache()) return -1;

        for (int i = 0; i < this.cache.size(); i++) {
            if (pattern.matcher(this.cache.get(i)).find()) {
                return i;
            }
        }

        return -1;
    }

    /**
     * @brief   Reads the first line containing a regex pattern from DB.
     * @details This method searches through the cached lines of DB to find the first occurrence of a specified regex
     *          pattern. It returns the content of the line where the pattern is first found. If the pattern is not
     *          found, it returns `null`. If the cache is empty, it re-builds the cache before searching.
     *
     * @param   pattern Regex pattern to search for.
     * @return  The first line containing the pattern, or `null` if not found.
     */
    public String readFirstLineContainingPattern(Pattern pattern) {
        if (this.cache.isEmpty() && !this.buildCache()) return null;

        int lineIndex = this.findFirstInstanceOfPattern(pattern);

        // One useless read on cache, but it keeps the code way cleaner
        if (lineIndex != -1) {
            return this.cache.get(lineIndex);
        }

        return null;
    }

    /**
     * @brief   Substitutes the first instance of a regex pattern in the database file with a new line.
     * @details This method searches for the first line in DB that matches the specified regex pattern and replaces it
     *          with the provided new line. It updates both the database file and the internal cache accordingly.
     *
     * @param   pattern The regex pattern to search for.
     * @param   newLine The new line to replace the found line.
     * @return  `true` if a substitution was made, `false` otherwise.
     */
    public boolean substituteFirstLineContainingPattern(Pattern pattern, String newLine) {
        int lineIndex = findFirstInstanceOfPattern(pattern);

        if (lineIndex == -1) return false;

        return writeNthLineReplace(lineIndex, newLine);
    }

    /**
     * @brief   Deletes the first line containing a regex pattern found in DB.
     * @details This method searches for the first line in DB that matches the specified regex pattern and removes it.
     *          It updates both the database file and the internal cache accordingly. It returns the removed line if it
     *          was found and deleted, or `null` otherwise.
     *
     * @param   pattern The regex pattern to search for.
     * @return  The removed line, or `null` if no deletion was made.
     */
    public String deleteFirstLineContainingPattern(Pattern pattern) {
        int lineIndex = findFirstInstanceOfPattern(pattern);

        if (lineIndex == -1) return null;

        return removeNthLine(lineIndex);
    }

    /**
     * @brief   Appends a new line at the end of DB.
     *
     * @param   newLine The new line to be appended.
     * @return  `true` if the line was successfully appended, `false` otherwise.
     */
    public boolean appendLine(String newLine) {
        return writeNthLineWShift(this.cache.size(), newLine);
    }

    @Override
    public String toString() {
        if (this.cache.isEmpty()) this.buildCache();

        StringBuilder output = new StringBuilder();

        for (String line : this.cache) {
            output.append(line).append(System.lineSeparator());
        }
        return output.toString();
    }
}
