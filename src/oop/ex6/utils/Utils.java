package oop.ex6.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String SEARCH_BEGINNING = "^";
    public static final String EMPTY_STRING = "";
    public static final String BRACKETS_REGEX = "\\(([^)]+)\\)";

    /**
     * check if the line starts with the regex
     * @param line - the line to check
     * @param regex - the regex to check
     * @return true if the line starts with the regex, false otherwise
     */
    public static boolean isStartWith(String line, String regex) {
        Pattern pattern = Pattern.compile(SEARCH_BEGINNING + regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    /**
     * check if the line is a complete match to the regex
     * @param line - the line to check
     * @param regex - the regex to check
     * @return true if the line is a complete match to the regex, false otherwise
     */
    public static boolean isCompleteMatch(String line, String regex) {
        Pattern pattern = Pattern.compile(SEARCH_BEGINNING + regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.matches();
    }

    /**
     * extract the content of the brackets
     * @param line - the line to extract from
     * @return the content of the brackets
     */
    public static String extractBrackets(String line) {
        Pattern pattern = Pattern.compile(BRACKETS_REGEX);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return EMPTY_STRING;
    }

}
