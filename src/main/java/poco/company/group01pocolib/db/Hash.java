package poco.company.group01pocolib.db;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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
        Path filePath = Path.of("c:/temp/testOut.txt");
        String checksum;

        try {
            byte[] data = Files.readAllBytes(filePath);
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(data);
            checksum = new BigInteger(1, hash).toString(16);
        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
            checksum = null;
        }

        return checksum;
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
            String toHash = String.join(lineSeparator, linesList);
            byte[] data = toHash.getBytes(StandardCharsets.UTF_8);
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(data);
            checksum = new BigInteger(1, hash).toString(16);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
            checksum = null;
        }

        return checksum;
    }

    /**
     * @brief   This method takes 2 files as input (through their `Path`) and calculates the SHA- 256 hashes for both of
     *          them. Then, it returns the result of the comparison.
     *
     * @param   p1  Path to the first file
     * @param   p2  Path to the second file
     * @return  `true` if the hashes correspond, `false` if they don't
     */
    public static boolean compareFileHashes(Path p1, Path p2) {
        return getFileHash(p1).equals(getFileHash(p2));
    }
}