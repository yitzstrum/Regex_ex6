package oop.ex6.exceptions;

/**
 * BadLineException class, inherits from Exception and is thrown when there is a syntax error
 */
public class BadLineException extends Exception{
    private static final String ERR_MSG = "ERROR: The following line has a syntax error: ";

    private final String line;

    /**
     * Class constructor
     * @param line the line which has the syntax error
     */
    public BadLineException(String line){
        this.line = line;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return an error message with the line which caused the exception
     */
    @Override
    public String getMessage() {
        return ERR_MSG + "'" + line + "'";
    }


}
