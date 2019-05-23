package exceptions;

public class BookIssuedMaxLimitExceededException extends Exception {

    public BookIssuedMaxLimitExceededException(String s) {
        super(s);
    }
}
