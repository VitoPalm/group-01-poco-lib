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
        // Crea un file DB temporaneo per ogni test
        dbPath = tempDir.resolve("test.db");
        Files.createFile(dbPath);
        db = new DB(dbPath);
    }

    /**
     * @brief Test method for reading data from the database.
     */
    @Test
    void testReadData() throws IOException {
        // Write some test data to the DB file
        Files.writeString(dbPath, "Line 0\nLine 1\nLine 2\n");
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
    void testWriteData() throws IOException {
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
     * @brief Test method for deleting data from the database.
     */
    @Test
    void testDeleteData() throws IOException {
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
    void testAutoSave() throws IOException {
        // Write data to DB
        db.appendLine("Persistent Line 1");
        db.appendLine("Persistent Line 2");

        // Create a new DB instance pointing to the same file
        // This simulates reopening the database
        DB newDB = new DB(dbPath);

        // Verify that data persists
        assertEquals("Persistent Line 1", newDB.readNthLine(0));
        assertEquals("Persistent Line 2", newDB.readNthLine(1));

        // Modify data with the new instance
        newDB.appendLine("Persistent Line 3");

        // Create another DB instance to verify persistence
        DB anotherDB = new DB(dbPath);
        assertEquals("Persistent Line 1", anotherDB.readNthLine(0));
        assertEquals("Persistent Line 2", anotherDB.readNthLine(1));
        assertEquals("Persistent Line 3", anotherDB.readNthLine(2));

        // Test that file content matches cache
        String fileContent = Files.readString(dbPath);
        assertTrue(fileContent.contains("Persistent Line 1"));
        assertTrue(fileContent.contains("Persistent Line 2"));
        assertTrue(fileContent.contains("Persistent Line 3"));
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

        // Test finding a pattern that exists later
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