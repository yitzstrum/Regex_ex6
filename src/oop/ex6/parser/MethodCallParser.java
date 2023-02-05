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

    /**
     * Constructor
     * @param token - the token to parse
     * @throws BadLineException - if the line is not a valid method call
     */
    public MethodCallParser(Token token) throws BadLineException {
        this.token = token;
        line = token.getContent();
        methodName = getMethodName();
        setMethodValues();
    }

    /**
     * get the method name and the values of the method call
     * @return
     */
    public Pair<String, String[]> getMethodData(){
        return new Pair<>(methodName, callValues);
    }

    /**
     * set the values of the method call
     * @throws BadLineException - if the values are not valid
     */
    private void setMethodValues() throws BadLineException {
        Matcher valuesMatcher = METHOD_CALL_PATTERN.matcher(line);
        valuesMatcher.find();
        String values = valuesMatcher.group(2);
        if (values != null){
            if (values.equals(EMPTY_STRING)){
                callValues = new String[0];
            }
            else{
                callValues = values.split(COMMA);
            }
        }
        for (String value : callValues){
            if (!value.trim().matches(VALUES_REGEX)){
                throw new BadLineException(value);
            }
        }
    }

    /**
     * get the method name
     * @return - the method name
     */
    private String getMethodName(){
        Matcher matchMethodName = METHOD_NAME_PATTERN.matcher(line);
        matchMethodName.find();
        return line.substring(matchMethodName.start(), matchMethodName.end());
    }

}
