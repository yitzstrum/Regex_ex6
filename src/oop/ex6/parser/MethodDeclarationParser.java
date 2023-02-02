package oop.ex6.parser;

import oop.ex6.SymbolTable.VariableData;
import oop.ex6.utils.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodDeclarationParser {

    private Token token;

    public static final String SPACE_REGEX = "\\s*";
    public static final int METHOD_NAME_START_IND = 4;
    private static final String COMMA = ",";
    private static final String VARIABLE_NAME_REGEX = "(_+[\\w]|[a-zA-Z])[\\w]*";
    private static final String VOID_REGEX = "void";
    private static final String METHOD_NAME_REGEX = "([a-zA-Z]+[\\w]*)";
    private static final String OPEN_PAREN_REGEX = "\\(";
    private static final String CLOSE_PAREN_REGEX = "\\)";
    private static final String OPEN_BRACKET_REGEX = "\\{";
    private static final String TYPE_REGEX = "\\s*(int|double|String|boolean|char)\\s*";
    private static final String SPACE_PLUS = "\\s+";
    private static final String FINAL_REGEX = "(final\\s+)?";
    private static final String TYPE_AND_NAME_REGEX =
            SPACE_REGEX + FINAL_REGEX + SPACE_REGEX +
                    TYPE_REGEX + SPACE_PLUS + VARIABLE_NAME_REGEX + SPACE_REGEX;

    private static final String METHOD_DEC_REGEX = "^" +
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

    private static final String PARAMS_REGEX = "(" +
            SPACE_REGEX +
            TYPE_AND_NAME_REGEX + "(" + "," + TYPE_AND_NAME_REGEX + ")*" +
            SPACE_REGEX + ")";

    public static final Pattern METHOD_DEC_PATTERN = Pattern.compile(METHOD_DEC_REGEX);
    public static final Pattern METHOD_NAME_PATTERN = Pattern.compile(METHOD_NAME_REGEX);
    public static final Pattern PARAMS_PATTERN = Pattern.compile(PARAMS_REGEX);

    private String methodName;
    private List<VariableData> methodParams = new ArrayList<>();

    public MethodDeclarationParser(Token token) throws BadLineException {
        this.token = token;
        checkMethodSyntax();
        String trimmedDecLine = token.getContent().trim();
        methodName = getName(trimmedDecLine.substring(METHOD_NAME_START_IND)).trim();
        setMethodParams(trimmedDecLine);
    }

    public Pair<String, List<VariableData>> getMethodData(){
        return new Pair<>(methodName, methodParams);
    }

    private void setMethodParams(String decLine){
        Matcher paramMatcher = PARAMS_PATTERN.matcher(decLine);
        if (paramMatcher.find()){
            String[] params = paramMatcher.group(1).split(COMMA);
            for (String param : params){
                DeclarationParser parser = new DeclarationParser(param);
                methodParams.add(new VariableData(parser.getType(), VariableData.Modifier.NONE));
            }
        }
    }

    public static String[] getMethodParams(String methodDec){
        Matcher paramMatcher = PARAMS_PATTERN.matcher(methodDec);
        if(paramMatcher.find()){
            return paramMatcher.group(1).split(COMMA);
        }
        return new String[0];
    }

    private void checkMethodSyntax() throws BadLineException {
        Matcher methodDecMatcher = METHOD_DEC_PATTERN.matcher(token.getContent());
        if (!methodDecMatcher.matches()){
            throw new BadLineException(token.getContent());
        }
    }

    private static String getName(String decLine) {
        Matcher matchMethodName = METHOD_NAME_PATTERN.matcher(decLine);
        matchMethodName.find();
        return decLine.substring(matchMethodName.start(), matchMethodName.end());
    }
}
