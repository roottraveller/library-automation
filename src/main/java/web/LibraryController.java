package web;

import domain.book.Book;
import domain.user.Student;
import exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import services.BookIssueService;

@Controller
public class LibraryController {
    @Autowired
    private BookIssueService bookIssueService;

    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public long registerBook(@RequestBody Book book) {
        return bookIssueService.registerBook(book);
    }

    @RequestMapping(value = "/student", method = RequestMethod.POST)
    public long registerStudent(@RequestBody Student student) {
        return bookIssueService.registerStudent(student);
    }

    @RequestMapping(value = "/borrow", method = RequestMethod.GET)
    public boolean issueBook(@RequestParam long bookId,
                             @RequestParam long studentId) throws BookIssuedMaxLimitExceededException, StudentNotFoundException, StudentIsBlackListedException, BookNotFoundException {
        return bookIssueService.issueBook(bookId, studentId);
    }

    @RequestMapping(value = "/return", method = RequestMethod.GET)
    public double returnBook(@RequestParam long bookId,
                             @RequestParam long studentId) throws BookNotIssuedException, BookNotFoundException, StudentNotFoundException {
        return bookIssueService.returnBook(bookId, studentId);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Book searchBook(@RequestParam String bookTitle) {
        return bookIssueService.searchBook(bookTitle);
    }


}
