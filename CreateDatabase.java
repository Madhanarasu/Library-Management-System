import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * CreateDatabase migration:
 * - If library.db does not exist, creates it.
 * - Creates tables `books` and `issued_books` (same schema used by Library.java).
 * - Inserts sample data: a few books and one issued_books entry.
 *
 * Usage:
 * - Call CreateDatabase.ensureDatabase() before Library connects (e.g. at the top of Library.main).
 * - Or run this class once: `java -cp ".:sqlite-jdbc-<version>.jar" CreateDatabase`
 */
public class CreateDatabase {
    private static final String DB_URL = "jdbc:sqlite:library.db";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static void ensureDatabase() {
        File f = new File("library.db");
        if (f.exists()) {
            System.out.println("Library DB already exists: library.db");
            return;
        }

        System.out.println("Creating library.db and seeding sample data...");
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            String createBooks =
                    "CREATE TABLE IF NOT EXISTS books ("
                            + "id REAL PRIMARY KEY,"
                            + "name TEXT NOT NULL,"
                            + "author TEXT,"
                            + "edition INTEGER,"
                            + "yop INTEGER"
                            + ");";

            String createIssued =
                    "CREATE TABLE IF NOT EXISTS issued_books ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "stu_name TEXT NOT NULL,"
                            + "stu_reg TEXT NOT NULL,"
                            + "book_id REAL NOT NULL,"
                            + "issue_date TEXT,"
                            + "due_date TEXT,"
                            + "returned INTEGER DEFAULT 0,"
                            + "FOREIGN KEY(book_id) REFERENCES books(id)"
                            + ");";

            try (Statement st = conn.createStatement()) {
                st.execute(createBooks);
                st.execute(createIssued);
            }

            // Insert sample books
            String insertBook = "INSERT INTO books (id, name, author, edition, yop) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertBook)) {
                ps.setDouble(1, 1001.0);
                ps.setString(2, "Introduction to Java");
                ps.setString(3, "John Doe");
                ps.setInt(4, 3);
                ps.setInt(5, 2018);
                ps.executeUpdate();

                ps.setDouble(1, 1002.0);
                ps.setString(2, "Data Structures");
                ps.setString(3, "Jane Smith");
                ps.setInt(4, 2);
                ps.setInt(5, 2016);
                ps.executeUpdate();

                ps.setDouble(1, 1003.0);
                ps.setString(2, "Database Systems");
                ps.setString(3, "Alan Turing");
                ps.setInt(4, 1);
                ps.setInt(5, 2020);
                ps.executeUpdate();
            }

            // Insert one issued_books sample row (book 1001 issued)
            String insertIssued = "INSERT INTO issued_books (stu_name, stu_reg, book_id, issue_date, due_date, returned) VALUES (?, ?, ?, ?, ?, ?)";
            java.time.LocalDate issueDate = java.time.LocalDate.now();
            java.time.LocalDate dueDate = issueDate.plusDays(14);
            try (PreparedStatement ps = conn.prepareStatement(insertIssued)) {
                ps.setString(1, "Alice Example");
                ps.setString(2, "REG001");
                ps.setDouble(3, 1001.0);
                ps.setString(4, issueDate.format(formatter));
                ps.setString(5, dueDate.format(formatter));
                ps.setInt(6, 0); // not returned
                ps.executeUpdate();
            }

            conn.commit();
            System.out.println("library.db created and seeded.");
        } catch (SQLException e) {
            System.err.println("Failed to create or seed library.db: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Optional: allow running the migration directly
    public static void main(String[] args) {
        ensureDatabase();
    }
}
