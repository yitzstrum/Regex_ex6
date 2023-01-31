package oop.ex6.parser;

public class SJavaException extends Exception{
    private static final String ERR_MSG = "ERROR: The following line has a syntax error: ";

    private final String line;

    public SJavaException(String line){
        this.line = line;
    }

    public String getMessage() {
        return ERR_MSG + line;
    }

}
