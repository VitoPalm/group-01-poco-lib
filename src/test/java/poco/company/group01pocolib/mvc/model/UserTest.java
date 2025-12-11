/**
 * @file UserTest.java
 * @brief Unit tests for the User class.
 * @author Daniele Pepe
 */
package poco.company.group01pocolib.mvc.model;

import org.junit.jupiter.api.Test;

import poco.company.group01pocolib.exceptions.UserDataNotValidException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @class UserTest
 * @brief Contains unit tests to verify the correctness of the User class methods and validation logic.
 */
public class UserTest {

    /**
     * @brief Tests that a User object is created correctly when valid arguments are provided.
     */
    @Test
    public void testUserCreationValid() {
        // Create a User object with valid data
        User user = new User("ABC12345", "Max", "Verstappen", "max.verstappen@fia.com");
        
        // Assertions to verify the user object
        assertEquals("ABC12345", user.getId());
        assertEquals("Max", user.getName());
        assertEquals("Verstappen", user.getSurname());
        assertEquals("max.verstappen@fia.com", user.getEmail());
        assertEquals(0, user.getBorrowedBooksCount());
        assertEquals(0, user.getBorrowedBooksEverCount());
    }

    /**
     * @brief Tests that the constructor throws UserDataNotValidException when an invalid email is provided.
     */
    @Test
    public void testUserCreationInvalidEmail() {
        assertThrows(UserDataNotValidException.class, () -> {
            new User("ABC12345", "Lando", "Norris", "landonorris-fia.com");
        });
    }

    /**
     * @brief Tests that the constructor throws UserDataNotValidException when an invalid ID is provided.
     */
    @Test
    public void testUserCreationInvalidId() {
        assertThrows(UserDataNotValidException.class, () -> {
            new User("a","Oscar","Piastri","oscarpiastri@fia.com");
        });
    }

    /**
     * @brief Tests the edit method to ensure User details are updated correctly. 
     */
    @Test
    public void testUserEdit(){
        User user = new User("DEF67890", "Arthur", "leclerc", "arthur.leclerc@fia.com");
        
        user.setId("DEF67891");
        user.setName("Charles");
        user.setSurname("Leclerc");
        user.setEmail("charles.leclerc@fia.com");
        user.setBorrowedBooksCount(1);
        user.setBorrowedBooksEverCount(2);

        assertEquals("DEF67891", user.getId());
        assertEquals("Charles", user.getName());
        assertEquals("Leclerc", user.getSurname());
        assertEquals("charles.leclerc@fia.com", user.getEmail());
        assertEquals(1, user.getBorrowedBooksCount());
        assertEquals(2, user.getBorrowedBooksEverCount());
    }

    @Test
    public void testCanBorrw(){
        User user = new User("GHI11223", "Sebastian", "Vettel", "sebastian.vettel@fia.com");
        user.setBorrowedBooksCount(3);
        assertFalse(user.canBorrow());

        user.setBorrowedBooksCount(1);
        assertTrue(user.canBorrow());
    }

    @Test
    public void testIncrementDecrementBorrowedBooksCount(){
        User user = new User("JKL44556", "Lewis", "Hamilton", "lewis.hamilton@fia.com");
        user.setBorrowedBooksCount(2);

        user.incrementBorrowedBooksCount();
        assertEquals(3, user.getBorrowedBooksCount());
        
        user.decrementBorrowedBooksCount();
        user.decrementBorrowedBooksCount();
        assertEquals(1, user.getBorrowedBooksCount());
    }

    @Test
    public void testHashCode(){
        User user1 = new User("MNO77889", "Fernando", "Alonso", "fernando.alonso@fia.com");
        User user2 = new User("MNO77889", "Fernando", "Alonso", "fernando.alonso@fia.com");
        User user3 = new User("JKL44556", "Lewis", "Hamilton", "lewis.hamilton@fia.com");

        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user2.hashCode(), user3.hashCode());
    }

    @Test
    public void testEquals(){
        User user1 = new User("MNO77889", "Fernando", "Alonso", "fernando.alonso@fia.com");
        User user2 = new User("MNO77889", "Fernando", "Alonso", "fernando.alonso@fia.com");
        User user3 = new User("JKL44556", "Lewis", "Hamilton", "lewis.hamilton@fia.com");

        assertEquals(user1, user2);
        assertNotEquals(user2, user3);
    }

    @Test 
    public void testFromDBString(){
        
        String userString = "PQR99000\u001CKimi\u001CAntonelli\u001Ckimi.antonelli@fia.com\u001C0\u001C0";

        User user = User.fromDBString(userString);
        
        User expectedUser = new User("PQR99000", "Kimi", "Antonelli", "kimi.antonelli@fia.com");
        
        assertEquals(expectedUser, user);
    }

    @Test
    public void testToDBString(){
        User user = new User("STU22334", "Valtteri", "Bottas", "valtteri.bottas@fia.com");
        user.setBorrowedBooksCount(2);
        user.setBorrowedBooksEverCount(5);
        String expectedString = "STU22334\u001CValtteri\u001CBottas\u001Cvaltteri.bottas@fia.com\u001C2\u001C5";
        assertEquals(expectedString, user.toDBString());
    }

    @Test
    public void testToSearchableString(){
        User user = new User("VWX55667", "George", "Russell", "george.russell@fia.com");
        String expectedString = "vwx55667georgerussellgeorge.russell@fia.com";
        assertEquals(expectedString, user.toSearchableString());
    }
}