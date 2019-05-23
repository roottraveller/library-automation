package domain.book;

public abstract class Book {
    private long bookId;
    private String title;
    private String author;
    private long isbnNo;
    private double price;
    private long count;

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getIsbnNo() {
        return isbnNo;
    }

    public void setIsbnNo(long isbnNo) {
        this.isbnNo = isbnNo;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
