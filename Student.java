import java.time.LocalDate;

public class Student {
  private String Stu_reg_no;
  private String Stu_name;
  private Double Book_id;
  private LocalDate Issuedate;
  private LocalDate DueDate;
  private Boolean isreturned= false ;
  
  Student(String Stu_name,String Stu_reg_no,Double Book_id,LocalDate Issuedate,LocalDate DueDate,Boolean isreturned ){
	  this.setBook_id(Book_id);
	  this.setDueDate(DueDate);
	  this.setIssuedate(Issuedate);
	  this.setStu_name(Stu_name);
	  this.setStu_reg_no(Stu_reg_no);
	  this.setreturned(isreturned);
  }
public String getStu_reg_no() {
	return Stu_reg_no;
}
public void setStu_reg_no(String stu_reg_no) {
	Stu_reg_no = stu_reg_no;
}
public String getStu_name() {
	return Stu_name;
}
public void setStu_name(String stu_name) {
	Stu_name = stu_name;
}
public Double getBook_id() {
	return Book_id;
}
public void setBook_id(Double book_id) {
	Book_id = book_id;
}
public LocalDate getIssuedate() {
	return Issuedate;
}
public void setIssuedate(LocalDate issuedate) {
	Issuedate = issuedate;
}
public LocalDate getDueDate() {
	return DueDate;
}
public void setDueDate(LocalDate dueDate) {
	DueDate = dueDate;
}

public Boolean Isreturned() {
	return isreturned;
}
public void setreturned(Boolean isreturned) {
	this.isreturned = isreturned;
}
  
  
  
}
