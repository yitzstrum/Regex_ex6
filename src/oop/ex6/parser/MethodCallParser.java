package oop.ex6.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodCallParser {

    private Token token;

    private static final String SPACE_REGEX = "\\s*";
    private static final String METHOD_NAME_REGEX = "([a-zA-Z]+[\\w]*)";
    private static final String OPEN_PAREN_REGEX = "\\(";
    private static final String CLOSE_PAREN_REGEX = "\\)";
    private static final String SEMI_COLLEN = ";";
    private static final String VARIABLE_NAME_REGEX = "(_+[\\w]|[a-zA-Z])[\\w]*";
    private static final String COMMA = ",";

    private static final String METHOD_CALL_REGEX = "^" +
            SPACE_REGEX +
            METHOD_NAME_REGEX +
            SPACE_REGEX +
            OPEN_PAREN_REGEX +
            SPACE_REGEX;

    public static final Pattern METHOD_CALL_PATTERN = Pattern.compile(METHOD_CALL_REGEX);


    public MethodCallParser(Token token) throws BadLineException {
        this.token = token;
        checkMethodCallSyntax();
    }

    private void checkMethodCallSyntax() throws BadLineException {
        Matcher methodDecMatcher = METHOD_CALL_PATTERN.matcher(token.getContent());
        if (!methodDecMatcher.matches()){
            throw new BadLineException(token.getContent());
        }
    }
}
