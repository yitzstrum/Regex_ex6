package oop.ex6.exceptions;

public class BadLogicException extends Exception{
    private static final String ERR_MSG = "ERROR: ";

    private final String line;

    public BadLogicException(String line){
        this.line = line;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return an error message with the line which caused the exception
     */
    @Override
    public String getMessage() {
        return ERR_MSG + line;
    }


}