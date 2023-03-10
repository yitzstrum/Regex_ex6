package oop.ex6.parser;

import java.util.regex.Pattern;

/**
 * Parser class, contains all the Parser's regex's
 */
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
    protected static final String UNASSIGNED_VARIABLE_REGEX = "^\\s*[a-zA-Z]\\w*\\s*";
    protected static final String NUMBER_REGEX = "(-?\\d+(\\.\\d+)?)";

    protected static final String STRING_REGEX = SPACE_REGEX + "\"[^\"].*\"" + SPACE_REGEX;
    protected static final String EQUAL_RHS_REGEX =
            SPACE_REGEX + "((" + STRING_REGEX + ")|([^=;,\\s{}]+|))"  + SPACE_REGEX;
    protected static final String ASSIGNED_VARIABLE_REGEX = "^\\s*[a-zA-Z]\\w*\\s*=" + EQUAL_RHS_REGEX;
    protected static final String OPEN_PAREN_REGEX = "\\(";
    protected static final String CLOSE_PAREN_REGEX = "\\)";
    protected static final String OPEN_BRACKET_REGEX = "\\{";
    protected static final String TYPE_REGEX = "\\s*(int|double|String|boolean|char)\\s*";
    protected static final String SPACE_PLUS = "\\s+";
    protected static final String EQUALS = "=";
    protected static final String EMPTY_STRING = "";
    protected static final String FINAL_REGEX = "(final\\s+)?";
    protected static final String CHAR_REGEX = "'[^'].*'";


    protected static final String EQUAL_REGEX = SPACE_REGEX + EQUALS + SPACE_REGEX + EQUAL_RHS_REGEX + SPACE_REGEX;
    protected static final String SEMI_COLON_REGEX = SPACE_REGEX + ";" + SPACE_REGEX + END_LINE_REGEX;
    protected static final String INT_TYPE = "int";
    protected static final String DOUBLE_TYPE = "double";
    protected static final String STRING_TYPE = "String";
    protected static final String BOOLEAN_TYPE = "boolean";
    protected static final String CHAR_TYPE = "char";
    protected final String FINAL_PREFIX_REGEX = "^\\s*final\\s*";


    protected static final String RESERVED_WORDS = "(int|double|String|boolean|char|" +
            "void|final|if|while|true|false|return)";

    protected static final String VALUES_REGEX = "(" + VARIABLE_NAME_REGEX + "|" +
            NUMBER_REGEX + "|" + CHAR_REGEX + "|" + STRING_REGEX + "|true|false)";

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


    private static final String IF_WHILE_DEC_REGEX = "(while|if)";
    private static final String SUPPORTED_IF_WHILE_OPERATORS = "(&&|\\|\\|)";

    private static final String UN_SUPPORTED_VALUE_REGEX = "[^&| )(]+";


    protected static final String IF_WHILE_LINE_REGEX =
            SPACE_REGEX +
                    IF_WHILE_DEC_REGEX +
                    SPACE_REGEX +
                    OPEN_PAREN_REGEX +
                    SPACE_REGEX +
                    UN_SUPPORTED_VALUE_REGEX +
                    SPACE_REGEX +
                    '(' +
                    SUPPORTED_IF_WHILE_OPERATORS +
                    SPACE_REGEX +
                    UN_SUPPORTED_VALUE_REGEX +
                    SPACE_REGEX +
                    ")*" +
                    CLOSE_PAREN_REGEX +
                    SPACE_REGEX +
                    OPEN_BRACKET_REGEX +
                    SPACE_REGEX +
                    END_LINE_REGEX;

    protected static final Pattern METHOD_NAME_PATTERN = Pattern.compile(METHOD_NAME_REGEX);


}