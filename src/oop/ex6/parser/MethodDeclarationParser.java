package oop.ex6.parser;

import oop.ex6.symbol_table.VariableData;
import oop.ex6.verifier.BadLogicException;
import oop.ex6.utils.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodDeclarationParser extends Parser{

    private Token token;


    private static final Pattern PARAMS_PATTERN = Pattern.compile(PARAMS_REGEX);
    private static final String RESERVED_WORDS_ERR = "Can't use a reserved name as the method name";

    private String methodName;
    private List<VariableData> methodParams = new ArrayList<>();


    /**
     * Constructor
     * @param token - the token to parse
     * @throws BadLogicException - if the method name is a reserved word
     */
    public MethodDeclarationParser(Token token) throws BadLogicException {
        this.token = token;
        String trimmedDecLine = token.getContent().trim();
        methodName = getName(trimmedDecLine.substring(METHOD_NAME_START_IND)).trim();
        if (methodName.matches(RESERVED_WORDS)){
            throw new BadLogicException(RESERVED_WORDS_ERR);
        }
        setMethodParams(trimmedDecLine);
    }

    /**
     * get the method name and the values of the method call
     * @return - a pair of the method name and the method params
     */
    public Pair<String, List<VariableData>> getMethodData(){
        return new Pair<>(methodName, methodParams);
    }


    /**
     * set the params of the method
     * @param decLine - the method declaration line
     * @throws BadLogicException - if the params are not valid
     */
    private void setMethodParams(String decLine) throws BadLogicException {
        Matcher paramMatcher = PARAMS_PATTERN.matcher(decLine);
        if (paramMatcher.find()){
            String[] params = paramMatcher.group(1).split(COMMA);
            for (String param : params){
                DeclarationParser parser = new DeclarationParser(param);
                methodParams.add(new VariableData(parser.getType(), VariableData.Modifier.NONE));
            }
        }
    }

    /**
     * get the method params
     * @param methodDec - the method declaration line
     * @return - an array of the method params
     */
    public static String[] getMethodParams(String methodDec){
        Matcher paramMatcher = PARAMS_PATTERN.matcher(methodDec);
        if(paramMatcher.find()){
            return paramMatcher.group(1).split(COMMA);
        }
        return new String[0];
    }

    /**
     * get the method name
     * @param decLine - the method declaration line
     * @return - the method name
     */
    private String getName(String decLine) {
        Matcher matchMethodName = METHOD_NAME_PATTERN.matcher(decLine);
        matchMethodName.find();
        return decLine.substring(matchMethodName.start(), matchMethodName.end());
    }
}
