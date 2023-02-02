package oop.ex6.parser;

public abstract class Parser {
    protected static final String SPACE_REGEX = "\\s*";
    protected static final int METHOD_NAME_START_IND = 4;
    protected static final String COMMA = ",";
    protected static final String VARIABLE_NAME_REGEX = "(_+[\\w]|[a-zA-Z])[\\w]*";
    protected static final String VOID_REGEX = "void";
    protected static final String METHOD_NAME_REGEX = "([a-zA-Z]+[\\w]*)";
    protected static final String OPEN_PAREN_REGEX = "\\(";
    protected static final String CLOSE_PAREN_REGEX = "\\)";
    protected static final String OPEN_BRACKET_REGEX = "\\{";
    protected static final String TYPE_REGEX = "\\s*(int|double|String|boolean|char)\\s*";
    protected static final String SPACE_PLUS = "\\s+";
    protected static final String FINAL_REGEX = "(final\\s+)?";
    protected static final String TYPE_AND_NAME_REGEX =
            SPACE_REGEX + FINAL_REGEX + SPACE_REGEX +
                    TYPE_REGEX + SPACE_PLUS + VARIABLE_NAME_REGEX + SPACE_REGEX;

    protected static final String METHOD_DEC_REGEX = "^" +
            SPACE_REGEX +
            VOID_REGEX +
            SPACE_REGEX +
            METHOD_NAME_REGEX +
            SPACE_REGEX +
            OPEN_PAREN_REGEX + SPACE_REGEX + "(" +
            SPACE_REGEX +
            TYPE_AND_NAME_REGEX + "(" + COMMA + TYPE_AND_NAME_REGEX + ")*" +
            SPACE_REGEX + ")?" +
            CLOSE_PAREN_REGEX +
            SPACE_REGEX +
            OPEN_BRACKET_REGEX +
            SPACE_REGEX +
            "$";

    protected static final String PARAMS_REGEX = "(" +
            SPACE_REGEX +
            TYPE_AND_NAME_REGEX + "(" + "," + TYPE_AND_NAME_REGEX + ")*" +
            SPACE_REGEX + ")";
}
