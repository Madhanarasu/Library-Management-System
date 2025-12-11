

public class Book {


    private Double id;
    private String Title;
    private String Author;
    private int Edition ;
    private int Yop;
   
    Book(Double id, String Title, String Author, int Edition ,int Yop){
	    this.setId(id);
	    this.setTitle(Title);
	    this.setAuthor(Author);
	    this.setEdition(Edition) ;
	    this.setYop(Yop);
    }

	public Double getId() {
		return id;
	}
   public void setId(Double Id) {
	   this.id=Id;
   }
	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getAuthor() {
		return Author;
	}

	public void setAuthor(String author) {
		Author = author;
	}

	public int getEdition() {
		return Edition;
	}

	public void setEdition(int edition) {
		Edition = edition;
	}

	public int getYop() {
		return Yop;
	}

	public void setYop(int yop) {
		Yop = yop;
	}

	
 
    
}
