package oop.ex6.parser;

import oop.ex6.exceptions.BadLineException;
import oop.ex6.utils.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodCallParser extends Parser{

    private Token token;
    private String methodName;
    private String[] callValues;
    private String line;
    private static final Pattern METHOD_CALL_PATTERN = Pattern.compile(METHOD_CALL_REGEX);

    public MethodCallParser(Token token) throws BadLineException {
        this.token = token;
        line = token.getContent();
        methodName = getMethodName();
        setMethodValues();
    }

    public Pair<String, String[]> getMethodData(){
        return new Pair<>(methodName, callValues);
    }

    private void setMethodValues(){
        Matcher valuesMatcher = METHOD_CALL_PATTERN.matcher(line);
        valuesMatcher.find();
        String values = valuesMatcher.group(2);
        if (values != null){
            callValues = values.split(COMMA);
        }
    }

    private String getMethodName(){
        Matcher matchMethodName = METHOD_NAME_PATTERN.matcher(line);
        matchMethodName.find();
        return line.substring(matchMethodName.start(), matchMethodName.end());
    }

}
