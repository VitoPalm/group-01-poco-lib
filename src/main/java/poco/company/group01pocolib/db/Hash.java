package poco.company.group01pocolib.db;

import java.io.*;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * Simple class containing two static methods, one to hash a file, and another to compare two files by their hashes.
 */
public class Hash {
    /**
     * This method takes a `path` as input, and calculates a hash for the corresponding file using SHA-256.
     * @param path Path to the file we intend to hash
     * @return String containing the hash of the input file, calculated using SHA-256
     */
    static public String getFileHash(Path path) {
        return "";
    }

    /**
     * This method takes 2 files as input (through their paths) and calculates the hashes for both of them. Then, it returns
     * the result of the comparison
     * @param p1 Path to the first file
     * @param p2 Path to the second file
     * @return `true` if the hashes correspond, `false` if they don't
     */
    static public boolean compareFileHashes(Path p1, Path p2) {
        return getFileHash(p1).equals(getFileHash(p2));
    }
}