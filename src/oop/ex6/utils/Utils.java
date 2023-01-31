package oop.ex6.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String SEARCH_BEGINNING = "^";
    public static final String EMPTY_STRING = "";
    public static final String BRACKETS_REGEX = "\\(([^)]+)\\)";



    public static boolean isMatch(String line, String regex) {
        Pattern pattern = Pattern.compile(SEARCH_BEGINNING + regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    public static boolean isCompleteMatch(String line, String regex) {
        Pattern pattern = Pattern.compile(SEARCH_BEGINNING + regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.matches();
    }

    public static String extractBrackets(String line) {
        Pattern pattern = Pattern.compile(BRACKETS_REGEX);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return EMPTY_STRING;
    }

    public static String removeSpaces(String line) {
        return line.replaceAll("\\s+", EMPTY_STRING);
    }

}
