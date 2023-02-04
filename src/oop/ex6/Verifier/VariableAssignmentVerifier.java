package oop.ex6.Verifier;

import oop.ex6.SymbolTable.VariableData;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.exceptions.BadLogicException;
import oop.ex6.parser.DeclarationParser;
import oop.ex6.parser.Token;
import oop.ex6.parser.Tokenizer;
import oop.ex6.utils.Pair;

import java.util.List;

public class VariableAssignmentVerifier implements Verifier{

    private static final String STRING_REGEX = "\"[^\"]*\"";

    protected static final String VARIABLE_NAME_REGEX = "(_+[\\w]|[a-zA-Z])[\\w]*";

    private final static String DOESNT_EXIST_ERR = "The variable has not been declared";
    private static final String ASSIGN_FINAL_ERR_MSG = "Cannot assign a value to a final variable";
    private static final String ASSIGN_TYPE_ERR = "The value doesn't match the variable type";
    private final DeclarationParser declarationParser;
    private Tokenizer tokenizer;
    private final VariableSymbolTable localVariableSymbolTable;
    private VariableSymbolTable globalVariableSymbolTable;

    public VariableAssignmentVerifier(Tokenizer tokenizer,
                                      VariableSymbolTable localVariableSymbolTable,
                                      VariableSymbolTable globalVariableSymbolTable) {
        this.tokenizer = tokenizer;
        this.globalVariableSymbolTable = globalVariableSymbolTable;
        if (tokenizer.getCurrentToken().getType() != Token.TokenType.VARIABLE_ASSIGNMENT) {
            throw new IllegalArgumentException("Token is not a variable assignment");
        }

        this.globalVariableSymbolTable = globalVariableSymbolTable;
        this.tokenizer = tokenizer;
        this.localVariableSymbolTable = localVariableSymbolTable;
        this.declarationParser = new DeclarationParser(tokenizer.getCurrentToken().getContent(), true);

    }

    public static boolean isGeneralValidAssign(VariableSymbolTable localVariableSymbolTable,
                                               VariableSymbolTable globalVariableSymbolTable,
                                               VariableData.Type type , String value) {
        if (value == null) {
            return true;
        }
        if (value.matches(VARIABLE_NAME_REGEX)) {
            return isValidVariableAssign(localVariableSymbolTable, globalVariableSymbolTable, type, value);
        }
        return isValidAssign(type, value);
    }

    public static boolean isValidAssign(VariableData.Type type , String value) {
        if (value == null) {
            return true;
        }

        switch (type) {
            case INT:
                return isValidInt(value);
            case DOUBLE:
                return isValidDouble(value);
            case STRING:
                return isValidString(value);
            case BOOLEAN:
                return isValidBoolean(value);
            case CHAR:
                return isValidChar(value);
            default:
                throw new IllegalArgumentException("Invalid type");
        }
    }

    private static boolean isValidVariableAssign(VariableSymbolTable localVariableSymbolTable,
                                                 VariableSymbolTable globalVariableSymbolTable,
                                                 VariableData.Type type , String value) {
        if (!value.matches(VARIABLE_NAME_REGEX)) {
            return false;
        }
        VariableSymbolTable symbolTable;
        if (globalVariableSymbolTable.containsKey(value)) {
            symbolTable = globalVariableSymbolTable;
        } else if (localVariableSymbolTable.containsKey(value)) {
            symbolTable = localVariableSymbolTable;
        } else {
            return false;
        }
        return symbolTable.get(value).getType() == type && symbolTable.get(value).isInitialized();
    }

    private static boolean isValidInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private static boolean isValidDouble(String value) {
        if (isValidInt(value)) {
            return true;
        }
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidString(String value) {
        return value.matches(STRING_REGEX);
    }

    private static boolean isValidBoolean(String value) {
        return value.equals("true") || value.equals("false") || isValidDouble(value);
    }

    public static boolean isValidChar(String value) {
        return value.length() == 3 && value.startsWith("'") && value.endsWith("'");
    }


    @Override
    public void verify() throws BadLogicException {
        List<Pair<String, String>> assignments = declarationParser.parseAssigment();
        for (Pair<String, String> assignment : assignments) {
            verifySingleAssigment(assignment);
        }
    }

    public static boolean  isVariableExists(VariableSymbolTable variableSymbolTable, String variableName) {
        return variableSymbolTable.containsKey(variableName);
    }


    private void verifySingleAssigment(Pair<String, String> assignment)
            throws BadLogicException {

        String variableName = assignment.getFirst();
        String value = assignment.getSecond();
        VariableSymbolTable variableSymbolTable;
        if (isVariableExists(localVariableSymbolTable, variableName)) {
            variableSymbolTable = localVariableSymbolTable;
        }
        else if (isVariableExists(globalVariableSymbolTable, variableName)){
            variableSymbolTable = globalVariableSymbolTable;
        }
        else{
            throw new BadLogicException(DOESNT_EXIST_ERR);
        }
        VariableData variableData = variableSymbolTable.get(variableName);;
        if (variableData.isFinal()) {
            throw new BadLogicException(ASSIGN_FINAL_ERR_MSG);
        }


        if (!isGeneralValidAssign(localVariableSymbolTable, globalVariableSymbolTable, variableData.getType(), value)){
            throw new BadLogicException(ASSIGN_TYPE_ERR);
        }
        variableSymbolTable.put(variableName, new VariableData(variableData.getType(),
                VariableData.Modifier.ASSIGNED));
    }
}