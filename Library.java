import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Library management using SQLite (library.db).
 * Requires sqlite-jdbc driver on the classpath (e.g. org.xerial:sqlite-jdbc).
 *
 * This class assumes Book and Student classes exist in the project with the same
 * constructors and getters used previously:
 *  - Book(Double id, String name, String author, int edition, int yop)
 *  - Student(String name, String reg, Double bookId, LocalDate issueDate, LocalDate dueDate, Boolean returned)
 */
public class Library {
    static Scanner Scan = new Scanner(System.in);

    Map<Double, Book> Books = new LinkedHashMap<>(); // for caching books
    ArrayList<Student> students = new ArrayList<>(); // issued book records
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private Connection conn;

    public Library() throws SQLException {
        // Connect to SQLite DB (creates file if not exists)
        conn = DriverManager.getConnection("jdbc:sqlite:library.db");
        createTablesIfNotExist();
        loadBooksIntoCache();
        loadIssuedBooks();
    }

    private void createTablesIfNotExist() throws SQLException {
        String createBooks = "CREATE TABLE IF NOT EXISTS books ("
                           + "id REAL PRIMARY KEY,"
                           + "name TEXT NOT NULL,"
                           + "author TEXT,"
                           + "edition INTEGER,"
                           + "yop INTEGER"
                           + ");";

        String createIssued = "CREATE TABLE IF NOT EXISTS issued_books ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "stu_name TEXT NOT NULL,"
                            + "stu_reg TEXT NOT NULL,"
                            + "book_id REAL NOT NULL,"
                            + "issue_date TEXT,"
                            + "due_date TEXT,"
                            + "returned INTEGER DEFAULT 0,"
                            + "FOREIGN KEY(book_id) REFERENCES books(id)"
                            + ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createBooks);
            stmt.execute(createIssued);
        }
    }

    private void loadBooksIntoCache() {
        String sql = "SELECT id, name, author, edition, yop FROM books";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Double id = rs.getDouble("id");
                String name = rs.getString("name");
                String author = rs.getString("author");
                int edition = rs.getInt("edition");
                int yop = rs.getInt("yop");
                Book b = new Book(id, name, author, edition, yop);
                Books.put(id, b);
            }
        } catch (SQLException e) {
            // ignore here
        }
    }

    private void loadIssuedBooks() {
        String sql = "SELECT stu_name, stu_reg, book_id, issue_date, due_date, returned FROM issued_books";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String Sname = rs.getString("stu_name");
                String Sreg = rs.getString("stu_reg");
                Double Bid = rs.getDouble("book_id");
                String isdStr = rs.getString("issue_date");
                String dueStr = rs.getString("due_date");
                boolean ir = rs.getInt("returned") != 0;
                LocalDate isd = LocalDate.parse(isdStr, formatter);
                LocalDate due = LocalDate.parse(dueStr, formatter);
                Student stuc = new Student(Sname, Sreg, Bid, isd, due, ir);
                students.add(stuc);
            }
        } catch (SQLException e) {
            System.out.println("Error loading issued books: " + e.getMessage());
        }
    }

    public void addBook() { // write to DB and cache
        System.out.println("Enter No of Books to be added:");
        int nob = Scan.nextInt();
        for (int i = 1; i <= nob; i++) {
            try {
                System.out.println("Enter book id:");
                Double id = Scan.nextDouble();
                System.out.println("Enter book Name:");
                String name = Scan.next();
                System.out.println("Enter book's author Name:");
                String author = Scan.next();
                System.out.println("Enter book's Edition:");
                int edition = Scan.nextInt();
                System.out.println("Enter Year of Publication:");
                int yop = Scan.nextInt();

                String sql = "INSERT OR REPLACE INTO books (id, name, author, edition, yop) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setDouble(1, id);
                    ps.setString(2, name);
                    ps.setString(3, author);
                    ps.setInt(4, edition);
                    ps.setInt(5, yop);
                    ps.executeUpdate();
                }

                Book book = new Book(id, name, author, edition, yop);
                Books.put(id, book);
                System.out.println("Book added: " + name);
            } catch (SQLException e) {
                System.out.println("Database error while adding book: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Enter valid details.");
            }
        }
    }

    public void listbooks() { // reads from DB (and cache)
        System.out.println("1-Display All Books");
        System.out.println("2-Get a Book");
        int ch = Scan.nextInt();

        if (ch == 1) {
            String sql = "SELECT id, name, author, edition, yop FROM books";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                boolean hasBooks = false;
                while (rs.next()) {
                    hasBooks = true;
                    System.out.print("\n Book Id :" + rs.getDouble("id"));
                    System.out.print("\n Book Name :" + rs.getString("name"));
                    System.out.print("\n Author Name :" + rs.getString("author"));
                    System.out.print("\n Edition :" + rs.getInt("edition"));
                    System.out.print("\n Year of Publishing :" + rs.getInt("yop") + "\n");
                }
                if (!hasBooks) {
                    System.out.println("There is No book stored");
                }
            } catch (SQLException e) {
                System.out.println("Error while reading books: " + e.getMessage());
            }
        } else {
            System.out.print("\n Enter the Book Id:");
            double i = Scan.nextDouble();
            String sql = "SELECT id, name, author, edition, yop FROM books WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDouble(1, i);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.print("\n Book Id :" + rs.getDouble("id"));
                        System.out.print("\n Book Name :" + rs.getString("name"));
                        System.out.print("\n Author Name :" + rs.getString("author"));
                        System.out.print("\n Edition :" + rs.getInt("edition"));
                        System.out.print("\n Year of Publishing :" + rs.getInt("yop") + "\n");
                    } else {
                        System.out.println("Book not found with id: " + i);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error while fetching the book: " + e.getMessage());
            }
        }
    }

    public void issueBook() { // insert into issued_books and in-memory list
        System.out.println("Enter how many books to be Issued:");
        int c = Scan.nextInt();
        for (int y = 1; y <= c; y++) {
            try {
                System.out.println("Enter Student Name :");
                String Stu_name = Scan.next();
                System.out.println("Enter Student Reg_no :");
                String Reg_no = Scan.next();
                System.out.println("Enter the Book id :");
                Double Book_id = Scan.nextDouble();
                Scan.nextLine();
                System.out.println("Enter issued date(Day-Month-Year) :");
                String Is_date = Scan.nextLine().trim();
                LocalDate isd = LocalDate.parse(Is_date.trim(), formatter);
                System.out.println("Due date(Day-Month-Year) :");
                String due = Scan.nextLine().trim();
                LocalDate du = LocalDate.parse(due.trim(), formatter);
                Boolean Returnst = false;

                String sql = "INSERT INTO issued_books (stu_name, stu_reg, book_id, issue_date, due_date, returned) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, Stu_name);
                    ps.setString(2, Reg_no);
                    ps.setDouble(3, Book_id);
                    ps.setString(4, isd.format(formatter));
                    ps.setString(5, du.format(formatter));
                    ps.setInt(6, Returnst ? 1 : 0);
                    ps.executeUpdate();
                }

                Student stu = new Student(Stu_name, Reg_no, Book_id, isd, du, Returnst);
                students.add(stu);
                System.out.println("Issued book " + Book_id + " to " + Stu_name);
            } catch (SQLException e) {
                System.out.println("Database error while issuing book: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Enter valid data.");
            }
        }
    }

    public void Duedate() { // find by due date in students list
        try {
            System.out.println("Enter Date :");
            String date = Scan.next();
            int total = students.size();
            for (int i = 0; i < total; i++) {
                String due = (students.get(i).getDueDate().format(formatter)).toString();
                if (date.equals(due) && students.get(i).Isreturned() == false) {
                    System.out.println("Student Name:" + students.get(i).getStu_name() + "\n" + "Student Reg_no:"
                            + students.get(i).getStu_reg_no() + "\n" + "Book ID:" + students.get(i).getBook_id());
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            System.out.println("Enter the date in Correct Format ");
        }
    }

    public void returnbook() { // mark returned=true both in-memory and in DB
        System.out.println("Enter the book ID:");
        double id = Scan.nextDouble();
        boolean found = false;

        for (int l = 0; l < students.size(); l++) {
            if (id == students.get(l).getBook_id()) {
                students.get(l).setreturned(true);
                found = true;

                System.out.println("Book with ID " + id + " has been returned successfully!");
                String sql = "UPDATE issued_books SET returned = 1 WHERE book_id = ? AND returned = 0";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setDouble(1, id);
                    int updated = ps.executeUpdate();
                    if (updated == 0) {
                        System.out.println("No matching issued record updated (it may already be returned).");
                    }
                } catch (SQLException e) {
                    System.out.println("Error updating return status: " + e.getMessage());
                }
                break;
            }
        }

        if (!found) {
            System.out.println("No issued book found with ID: " + id);
        }
    }

    private void closeConnection() {
        try {
            if (conn != null && !conn.isClosed())
                conn.close();
        } catch (SQLException e) {
            // ignore
        }
    }

    public static void main(String[] args) {
        Library li = null;
        try {
            li = new Library();
        } catch (SQLException e) {
            System.out.println("Failed to initialize library database: " + e.getMessage());
            return;
        }

        int choice;
        do {
            System.out.println("1.Add a Book");// completed
            System.out.println("2.Issue  Books");// completed
            System.out.println("3.Due Dates");// completed
            System.out.println("4.Return");// completed
            System.out.println("5.Books List");// completed
            System.out.println("6.Exit");// completed
            System.out.println("Enter:");
            choice = Scan.nextInt();
            switch (choice) {
                case 1:
                    li.addBook();
                    break;
                case 2:
                    li.issueBook();
                    break;
                case 3:
                    li.Duedate();
                    break;
                case 4:
                    li.returnbook();
                    break;
                case 5:
                    li.listbooks();
                    break;
            }
        } while (choice != 6);

        li.closeConnection();
        Scan.close();
    }
}
