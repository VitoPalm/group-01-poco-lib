package poco.company.group01pocolib.db;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Base64;

/**
 * @class   Hash
 * @brief   Simple class containing static methods, for calculating hashes
 */
public class Hash {
    /**
     * @brief   This method takes a `path` as input, and calculates a hash for the corresponding file using SHA-256.
     *
     * @param   path Path to the file we intend to hash.
     * @return  String containing the hash of the input file, calculated using SHA-256.
     */
    public static String getFileHash(Path path) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            FileInputStream fis = new FileInputStream(path.toFile());
            byte[] byteArray = new byte[1024];
            int bytesCount;

            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }

            fis.close();

            byte[] bytes = digest.digest();
            return Base64.getEncoder().encodeToString(bytes);

        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @brief   Allows to get the SHA-256 hash of a file by checking a List containing all the lines in the file.
     * @details Rather than using the file itself, this method calculates the hash of a `File` from a `List` of
     *          `Strings` corresponding to the lines of the file itself.
     *          <br><br>
     *          This process is done in a way that makes sure the hash resulting from this operation is the same that
     *          would result from calculating the hash of the file itself, allowing many operations to be executed way
     *          more efficiently by only needing to operate in memory.
     *
     * @param   linesList       The `List` of lines to calculate the hash on.
     * @param   lineSeparator   The character that was used to separate the lines in the file itself.
     * @return  String containing the hash of the input `List`, computed as if it was a `File`.
     */
public static String getFileHashFromLines(List<String> linesList, String lineSeparator) {
    String checksum;

    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        for (String line : linesList) {
            byte[] lineBytes = line.getBytes(StandardCharsets.UTF_8);
            digest.update(lineBytes);
            // Add line separator after each line (including the last one)
            digest.update(lineSeparator.getBytes(StandardCharsets.UTF_8));
        }

        byte[] hash = digest.digest();
        checksum = Base64.getEncoder().encodeToString(hash);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        checksum = null;
    }

    return checksum;
}

    /**
     * @brief   Compares the SHA-256 hashes of two files.
     * @details This method takes 2 files as input (through their `Path`) and calculates the SHA- 256 hashes for both of
     *          them. Then, it returns the result of the comparison.
     *
     * @param   p1  Path to the first file
     * @param   p2  Path to the second file
     * @return  `true` if the hashes correspond, `false` if they don't
     */
    public static boolean compareFileHashes(Path p1, Path p2) {
        return getFileHash(p1).equals(getFileHash(p2));
    }

    /**
     * @brief   Compares the SHA-256 hash of a file with that of a list of strings.
     * @details This method takes as input a file (through its `Path`) and a `List<String>` containing the lines of
     *          another file. It calculates the SHA-256 hashes for both of them, and then returns the result of the
     *          comparison.
     */
    public static boolean compareFileHashWithLines(Path p, List<String> linesList, String lineSeparator) {
        return getFileHash(p).equals(getFileHashFromLines(linesList, lineSeparator));
    }

    /**
     * @brief   Compares the SHA-256 hashes of two lists of strings. (comparison is independent of line endings)
     * @details This method takes as input two `List<String>`, each containing the lines of a file. It calculates the
     *          SHA-256 hashes for both of them, and then returns the result of the comparison.
     *
     * @param   linesList1     The first `List<String>` to compare
     * @param   linesList2     The second `List<String>` to compare
     * @param   lineSeparator1 The character that was used to separate the lines in the first file.
     * @param   lineSeparator2 The character that was used to separate the lines in the second file.
     * @return  `true` if the hashes correspond, `false` if they don't
     */
    public static boolean compareFileHashesFromLines(List<String> linesList1, List<String> linesList2,
                                                     String lineSeparator1, String lineSeparator2) {
        return getFileHashFromLines(linesList1, lineSeparator1).equals(getFileHashFromLines(linesList2, lineSeparator2));
    }
}