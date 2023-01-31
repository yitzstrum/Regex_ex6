package oop.ex6.parser;

public class BadParamException extends SJavaException {

    private static final String ERR_MSG = "ERROR: The following line has a syntax error: ";

    public BadParamException(String line) {
        super(line);
    }
}
