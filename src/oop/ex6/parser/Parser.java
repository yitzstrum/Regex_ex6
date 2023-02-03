package oop.ex6.parser;

import java.util.regex.Pattern;

public abstract class Parser {
    protected static final int METHOD_NAME_START_IND = 4;
    protected static final String SPACE_REGEX = "\\s*";
    protected static final String MANDATORY_SPACE_REGEX = "\\s+";
    protected static final String END_LINE_REGEX = "$";
    protected static final String START_LINE_REGEX = "^";
    protected static final String ANY_CHAR_REGEX = ".*";
    protected static final String COMMA = ",";
    protected static final String SEMI_COLLEEN = ";";
    protected static final String VARIABLE_NAME_REGEX = "(_+[\\w]|[a-zA-Z])[\\w]*";
    protected static final String VOID_REGEX = "void";
    protected static final String METHOD_NAME_REGEX = "([a-zA-Z]+[\\w]*)";
    protected static final String OPEN_PAREN_REGEX = "\\(";
    protected static final String CLOSE_PAREN_REGEX = "\\)";
    protected static final String OPEN_BRACKET_REGEX = "\\{";
    protected static final String TYPE_REGEX = "\\s*(int|double|String|boolean|char)\\s*";
    protected static final String SPACE_PLUS = "\\s+";
    protected static final String FINAL_REGEX = "(final\\s+)?";
    protected static final String EQUAL_REGEX = SPACE_REGEX + "=" + SPACE_REGEX + "\\w+" + SPACE_REGEX;
    protected static final String SEMI_COLON_REGEX = SPACE_REGEX + ";" + SPACE_REGEX + END_LINE_REGEX;

    protected static final String FINAL_TYPE_REGEX = "^" +
            SPACE_REGEX +
            FINAL_REGEX +
            TYPE_REGEX +
            MANDATORY_SPACE_REGEX +
            VARIABLE_NAME_REGEX +
            SPACE_REGEX;

    protected static final String MAYBE_EQUAL_REGEX = "(" + EQUAL_REGEX + ")?";

    protected static final String VARIABLE_DEC =
            FINAL_TYPE_REGEX + MAYBE_EQUAL_REGEX +
                    '(' +
                    SPACE_REGEX + COMMA + SPACE_REGEX +
                    VARIABLE_NAME_REGEX + SPACE_REGEX + MAYBE_EQUAL_REGEX + SPACE_REGEX +
                    ")*" + SEMI_COLON_REGEX;

    protected static final String VARIABLE_ASSIGMENT_REGEX =
            VARIABLE_NAME_REGEX + "(" + EQUAL_REGEX + ")?";


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

    protected static final String METHOD_CALL_REGEX = "^" +
            SPACE_REGEX +
            METHOD_NAME_REGEX +
            SPACE_REGEX +
            OPEN_PAREN_REGEX +
            "(" +
            ANY_CHAR_REGEX +
            ")" +
            CLOSE_PAREN_REGEX +
            SEMI_COLLEEN;

    protected static final Pattern METHOD_NAME_PATTERN = Pattern.compile(METHOD_NAME_REGEX);
}
