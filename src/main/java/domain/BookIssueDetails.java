package domain;

import constants.Constants;

import java.util.Date;

public class BookIssueDetails {
    private long bookId;
    private long noOfBookIssued;
    private Date issueDate;
    private Date returnDate;
    private long totalBookAllow;

    public BookIssueDetails() {
        this.totalBookAllow = Constants.MAX_BOOK_ALLOW;
        this.noOfBookIssued = 0;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getNoOfBookIssued() {
        return noOfBookIssued;
    }

    public void setNoOfBookIssued(long noOfBookIssued) {
        this.noOfBookIssued = noOfBookIssued;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public long getTotalBookAllow() {
        return totalBookAllow;
    }

    public void setTotalBookAllow(long totalBookAllow) {
        this.totalBookAllow = totalBookAllow;
    }
}
