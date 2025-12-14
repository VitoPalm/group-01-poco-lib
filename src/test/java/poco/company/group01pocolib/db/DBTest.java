/**
 * @file DBTest.java
 * @brief Unit tests for the DB class.
 * @author Daniele Pepe
 * @author Francesco Marino
 */
package poco.company.group01pocolib.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @class DBTest
 * @brief Contains unit tests to verify setup and functionality of the DB class.
 */
class DBTest {

    @TempDir
    Path tempDir;

    private Path dbPath;
    private DB db;

    /**
     * @brief Sets up a temporary database file before each test.
     * @throws IOException if an I/O error occurs creating the file.
     */
    @BeforeEach
    void setUp() throws IOException {
        dbPath = tempDir.resolve("test.db");
        Files.createFile(dbPath);
        db = new DB(dbPath);
    }

    /**
     * @brief Test method for database initialization.
     */
    @Test
    void testDBInitialization() {
        assertNotNull(db);
        DB dbFromString = new DB(dbPath.toString());
        assertNotNull(dbFromString);
        assertEquals(dbPath.toString(), dbFromString.getDBPath());
    }

    /**
     * @brief Test method for the updateDBFileHash method even with file deletion.
     */
    @Test
    void testUpdateDBFileHash() {
        String initialHash = db.getDBFileHash();
        assertNotNull(initialHash);
        String check = db.updateAndGetDBFileHash();
        assertEquals(initialHash, check);
        // Delete the DB file to test hash update on missing file
        try {
            Files.delete(dbPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.updateDBFileHash();
        String updatedHash = db.getDBFileHash();
        assertNull(updatedHash);

    }

    /**
     * @brief Test method for the forceHashOnFile method.
     * @throws IOException if an I/O error occurs during file operations.
     */
    @Test
    void testForceHash() {
        // Add initial content to ensure cache is not empty
        db.appendLine("Initial content");
        
        // Force hash should return the same hash for unchanged file
        String initialHash = db.updateAndGetDBFileHash();
        assertNotNull(initialHash);
        String forcedHash = db.forceHashOnFile();
        assertEquals(initialHash, forcedHash);
        
        // Modify file externally (bypassing DB methods)
        try {
            Files.writeString(dbPath, "External modification\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Stored hash should still be the old one, even after update without force since the cache isn't changed
        assertEquals(initialHash, db.getDBFileHash());
        assertEquals(initialHash, db.updateAndGetDBFileHash());
        
        // Force hash should detect the change and update
        forcedHash = db.forceHashOnFile();
        assertNotEquals(initialHash, forcedHash);
        assertEquals(forcedHash, db.getDBFileHash());
        
        // Verify cache was rebuilt - should contain the external modification
        assertEquals("External modification", db.readNthLine(0));
        
        // Test with empty file, forcing hash should update accordingly
        try {
            Files.writeString(dbPath, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String emptyHash = db.forceHashOnFile();
        assertNotEquals(forcedHash, emptyHash);
        assertNull(db.readNthLine(0));
    }

    /**
     * @brief Test method for detecting line separators in the database file.
     */
    @Test
    void testDetectLineSeparator() {
        // Initially, the file is empty, so line separator should be system default
        String lineSeparator = db.detectLineSeparator();
        assertEquals(System.lineSeparator(), lineSeparator);

        // Write content with Unix line endings
        try {
            Files.writeString(dbPath, "Line 1\nLine 2\nLine 3\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        lineSeparator = db.detectLineSeparator();
        assertEquals("\n", lineSeparator);

        // Write content with Windows line endings
        try {
            Files.writeString(dbPath, "Line 1\r\nLine 2\r\nLine 3\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        lineSeparator = db.detectLineSeparator();
        assertEquals("\r\n", lineSeparator);
    }

    /**
     * @brief Test method for reading data from the database.
     */
    @Test
    void testReadData() {
        // Write some test data to the DB file
        try {
            Files.writeString(dbPath, "Line 0\nLine 1\nLine 2\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.buildCache();

        // Test reading the first line
        String line0 = db.readNthLine(0);
        assertEquals("Line 0", line0);

        // Test reading the second line
        String line1 = db.readNthLine(1);
        assertEquals("Line 1", line1);

        // Test reading the third line
        String line2 = db.readNthLine(2);
        assertEquals("Line 2", line2);

        // Test reading a non-existent line
        String lineNull = db.readNthLine(10);
        assertNull(lineNull);

        // Test reading with negative index
        String lineNegative = db.readNthLine(-1);
        assertNull(lineNegative);
    }

    /**
     * @brief Test method for writing data to the database.
     */
    @Test
    void testWriteData() {
        // Test appending a line
        assertTrue(db.appendLine("First line"));
        assertEquals("First line", db.readNthLine(0));

        // Test appending another line
        assertTrue(db.appendLine("Second line"));
        assertEquals("Second line", db.readNthLine(1));

        // Test writing with shift (insert at position 1)
        assertTrue(db.writeNthLineWShift(1, "Inserted line"));
        assertEquals("Inserted line", db.readNthLine(1));
        assertEquals("Second line", db.readNthLine(2));

        // Test writing with replace
        assertTrue(db.writeNthLineReplace(0, "Replaced first line"));
        assertEquals("Replaced first line", db.readNthLine(0));

        // Test writing to invalid index
        assertFalse(db.writeNthLineWShift(-1, "Invalid"));
        assertFalse(db.writeNthLineReplace(100, "Invalid"));
    }

    /**
     * @brief Test method for write rollback functionality.
     */
    @Test
    void testWriteRollback() {
        // Initial write
        db.appendLine("Line 1");
        db.appendLine("Line 2");

        // Make the file read-only to simulate write failure
        dbPath.toFile().setReadOnly();

        // Attempt to append should fail and rollback
        assertFalse(db.appendLine("Line 3"));

        // Restore write permissions for cleanup
        dbPath.toFile().setWritable(true);

        // Verify that the cache was rolled back - Line 3 should not exist
        assertEquals("Line 1", db.readNthLine(0));
        assertEquals("Line 2", db.readNthLine(1));
        assertNull(db.readNthLine(2));
    }

    /**
     * @brief Test method for remove rollback functionality.
     */
    @Test
    void testRemoveRollback() {
        // Initial write
        db.appendLine("Line 1");
        db.appendLine("Line 2");
        db.appendLine("Line 3");

        // Make the file read-only to simulate delete failure
        dbPath.toFile().setReadOnly();

        // Attempt to remove should fail and rollback
        assertNull(db.removeNthLine(1));

        // Restore write permissions for cleanup
        dbPath.toFile().setWritable(true);

        // Verify that the cache was rolled back - Line 2 should still exist
        assertEquals("Line 1", db.readNthLine(0));
        assertEquals("Line 2", db.readNthLine(1));
        assertEquals("Line 3", db.readNthLine(2));
    }

    /**
     * @brief Test method for deleting data from the database.
     */
    @Test
    void testDeleteData() {
        // Populate DB with test data
        db.appendLine("Line 0");
        db.appendLine("Line 1");
        db.appendLine("Line 2");
        db.appendLine("Line 3");

        // Test removing the second line
        String removed = db.removeNthLine(1);
        assertEquals("Line 1", removed);
        assertEquals("Line 2", db.readNthLine(1)); // The third line should now be at index 1

        // Test removing the first line
        removed = db.removeNthLine(0);
        assertEquals("Line 0", removed);
        assertEquals("Line 2", db.readNthLine(0)); // Line 2 should now be at index 0

        // Test removing with invalid index
        String nullRemoved = db.removeNthLine(100);
        assertNull(nullRemoved);

        nullRemoved = db.removeNthLine(-1);
        assertNull(nullRemoved);

        // Test clear method
        db.clear();
        assertNull(db.readNthLine(0));
    }

    /**
     * @brief Test method for auto-saving functionality of the database.
     */
    @Test
    void testAutoSave() {
        // Write data to DB
        db.appendLine("Line 1");
        db.appendLine("Line 2");

        // Create a new DB instance pointing to the same file
        // This simulates reopening the database
        DB newDB = new DB(dbPath);

        // Verify that data persists
        assertEquals("Line 1", newDB.readNthLine(0));
        assertEquals("Line 2", newDB.readNthLine(1));

        // Modify data with the new instance
        newDB.appendLine("Line 3");
        // Create another DB instance to verify persistence
        DB anotherDB = new DB(dbPath);
        assertEquals("Line 1", anotherDB.readNthLine(0));
        assertEquals("Line 2", anotherDB.readNthLine(1));
        assertEquals("Line 3", anotherDB.readNthLine(2));

        // Test that file content matches cache
        String fileContent;
        try {
            fileContent = Files.readString(dbPath);
            assertTrue(fileContent.contains("Line 1"));
            assertTrue(fileContent.contains("Line 2"));
            assertTrue(fileContent.contains("Line 3"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Test method for finding patterns in the database.
     */
    @Test
    void testFindFirstInstanceOfPattern() throws IOException {
        // Populate DB with test data
        db.appendLine("First line with test");
        db.appendLine("Second line");
        db.appendLine("Third line with test keyword");
        db.appendLine("Fourth line");

        // Test finding a pattern that exists
        Pattern pattern = Pattern.compile("test");
        int index = db.findFirstInstanceOfPattern(pattern);
        assertEquals(0, index);

        Pattern pattern2 = Pattern.compile("Third");
        index = db.findFirstInstanceOfPattern(pattern2);
        assertEquals(2, index);

        // Test finding a pattern that doesn't exist
        Pattern pattern3 = Pattern.compile("nonexistent");
        index = db.findFirstInstanceOfPattern(pattern3);
        assertEquals(-1, index);

        // Test with regex pattern
        Pattern regexPattern = Pattern.compile("line.*test");
        index = db.findFirstInstanceOfPattern(regexPattern);
        assertEquals(0, index);
    }

    /**
     * @brief Test method for reading lines containing patterns.
     */
    @Test
    void testReadFirstLineContainingPattern() throws IOException {
        // Populate DB with test data
        db.appendLine("alpha beta gamma");
        db.appendLine("delta epsilon");
        db.appendLine("zeta eta theta");

        // Test reading a line with a pattern that exists
        Pattern pattern = Pattern.compile("epsilon");
        String line = db.readFirstLineContainingPattern(pattern);
        assertEquals("delta epsilon", line);

        // Test reading a line with regex pattern
        Pattern pattern2 = Pattern.compile("alpha.*gamma");
        line = db.readFirstLineContainingPattern(pattern2);
        assertEquals("alpha beta gamma", line);

        // Test reading a line with a pattern that doesn't exist
        Pattern pattern3 = Pattern.compile("notfound");
        line = db.readFirstLineContainingPattern(pattern3);
        assertNull(line);
    }

    /**
     * @brief Test method for substituting lines containing patterns.
     */
    @Test
    void testSubstituteFirstLineContainingPattern() throws IOException {
        // Populate DB with test data
        db.appendLine("Line 1");
        db.appendLine("Line 2 to replace");
        db.appendLine("Line 3");

        // Test substituting a line with a pattern that exists
        Pattern pattern = Pattern.compile("to replace");
        boolean result = db.substituteFirstLineContainingPattern(pattern, "Line 2 replaced");
        assertTrue(result);
        assertEquals("Line 2 replaced", db.readNthLine(1));

        // Verify other lines are unchanged
        assertEquals("Line 1", db.readNthLine(0));
        assertEquals("Line 3", db.readNthLine(2));

        // Test substituting with a pattern that doesn't exist
        Pattern pattern2 = Pattern.compile("nonexistent");
        result = db.substituteFirstLineContainingPattern(pattern2, "Should not be added");
        assertFalse(result);

        // Test with regex pattern
        Pattern regexPattern = Pattern.compile("Line\\s+1");
        result = db.substituteFirstLineContainingPattern(regexPattern, "New first line");
        assertTrue(result);
        assertEquals("New first line", db.readNthLine(0));
    }

    /**
     * @brief Test method for deleting lines containing patterns.
     */
    @Test
    void testDeleteFirstLineContainingPattern() throws IOException {
        // Populate DB with test data
        db.appendLine("Keep this line");
        db.appendLine("Delete this line");
        db.appendLine("Keep this too");

        // Test deleting a line with a pattern that exists
        Pattern pattern = Pattern.compile("Delete");
        String removed = db.deleteFirstLineContainingPattern(pattern);
        assertEquals("Delete this line", removed);
        assertEquals("Keep this too", db.readNthLine(1));

        // Verify only 2 lines remain after deletion
        assertNotNull(db.readNthLine(0));
        assertNotNull(db.readNthLine(1));
        assertNull(db.readNthLine(2));

        // Test deleting with a pattern that doesn't exist
        Pattern pattern2 = Pattern.compile("notfound");
        removed = db.deleteFirstLineContainingPattern(pattern2);
        assertNull(removed);

        // Test with regex pattern
        Pattern regexPattern = Pattern.compile("Keep.*line");
        removed = db.deleteFirstLineContainingPattern(regexPattern);
        assertEquals("Keep this line", removed);
        assertEquals("Keep this too", db.readNthLine(0));
    }
}