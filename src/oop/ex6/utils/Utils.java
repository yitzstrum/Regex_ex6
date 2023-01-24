package oop.ex6.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String SEARCH_BEGINNING = "^";
    public static final String EMPTY_STRING = "";



    public static boolean isMatch(String line, String regex) {
        Pattern pattern = Pattern.compile(SEARCH_BEGINNING + regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    public static String removeSpaces(String line) {
        return line.replaceAll("\\s+", "");
    }

}
