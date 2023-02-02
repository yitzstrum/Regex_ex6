package oop.ex6.parser;

import oop.ex6.SymbolTable.VariableData;
import oop.ex6.utils.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodDeclarationParser extends Parser{

    private Token token;

    public static final Pattern METHOD_NAME_PATTERN = Pattern.compile(METHOD_NAME_REGEX);
    public static final Pattern PARAMS_PATTERN = Pattern.compile(PARAMS_REGEX);

    private String methodName;
    private List<VariableData> methodParams = new ArrayList<>();

    public MethodDeclarationParser(Token token) {
        this.token = token;
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

    private static String getName(String decLine) {
        Matcher matchMethodName = METHOD_NAME_PATTERN.matcher(decLine);
        matchMethodName.find();
        return decLine.substring(matchMethodName.start(), matchMethodName.end());
    }
}
