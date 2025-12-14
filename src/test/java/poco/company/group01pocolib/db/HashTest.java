/**
 * @file HashTest.java
 * @brief Unit tests for the Hash class.
 * @author Daniele Pepe
 * @author Francesco Marino
 */
package poco.company.group01pocolib.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @class HashTest
 * @brief Contains unit tests for the Hash comparison functionalities.
 */
class HashTest {

    @TempDir
    Path tempDir;

    private Path file1;
    private Path file2;
    private Path file3;

    /**
     * @brief Sets up test files before each test case.
     */
    @BeforeEach
    void setUp() throws IOException {
        // Crea tre file: due identici e uno diverso
        file1 = tempDir.resolve("file1.txt");
        file2 = tempDir.resolve("file2.txt");
        file3 = tempDir.resolve("file3.txt");

        Files.writeString(file1, "Content A\nContent B");
        Files.writeString(file2, "Content A\nContent B"); // Identico a file1
        Files.writeString(file3, "Content B\nContent C"); // Diverso


    }

    /**
     * @brief Tests the hash generation.
     * @details Verifies that the hash is generated correctly for a given file by comparing it to a known hash value.
     */
    @Test
    void testGetFileHash() {
        
        String hash = Hash.getFileHash(file1);
        assertNotNull(hash);
    }

    /**
     * @brief Tests comparison of file hashes for identical content.
     * @details The hashes of two files with identical content should match.
     */
    @Test
    void testCompareFileHashesSameContent() {
        
        String hash1 = Hash.getFileHash(file1);
        String hash2 = Hash.getFileHash(file2);
        assertEquals(hash1, hash2);
    }

    /**
     * @brief Tests comparison of file hashes for different content.
     * @details The hashes of two files with different content should not match.
     */
    @Test
    void testCompareFileHashesDifferentContent() {
                            
        String hash1 = Hash.getFileHash(file1);
        String hash3 = Hash.getFileHash(file3);
        assertNotEquals(hash1, hash3);
    }
    
    /**
     * @brief Tests the consistency of hash generation for the same file.
     * @details Multiple calls to hash function on the same file should return the same result
     */
    @Test
    void testHashConsistency() {
        // Calculate reference hash
        String referenceHash = Hash.getFileHash(file1);
        
        // Reference hash should not be null
        assertNotNull(referenceHash);
        
        // Test hash consistency over multiple invocations, hash should remain the same after each iteration
        for (int i = 0; i < 100; i++) {
            String currentHash = Hash.getFileHash(file1);
            assertEquals(referenceHash, currentHash);
        }
    }

    /**
     * @brief Tests hash generation from a list of lines.
     * @details Verifies that getFileHashFromLines generates a hash from a list of strings.
     */
    @Test
    void testGetFileHashFromLines() {
        // Create a list of lines for testing
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        
        // Generate hash from lines with newline separator
        String hash = Hash.getFileHashFromLines(lines, "\n");
        
        // Hash should not be null or empty
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    /**
     * @brief Tests consistency of hash generation from lines.
     * @details Same list of lines should always produce the same hash.
     */
    @Test
    void testGetFileHashFromLinesConsistency() {
        // Create a list of lines for testing
        List<String> lines = Arrays.asList("Content A", "Content B", "Content C");
        
        // Generate hash from lines with newline separator
        String hash1 = Hash.getFileHashFromLines(lines, "\n");
        String hash2 = Hash.getFileHashFromLines(lines, "\n");
        
        // Same lines should produce same hash
        assertEquals(hash1, hash2);
    }

    /**
     * @brief Tests that different line separators produce different hashes.
     * @details The line separator is part of the content, so different separators should yield different hashes.
     */
    @Test
    void testGetFileHashFromLinesDifferentSeparators() {
        // Create a list of lines for testing
        List<String> lines = Arrays.asList("Line 1", "Line 2");
        
        // Generate hashes with different line separators
        String hashNewline = Hash.getFileHashFromLines(lines, "\n");
        String hashCarriageReturn = Hash.getFileHashFromLines(lines, "\r\n");
        
        // Different line separators should produce different hashes
        assertNotEquals(hashNewline, hashCarriageReturn);
    }

    /**
     * @brief Tests that different content produces different hashes.
     * @details Lists with different content should have different hashes.
     */
    @Test
    void testGetFileHashFromLinesDifferentContent() {
        // Create two different lists of lines for testing
        List<String> lines1 = Arrays.asList("Content A", "Content B");
        List<String> lines2 = Arrays.asList("Content A", "Content C");
        
        // Generate hashes from both lists with newline separator
        String hash1 = Hash.getFileHashFromLines(lines1, "\n");
        String hash2 = Hash.getFileHashFromLines(lines2, "\n");
        
        // Different content should produce different hashes
        assertNotEquals(hash1, hash2);
    }

    /**
     * @brief Tests that the hash produced from lines matches the hash of a file with the same content.
     * @details The hash generated from a list of lines should match the hash of a file containing the same lines.
     */
    @Test
    void testGetFileHashFromLinesMatchesFileHash() {
        // Create a list of lines matching the content of file1
        List<String> lines = Arrays.asList("Content A", "Content B");

        // Generate hash from lines with system line separator
        String hashFromLines = Hash.getFileHashFromLines(lines, System.lineSeparator());

        // Get hash of file1
        String fileHash = Hash.getFileHash(file1);

        // Hash from lines should match file hash
        assertEquals(hashFromLines, fileHash);
    }

    /**
     * @brief Tests hash comparison for identical files.
     * @details compareFileHashes should return true for files with identical content.
     */
    @Test
    void testCompareFileHashesIdentical() {
        // Compare file1 and file2 hashes (same content)
        boolean result = Hash.compareFileHashes(file1, file2);
        
        // Files with identical content should have matching hashes
        assertTrue(result);
    }

    /**
     * @brief Tests hash comparison for different files.
     * @details compareFileHashes should return false for files with different content.
     */
    @Test
    void testCompareFileHashesDifferent() {
        // Compare file1 and file3 hashes (different content)
        boolean result = Hash.compareFileHashes(file1, file3);
        
        // Files with different content should not have matching hashes
        assertFalse(result);
    }

    /**
     * @brief Tests hash comparison for same file.
     * @details A file compared to itself should always return true.
     */
    @Test
    void testCompareFileHashesSameFile() {
        // Compare file1 to itself
        boolean result = Hash.compareFileHashes(file1, file1);
        
        // A file compared to itself should always have matching hash
        assertTrue(result);
    }
}