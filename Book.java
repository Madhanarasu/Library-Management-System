public class Book {
    private Double id;
    private String title;
    private String author;
    private int edition;
    private int yop;

    public Book(Double id, String title, String author, int edition, int yop) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.edition = edition;
        this.yop = yop;
    }

    public Double getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getEdition() {
        return edition;
    }

    public int getYop() {
        return yop;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public void setYop(int yop) {
        this.yop = yop;
    }

    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + '\'' +
               ", author='" + author + '\'' + ", edition=" + edition +
               ", yop=" + yop + '}';
    }
}
