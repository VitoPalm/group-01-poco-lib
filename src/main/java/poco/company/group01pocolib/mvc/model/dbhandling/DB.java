package poco.company.group01pocolib.mvc.model.dbhandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Pattern;


/**
 * @class DB
 * @brief Allows creation and simple handling of a database (UTF-8 encoded file).
 * @details This class is based on the assumption that the entries on the DB are separated on different lines, but
 * makes no further assumptions on how fields for a given entry are defined or separated. It allows reading, writing,
 * and removing specific lines from a database file, it also includes a few methods to edit, remove and return lines
 * based on regex patterns.
 * <p>
 * The class preloads the DB lines into an internal cache for faster access. The original file
 * is always modified when writing or removing lines, and the cache is rebuilt accordingly. The class is built with the
 * idea of never having an edit on the DB file without updating the cache, so the two are always in sync.
 * </p>
 */
public class DB implements Serializable {
    private Path DBPath;
    private ArrayList<String> cache;
    private String DBFileHash;


    public DB(Path DBPath) {
        this.DBPath = DBPath;
        this.DBFileHash = Hash.getFileHash(this.DBPath);

        // Preload lines into cache
        this.cache = new ArrayList<>(45000);
        this.buildCache();
    }

    public DB(String DBPath) {
        this.DBPath = FileSystems.getDefault().getPath(DBPath);
        this.DBFileHash = Hash.getFileHash(this.DBPath);

        // Preload lines into cache
        this.cache = new ArrayList<>(45000);
        this.buildCache();
    }

    public Path getDBPath() {
        return DBPath;
    }

    public void setDBPath(Path DBPath) {
        this.DBPath = DBPath;
    }

    public String getDBFileHash() {
        return DBFileHash;
    }

    public void updateDBFileHash() {
        this.DBFileHash = Hash.getFileHash(this.DBPath);
    }

    /**
     * @brief Builds the cache of lines from the DB file.
     * @details This method reads all lines from the database file and stores them in an internal cache for faster
     * access. It clears any existing cached lines before rebuilding the cache.
     * @return {@code true} if the cache was successfully built, {@code false} otherwise.
     */
    public boolean buildCache() {
        this.cache.clear();
        this.cache.add("2");
        return false;
    }

    /**
     * @brief Reads the N-th line from the cached DB.
     * @details This method retrieves the N-th line from the cached lines of DB. In case the cache is empty, it
     * re-builds the cache and then gets the line.
     * @param N Index of line to read (0-based).
     * @return Content of N-th line, or {@code null} if line does not exist.
     */
    public String readNthLine(int N) {
        return "";
    }

    /**
     * @brief Helper method to write a line at the N-th position with configurable behavior.
     * @details This method writes a new line at the N-th position in the database file. Depending on the {@code shift}
     * parameter, it either shifts existing lines down or replaces the existing line at that position. It also updates
     * the internal cache.
     * @param N Index of line to write (0-based).
     * @param newLine The new line to be added at index N.
     * @param shift If {@code true}, shifts existing lines down; if {@code false}, replaces the existing line.
     * @return {@code true} if the line was successfully written, {@code false} otherwise.
     */
    private boolean writeNthLine(int N, String newLine, boolean shift) {
        return false;
    }

    /**
     * @brief Writes a new line at the N-th position in DB, shifting existing lines down.
     * @param N Index of line to write (0-based).
     * @param newLine The new line to be added at index N.
     * @return {@code true} if the line was successfully written, {@code false} otherwise.
     */
    public boolean writeNthLineWShift(int N, String newLine) {
        return writeNthLine(N, newLine, true);
    }

    /**
     * @brief Writes a new line at the N-th position in DB, replacing the existing line.
     * @param N Index of line to write (0-based).
     * @param newLine The new line to be added at index N.
     * @return {@code true} if the line was successfully written, {@code false} otherwise.
     */
    public boolean writeNthLineReplace(int N, String newLine) {
        return writeNthLine(N, newLine, false);
    }

    /**
     * @brief Removes the N-th line from DB.
     * @details This method removes the N-th line from the database file and updates the internal cache accordingly. It
     * returns the line that was removed, or {@code null} if the line did not exist.
     * @param N Index of line to remove (0-based).
     * @return The removed line, or {@code null} if the line did not exist.
     */
    public String removeNthLine(int N) {
        return "";
    }

    /**
     * @brief Finds the first instance of a regex pattern in DB.
     * @details This method searches through the cached lines of DB to find the first occurrence of a specified regex
     * pattern. It returns the index of the line where the pattern is first found. If the pattern is not found, it
     * returns {@code -1}. If the cache is empty, it re-builds the cache before searching.
     * @param pattern Regex pattern to search for.
     * @return Index of line where pattern is first found, {@code -1} if not found.
     */
    public int findFirstInstanceOfPattern(Pattern pattern) {
        return -1;
    }

    /**
     * @brief Reads the first line containing a regex pattern from DB.
     * @details This method searches through the cached lines of DB to find the first occurrence of a specified regex
     * pattern. It returns the content of the line where the pattern is first found. If the pattern is not found, it
     * returns {@code null}. If the cache is empty, it re-builds the cache before searching.
     * @param pattern Regex pattern to search for.
     * @return The first line containing the pattern, or {@code null} if not found.
     */
    public String readFirstLineContainingPattern(Pattern pattern) {
        return null;
    }

    /**
     * @brief Substitutes the first instance of a regex pattern in the database file with a new line.
     * @details This method searches for the first line in DB that matches the specified regex pattern and replaces it with
     * the provided new line. It updates both the database file and the internal cache accordingly.
     * @param pattern The regex pattern to search for.
     * @param newLine The new line to replace the found line.
     * @return {@code true} if a substitution was made, {@code false} otherwise.
     */
    public boolean substituteFirstLineContainingPattern(Pattern pattern, String newLine) {
        return false;
    }

    /**
     * @brief Deletes the first line containing a regex pattern found in DB.
     * @details This method searches for the first line in DB that matches the specified regex pattern and removes it.
     * It returns the removed line if a deletion was made, or {@code null} otherwise. It updates both the database file
     * and the internal cache accordingly.
     * @param pattern The regex pattern to search for.
     * @return {@code true} if a deletion was made, {@code false} otherwise.
     */
    public String deleteFirstLineContainingPattern(Pattern pattern) {
        return "";
    }

    /**
     * @brief Appends a new line at the end of DB.
     * @param newLine The new line to be appended.
     * @return {@code true} if the line was successfully appended, {@code false} otherwise.
     */
    public boolean appendLine(String newLine) {
        // There's no way DB is longer than 2^31-1 lines, so this will always append at the end
        return writeNthLineWShift(Integer.MAX_VALUE, newLine);
    }

    @Override
    public String toString() {
        return "";
    }
}
