import java.time.LocalDate;

public class Student {
    private String stu_name;
    private String stu_reg_no;
    private Double book_id;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private boolean returned;

    // Matches constructor used in Library.java
    public Student(String stu_name, String stu_reg_no, Double book_id,
                   LocalDate issueDate, LocalDate dueDate, Boolean returned) {
        this.stu_name = stu_name;
        this.stu_reg_no = stu_reg_no;
        this.book_id = book_id;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returned = returned != null ? returned : false;
    }

    public String getStu_name() {
        return stu_name;
    }

    public String getStu_reg_no() {
        return stu_reg_no;
    }

    public Double getBook_id() {
        return book_id;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    // Matches the original Library.java method name (non-standard casing)
    public boolean Isreturned() {
        return returned;
    }

    // Matches the original Library.java setter name
    public void setreturned(boolean returned) {
        this.returned = returned;
    }

    @Override
    public String toString() {
        return "Student{stu_name='" + stu_name + '\'' +
               ", stu_reg_no='" + stu_reg_no + '\'' +
               ", book_id=" + book_id +
               ", issueDate=" + issueDate +
               ", dueDate=" + dueDate +
               ", returned=" + returned + '}';
    }
}
