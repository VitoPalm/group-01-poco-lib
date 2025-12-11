/**
 * @file BookSet.java
 * @brief This file contains the definition of BookSet class
 * @author Francesco Marino
 * @author Giovanni Orsini
 */

package poco.company.group01pocolib.mvc.model;

import poco.company.group01pocolib.db.DB;
import poco.company.group01pocolib.db.Hash;
import poco.company.group01pocolib.db.omnisearch.Index;
import poco.company.group01pocolib.db.omnisearch.Search;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.io.IOException;
import java.util.*;

/**
 * @class   BookSet
 * @brief   A collection of books that acts as the real data interface for the main controller to interact with.
 * @details This class is the real data model for books for the MVC structure of this project. It allows object
 *          permanence between sessions through serialization, and allows searching through an indexed DB of books.
 *          <br><br>
 *          The fact that the collection of books is essentially memorized in three different data structures (the Set,
 *          the DB file, and the Index) may seem like a waste of resources, but it allows for some serious optimization
 *          of search operations, as well as a mechanism to recover the data in the case of the application being run on
 *          a different machine or after an update that may have corrupted the serialized data. (This is due to
 *          serialization requiring the structure of all classes involved to remain the same between sessions)
 */
public class BookSet implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Set<Book> bookSet;      
    private Index<Book> bookIndex;
    private DB bookDB;

    private Book dummy;         ///< Used in {@link poco.company.group01pocolib.mvc.model.BookSet.isStored isStored()} as a dummy object for the `contains()` method of the Collection

    private String lastKnownDBHash;
    private String DBPath;
    private String serializationPath;
    
    /**
     * @brief   Default constructor only initializes an empty collection of `Book` elements
     */
    public BookSet(){
        this.bookSet = new HashSet<>();
        this.bookIndex = new Index<>();

        List<String> authors = new ArrayList<>();
        authors.add("Doug Lowe");
        this.dummy = new Book("Java All-in-one for Dummies", authors, "", 2023, 0);
    }

    /**
     * @brief   Get the book set
     * @return  The `Set` of books in the collection
     */
    public Set<Book> getBookSet() {
        return bookSet;
    }

    /**
     * @brief   Set the `bookSet` `Set`
     * @param   bookSet The `Set` of books to set `bookSet` to
     */
    public void setBookSet(HashSet<Book> bookSet) {
        this.bookSet = bookSet;
    }

    /**
     * @brief   Get the book index
     * @return  The Index object for the books
     */
    public Index<Book> getBookIndex() {
        return bookIndex;
    }

    /**
     * @brief   Set the book index
     * @param   bookIndex The Index object to set `bookIndex` to
     */
    public void setBookIndex(Index<Book> bookIndex) {
        this.bookIndex = bookIndex;
    }

    /**
     * @brief   Get the DB object
     * @return  The DB object
     */
    public DB getBookDB() {
        return bookDB;
    }

    /**
     * @brief   Set the DB object
     * @param   bookDB The new DB object to set `bookDB` to
     */
    public void setBookDB(DB bookDB) {
        this.bookDB = bookDB;
    }

    /**
     * @brief   Get the last known DB hash
     * @return  The last known DB hash
     */
    public String getLastKnownDBHash() {
        return lastKnownDBHash;
    }

    /**
     * @brief   Updates the `lastKnownDBHash` field by rehashing the current DB
     */
    public void updateLastKnownDBHash() {
        this.lastKnownDBHash = this.bookDB.updateAndGetDBFileHash();
    }

    /**
     * @brief   Get the path to the DB file
     * @return  The path to the DB file
     */
    public String getDBPath() {
        return DBPath;
    }

    /**
     * @brief   Set the path to the DB file
     * @param   DBPath The path to the DB file
     */
    public void setDBPath(String DBPath) {
        this.DBPath = DBPath;
    }

    /**
     * @brief   Get the path to the serialized version of the BookSet
     * @return  The path to the serialized version of the BookSet
     */
    public String getSerializationPath() {
        return serializationPath;
    }

    /**
     * @brief   Set to the path of the serialized version of the BookSet
     * @param   serializationPath The path to the serialized version of the BookSet
     */
    public void setSerializationPath(String serializationPath) {
        this.serializationPath = serializationPath;
    }

    /**
     * @brief   Get all the books in the collection as a list
     * @return  A `List` of all the books in the collection
     */
    public List<Book> getListOfBooks() {
        return new ArrayList<>(bookSet);
    }

    /**
     * @brief   Loads a `BookSet` from a serialized version on disk
     * @details If the DB file has changed since the last serialization, the `BookSet` will be rebuilt from the DB
     *          file. Otherwise, it will be loaded as is. This includes re-hydrating the Index and the DB object itself.
     *          (This is done to avoid rebuilding the DB's internal Cache from scratch)
     *
     * @param   serializationPath The path to the serialized `BookSet`
     * @param   DBPath The path to the DB file
     * @return  The loaded `BookSet` object
     */
    public static BookSet loadFromSerialized(String serializationPath, String DBPath) {
        Object obj;
        BookSet bookSet = null;

        // Attempt to read the serialized BookSet from disk
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(serializationPath)))) {
        
            obj = in.readObject();
            bookSet = (BookSet) obj;

        // If any error occurs during deserialization, rebuild the BookSet from the DB
        } catch (IOException | ClassNotFoundException e) {
            bookSet = new BookSet();
            bookSet.setDBPath(DBPath);
            bookSet.rebuildFromDB(DBPath);
            return bookSet;
        }

        // Create a new DB object using the provided DB path
        DB currentDB = new DB(DBPath);
        String currentDBHash = currentDB.getDBFileHash();

        // Check if the DB file has changed since the last serialization by comparing hashes
        if (currentDBHash.equals(bookSet.getLastKnownDBHash())) {
            bookSet.setDBPath(DBPath);
            bookSet.setBookDB(currentDB);
            return bookSet;
        } else {
            bookSet.setDBPath(DBPath);
            bookSet.rebuildFromDB(DBPath);
        }

        return bookSet;
    }

    /**
     * @brief   Rebuilds the `BookSet` from the DB file
     * @param   DBPath The path to the DB file
     */
    public void rebuildFromDB(String DBPath) {
        //Initialize the DB object for rebuilding
        this.bookDB = new DB(DBPath);
    
        // Clear in-memory data structures before reloading
        this.bookSet.clear();
        this.bookIndex = new Index<>(); 

        int i = 0;
        String line;
        
        // Iterate through each line in the DB file and parse it into a Book object
        while ((line = this.bookDB.readNthLine(i)) != null) {
            try {
            
                Book book = Book.fromDBString(line);
                
                this.bookSet.add(book);
                this.bookIndex.add(book.toSearchableString(), book);

            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }

        // Update the hash to indicate that we are synchronized with the DB
        updateLastKnownDBHash();
    }

    /**
     * @brief   Adds a book to collection. If the book already exists (based on ISBN), it is edited.
     * @param   book The book object to add or edit.
     * @warning **Overwriting an object cannot undone**: Be sure to call this method after checking if the object {@link poco.company.group01pocolib.mvc.model.BookSet.isStored isStored()} when only wishing to add a new Book to the BookSet
     */
    public void addOrEditBook(Book book){
        
        // Removes the book if it already exists
        bookSet.remove(book);
        bookIndex.remove(book);
        
        // Adds the book (new or updated)
        bookSet.add(book);
        bookIndex.add(book.toSearchableString(), book);
        
        // Syncs the changes to DB and serialized file
        syncOnWrite();
    }

    /**
     * @brief   Removes a book from the collection
     * @param   isbn The ISBN of the book to remove
     */
    public void removeBook(String isbn){
        Book dummyBook = new Book();
        dummyBook.setIsbn(isbn);
        
        // Removes the book from the set and index
        bookSet.remove(dummyBook);
        bookIndex.remove(dummyBook);
        
        // Syncs the changes to DB and serialized file
        syncOnWrite();
    } 

    /**
     * @brief   Get a book from its ISBN
     *
     * @param   isbn The ISBN of the book to get
     * @return  The Book object if found, null otherwise
     */
    public Book getBook(String isbn){
        for (Book book : bookSet) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    /**
     * @brief   Checks whether a book is already registred in the Set
     * @details The search is performed on the Collection used to store the BookSet. Two Books are {@link poco.company.group01pocolib.mvc.model.Book.equals equal} when they have the same unique identifier (`isbn`)
     * 
     * @param   book The book to search 
     * @return  Returns `true` if a result is found for the book's unique identifier
     * @author  Giovanni Orsini
     */
    public boolean isStored(Book book) {
        return bookSet.contains(book);
    }

    /**
     * @brief   Checks whether a book is already registred in the Set
     * @details The search is performed on the Collection used to store the BookSet. Two Books are {@link poco.company.group01pocolib.mvc.model.Book.equals equal} when they have the same unique identifier (`isbn`)
     * 
     * @param   isbn The unique identifier of the book to search
     * @return  Returns `true` if a result is found for the book's unique identifier
     * @author  Giovanni Orsini
     */
    public boolean isStored(String isbn) {
        dummy.setIsbn(isbn);
        return bookSet.contains(dummy);
    }

    /**
     * @brief   Perform search on the indexed books with ranking
     *
     * @param   rawQuery The raw search query
     * @return  A list of books matching the search query, ranked by relevance
     */
    public List<Book> search(String rawQuery) {

        ArrayList<Search.SearchResult<Book>> searchResults = Search.search(rawQuery, bookIndex);

        if (searchResults == null || searchResults.isEmpty()) {
            return new ArrayList<>();
        }

        searchResults.sort((r1,r2)-> {
            int hitComparison = Integer.compare(r2.hits, r1.hits);
            if (hitComparison != 0) {
                return hitComparison;
            }

            int score1 = calculateScore(r1.item, rawQuery);
            int score2 = calculateScore(r2.item, rawQuery);
            return Integer.compare(score2, score1);
        });

        List<Book> rankedBooks = new ArrayList<>();
        for (Search.SearchResult<Book> result : searchResults) {
            rankedBooks.add(result.item);
        }   

        return rankedBooks;

        //TODO: Vito pls look at this
    }

    /**
     * @brief   Perform search on the indexed books without ranking
     *
     * @param   rawQuery The raw search query
     * @return  A list of books matching the search query
     */
    private List<Book> rawSearch(String rawQuery) {
        //TODO: Vito pls look at this
        return null;
    }

    /**
     * @brief   Calculate the relevance score of a book for a given search query
     *
     * @param   book The book to calculate the score for
     * @param   rawQuery The raw search query
     * @return  The relevance score of the book
     */
    private int calculateScore(Book book, String rawQuery) {
        //TODO: Vito pls look at this
        return 0;
    }

    /**
     * @brief   Synchronizes the current state of the BookSet to the DB and serialized file on write operations
     */
    private void syncOnWrite() {
        
        // Clear the DB file
        bookDB.clear();
            
        // Write all books to the DB file
        for (Book book : bookSet) {
            bookDB.appendLine(book.toDBString());
        }
        
        // Update the hash after writing to DB
        updateLastKnownDBHash();
        
        // Save the serialized version
        saveToSerialized();
    }

    /**
     * @brief   Saves the current state of the BookSet to a serialized file on disk
     */
    private void saveToSerialized() {

        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(serializationPath)))) {
            out.writeObject(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief   Returns a string representation of the BookSet.
     * @return  A string representation of the BookSet.
     */
    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();

        for (Book book : bookSet) {
            buff.append(book.toString()).append("\n");
        }

        return buff.toString();
    }
}