package services;

import domain.BookIssueDetails;
import exceptions.*;
import domain.book.Book;
import domain.user.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BookIssueService {
    @Autowired
    private BookManager bookManager;

    public long registerBook(Book book) {
        return bookManager.addBook(book);
    }

    public long registerStudent(Student student) {
        return bookManager.addStudent(student);
    }

    public boolean issueBook(long bookId, long studentId) throws StudentNotFoundException, BookNotFoundException, BookIssuedMaxLimitExceededException, StudentIsBlackListedException {
        validateBookAndOrUser(bookId, studentId);

        BookIssueDetails bookIssueDetails = new BookIssueDetails();
        bookIssueDetails.setBookId(bookId);
        bookIssueDetails.setNoOfBookIssued(1);
        bookIssueDetails.setIssueDate(new Date());
        return bookManager.addIssueBookRecord(studentId, bookIssueDetails);
    }

    public double returnBook(long bookId, long studentId) throws BookNotFoundException, StudentNotFoundException, BookNotIssuedException {
        validateBookAndOrUser(bookId, studentId);

        return bookManager.returnBook(bookId, studentId);
    }

    private boolean validateBookAndOrUser(long bookId, long studentId) throws StudentNotFoundException, BookNotFoundException {
        Student student = bookManager.getStudent(studentId);
        if (student == null) {
            throw new StudentNotFoundException("Student not exist");
        }
        Book book = bookManager.getBook(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book does not exist");
        }
        return true;
    }

    public Book searchBook(String bookTitle){
        return bookManager.searchBook(bookTitle);
    }
}
