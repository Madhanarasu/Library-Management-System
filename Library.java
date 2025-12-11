import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class Library {
static Scanner Scan = new Scanner(System.in);

Map<Double,Book> Books = new LinkedHashMap<Double,Book>(); //For storing  the books only
ArrayList<Student> students = new ArrayList<Student>();// To store the student details whom the book with id is issued
DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("dd-MM-yyyy"); 
BufferedReader read = new BufferedReader(new FileReader("issuedBooks.txt"));  
Library() throws IOException{ //
String stu ; 
while((stu = read.readLine())!= null) { //Read and stores the contents of the File into the ArrayList 

	String stua[]=stu.split("\\|");
	String Sname = stua[0];
	String Sreg = stua[1];	
    Double Bid = Double.parseDouble(stua[2]);
    LocalDate isd = LocalDate.parse(stua[3], formatter);
	LocalDate due = LocalDate.parse(stua[4], formatter);
	Boolean ir = Boolean.parseBoolean(stua[5]);
	Student stuc = new Student(Sname,Sreg,Bid,isd,due,ir);
	students.add(stuc);
	read.close();
	
}
}
	public  void addBook() {//Write the datails to a File and LinkedHashMap
		try(BufferedWriter write = new BufferedWriter(new FileWriter("Books.txt",true));) {
		
		System.out.println("Enter No of Books to be added:");
		int nob = Scan.nextInt();
		
		for(int i=1;i<=nob;i++) {
		System.out.println("Enter book id:");
		Double id  = Scan.nextDouble();
		System.out.println("Enter book Name:");
		String name = Scan.next();
		System.out.println("Enter book's author Name:");
		String author = Scan.next();
		System.out.println("Enter book's Edition:");
		int edition = Scan.nextInt();
		System.out.println("Enter Year of Publication:");
		int yop = Scan.nextInt();
		Book book = new Book(id,name,author,edition,yop );
		Books.put(id, book);	
     	write.write(id+"|"+name+"|"+author+"|"+edition+"|"+yop+"|");//writing the Contents  with "|" delimiter(to spilt)
		  write.newLine();
		  write.flush();
		
		}
		}
		catch(FileNotFoundException e) {
			System.out.println("The Books File is Missing");			
		} catch (IOException e) {
			System.out.println("Enter Valid  details ");
			
		}
	
				
	}
	
public void listbooks()  { // Reads the Contents from the Books file and Store it on HashMap
	try(BufferedReader read = new BufferedReader(new FileReader("Books.txt")); ){
		String line ;
		System.out.println("1-Display All Books");
		System.out.println("2-Get a Book");
		int ch = Scan.nextInt();
		Boolean hasBooks =  false;
		 while((line = read.readLine())!= null) {
		 hasBooks= true;
		String lines[]=line.split("\\|");
		Double id = Double.parseDouble(lines[0].trim());
		
		String name = lines[1];
		String author = lines[2];
		int edition = Integer.parseInt(lines[3]);
		int yop = Integer.parseInt(lines[4]);
		Book bok = new Book(id,name,author,edition,yop);
		Books.put(id, bok);
		if(ch==1) {
//		Books.forEach(null);
			for(Book bokk : Books.values()) {
		System.out.print("\n Book Id :"+bokk.getId());
		System.out.print("\n Book Name :"+bokk.getTitle());
		System.out.print("\n Author Name :"+bokk.getAuthor());
		System.out.print("\n Edition :"+bokk.getEdition());
		System.out.print("\n Year of Publishing :"+bokk.getYop()+"\n");
			}}
		else    {
		 System.out.print("\n Enter the Book Id:");
		 double i = Scan.nextDouble();
		 if(Books.containsKey(i)) {
		 Book book = Books.get(i);
		 System.out.print("\n Book Id :"+book.getId());
		 System.out.print("\n Book Name :"+book.getTitle());
		 System.out.print("\n Author Name :"+book.getAuthor());
		 System.out.print("\n Edition :"+book.getEdition());
		 System.out.print("\n Year of Publishing :"+book.getYop()+"\n");
			return;              }                
		         }
		
		 if(!hasBooks) {
			 System.out.println("There is No book stored");
		   return;
		               }
		     }
	 }
	catch (FileNotFoundException e) {
		
		System.out.println("The Books file is missing");
	} catch (NumberFormatException e) {
		
		System.out.println("Error while Parsing Daata from the File");
	} catch (IOException e) {
		System.out.println("Error while prasing the Data from file ");
		
	}  
		}
	

public void issueBook()  { //Issue Book(using id) to a student(student Class) and Store it in file and arrayList
	try(BufferedWriter write = new BufferedWriter(new FileWriter("issuedBooks.txt",true));){	
	System.out.println("Enter how many books to be Issued:");
	   int c = Scan.nextInt();
	   for(int y=1;y<=c;y++) {	   
		System.out.println("Enter Student Name :");
		String Stu_name =Scan.next();
		System.out.println("Enter Student Reg_no :");
		String Reg_no = Scan.next();		
		System.out.println("Enter the Book id :");
	    Double Book_id =Scan.nextDouble();
	    Scan.nextLine();	    
		System.out.println("Enter issued date(Day-Month-Year) :");
		String Is_date = Scan.nextLine().trim();
		LocalDate isd = LocalDate.parse((Is_date.trim()),formatter);
		System.out.println("Due date(Day-Month-Year) :");
		String due = Scan.nextLine().trim();
		LocalDate du = LocalDate.parse(due.trim(),formatter);
		Boolean Returnst = false;
		write.write(Stu_name+"|"+Reg_no+"|"+Book_id+"|"+isd+"|"+due+"|"+Returnst+"|");
		write.newLine();
		write.flush();
		Student stu = new Student(Stu_name,Reg_no,Book_id,isd ,du,Returnst);
		students.add(stu);
	   }} catch (FileNotFoundException e) {
		
		System.out.println("IssuedBooks File is missing");
	}
	catch(IOException e) {
		System.out.println("Enter Valid Data");
	}
	
		
	}
public void Duedate()
{try { // By accessing the arraylist, returns the book that matches date entered by the user with due date stored in the arraylist
  System.out.println("Enter Date :");
  String date = Scan.next();
  int total =students.size();
   for (int i=0;i<total;i++){
       String due =(students.get(i).getDueDate().format(formatter)).toString(); 
       if (date.equals(due)&&students.get(i).Isreturned()==false){
    	   System.out.println("Student Name:"+students.get(i).getStu_name()+"\n"+"Student Reg_no:"+
       students.get(i).getStu_reg_no()+"\n"+"Book ID:"+ students.get(i).getBook_id());
       }
       else {
    	   continue;
       }
   }}
catch(NumberFormatException  e) {
	System.out.println("Enter the date in Correct Format ");
}
	
}
public void returnbook() {//after entering the returned book id, it will change the Boolean Flag(Indicates if a Book is retunred or not in the student class) 
//                            of the student object to true
    System.out.println("Enter the book ID:");
    double id = Scan.nextDouble();
    boolean found = false;

    for (int l = 0; l < students.size(); l++) {
        if (id == students.get(l).getBook_id()) {
             students.get(l).setreturned(true);
            found = true;

            System.out.println("Book with ID " + id + " has been returned successfully!");
            try(BufferedReader read = new BufferedReader(new FileReader("issuedBook.txt"));	
            		BufferedWriter write = new BufferedWriter(new FileWriter("issuedBooks.txt",true));){
            	String line ;
            	ArrayList<String> temp = new ArrayList<>();
            	while(( line =read.readLine()) != null ) {
    	         String[] lines = line.split("\\|");
    	         Double i =Double.parseDouble(lines[2]) ;
    	         if(i == id) {
    	        	 lines[5] ="true";
    	        	 
    	         }
    	      temp.add(String.join("|", lines));
    	      for (String record : temp) {
                  write.write(record);
              }
            	}
    } catch (IOException e) {
	 
		e.printStackTrace();
	}break;
        }
    }

    if (!found) {
        System.out.println("No issued book found with ID: " + id);
    }
    
    
}



	public static void main(String[] args) throws IOException {
	Library li = new Library();
	int choice ;
	do {
	System.out.println("1.Add a Book");//completed
	System.out.println("2.Issue  Books");//completed
	System.out.println("3.Due Dates");//completed
	System.out.println("4.Return");//completed
	System.out.println("5.Books List");//completed
	System.out.println("6.Exit");//completed
	System.out.println("Enter:");
	choice = Scan.nextInt();
	switch(choice) {
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
	}while(choice!=6);
	Scan.close();
}
}