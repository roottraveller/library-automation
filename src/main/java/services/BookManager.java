package services;

import constants.Constants;
import domain.BookIssueDetails;
import domain.book.Book;
import domain.user.Student;
import exceptions.BookIssuedMaxLimitExceededException;
import exceptions.BookNotFoundException;
import exceptions.BookNotIssuedException;
import exceptions.StudentIsBlackListedException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class BookManager {
    private List<Book> bookList;
    private List<Student> studentList;
    private Map<Long, List<BookIssueDetails>> bookIssuedMap;

    public BookManager() {
        this.bookList = new ArrayList<>();
        this.studentList = new ArrayList<>();
        this.bookIssuedMap = new HashMap<>();
    }

    public long addBook(Book book) {
        Constants.BOOK_ID_COUNTER += 1;
        book.setBookId(Constants.BOOK_ID_COUNTER);
        bookList.add(book);
        return book.getBookId();
    }

    public Book getBook(long bookId) {
        return bookList.stream()
                .filter(it -> it.getBookId() == bookId)
                .findAny()
                .orElse(null);
    }

    public long addStudent(Student student) {
        Constants.USER_ID_COUNTER += 1;
        Student student1 = studentList.stream()
                .filter(it -> it.getEmail().equalsIgnoreCase(student.getEmail()))
                .findAny()
                .orElse(null);
        if (student1 != null) return -1;
        student.setUserId(Constants.USER_ID_COUNTER);
        studentList.add(student);
        return student.getUserId();
    }

    public Student getStudent(long studentId) {
        return studentList.stream()
                .filter(it -> it.getUserId() == studentId)
                .findAny()
                .orElse(null);
    }

    public boolean addIssueBookRecord(long userId, BookIssueDetails bookIssueDetails) throws BookIssuedMaxLimitExceededException, StudentIsBlackListedException {
        if (isStudentBlackListed(userId)) {
            throw new StudentIsBlackListedException("current student is blacklisted");
        }
        List<BookIssueDetails> bookIssueDetailsList = bookIssuedMap.get(userId);
        if (bookIssueDetailsList == null) {
            List<BookIssueDetails> bookIssueDetailsListNew = new ArrayList<>();
            bookIssueDetailsListNew.add(bookIssueDetails);
            bookIssuedMap.put(userId, bookIssueDetailsListNew);
            return true;
        } else {
            BookIssueDetails bookIssueDetailsTemp = bookIssueDetailsList.stream()
                    .filter(it -> it.getBookId() == bookIssueDetails.getBookId())
                    .findAny()
                    .orElse(null);
            if (bookIssueDetailsTemp == null) {
                bookIssueDetailsList.add(bookIssueDetails);
                return true;
            } else {
                if (bookIssueDetailsTemp.getNoOfBookIssued() >= bookIssueDetailsTemp.getTotalBookAllow()) {
                    throw new BookIssuedMaxLimitExceededException("No of max book already allocated");
                } else {
                    bookIssueDetailsTemp.setNoOfBookIssued(bookIssueDetailsTemp.getNoOfBookIssued() + 1);
                    return true;
                }
            }
        }
    }

    public double returnBook(long bookId, long studentId) throws BookNotIssuedException, BookNotFoundException {
        List<BookIssueDetails> bookIssueDetailsList = bookIssuedMap.get(studentId);
        if (bookIssueDetailsList == null) {
            throw new BookNotIssuedException("No book issued to this user");
        } else {
            BookIssueDetails bookIssueDetails = bookIssueDetailsList.stream()
                    .filter(it -> it.getBookId() == bookId)
                    .findAny()
                    .orElse(null);
            if (bookIssueDetails == null) {
                throw new BookNotFoundException("No book found for this user");
            } else {
                // Yaaaaaeee, Calculate fine now
                Date today = new Date();
                bookIssueDetails.setReturnDate(today);
                return calculateFine(bookIssueDetails.getIssueDate(), bookIssueDetails.getReturnDate(), studentId);
            }
        }
    }

    private boolean isStudentBlackListed(long userId) {
        Student student = studentList.stream()
                .filter(it -> it.getUserId() == userId)
                .findAny()
                .orElse(null);

        return student != null && student.isBlackListed();
    }

    public Book searchBook(String bookTitle) {
        String titleRegEx = "(.*)" + bookTitle + "(.*)";
        Book book = bookList.stream()
                .filter(it -> it.getTitle().matches(titleRegEx))
                .findAny()
                .orElse(null);
        return book;
    }

    /********************/
    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public Map<Long, List<BookIssueDetails>> getBookIssuedMap() {
        return bookIssuedMap;
    }

    public void setBookIssuedMap(Map<Long, List<BookIssueDetails>> bookIssuedMap) {
        this.bookIssuedMap = bookIssuedMap;
    }

    private double calculateFine(Date issueDate, Date returnDate, long studentId) {
        long diff = returnDate.getTime() - issueDate.getTime();
        long noOfDaysElapsed = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        double totalFine = 0.0;
        if (noOfDaysElapsed <= 14) {
            return totalFine;
        } else {
            noOfDaysElapsed -= 14;
            totalFine += Math.min(noOfDaysElapsed, 3) * 1;
            noOfDaysElapsed -= Math.min(noOfDaysElapsed, 3);
            if (noOfDaysElapsed > 0) {
                totalFine += Math.min(noOfDaysElapsed, 3) * 2;
                noOfDaysElapsed -= Math.min(noOfDaysElapsed, 3);
            }
            if (noOfDaysElapsed > 0) {
                totalFine += Math.min(noOfDaysElapsed, 30) * 3;
                noOfDaysElapsed -= Math.min(noOfDaysElapsed, 30);
            }

            if (noOfDaysElapsed > 0) {
                totalFine += noOfDaysElapsed * 3;
                // BlackList Student
                Student student = studentList.stream()
                        .filter(it -> it.getUserId() == studentId)
                        .findAny()
                        .orElse(null);
                if (student != null) {
                    student.setBlackListed(true);
                }
            }
        }
        return 0.0;
    }
}
