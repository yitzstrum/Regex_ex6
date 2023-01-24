package oop.ex6.parser;

public class BadLineException extends Exception{
    private static final String ERR_MSG = "ERROR: The following line has a syntax error: ";

    private final String line;

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
        return ERR_MSG + line;
    }


}
