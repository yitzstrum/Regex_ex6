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

    private final static String DOESNT_EXIST_ERR = "The variable has not been declared";
    private static final String ASSIGN_FINAL_ERR_MSG = "Cannot assign a value to a final variable";
    private final VariableSymbolTable variableSymbolTable;
    private final DeclarationParser declarationParser;
    private Tokenizer tokenizer;
    private VariableSymbolTable globalVariableSymbolTable;

    public VariableAssignmentVerifier(Tokenizer tokenizer,
                                      VariableSymbolTable variableSymbolTable,
                                      VariableSymbolTable  globalVariableSymbolTable) {
        this.tokenizer = tokenizer;
        this.globalVariableSymbolTable = globalVariableSymbolTable;
        if (tokenizer.getCurrentToken().getType() != Token.TokenType.VARIABLE_ASSIGNMENT) {
            throw new IllegalArgumentException("Token is not a variable assignment");
        }

        this.globalVariableSymbolTable = globalVariableSymbolTable;
        this.tokenizer = tokenizer;
        this.variableSymbolTable = variableSymbolTable;
        this.declarationParser = new DeclarationParser(tokenizer.getCurrentToken().getContent(), true);

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
        return value.startsWith("\"") && value.endsWith("\"");
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
            try {
                verifySingleAssigment(assignment, variableSymbolTable);
            } catch (BadLogicException e) {
                verifySingleAssigment(assignment, globalVariableSymbolTable);
            }
        }
    }

    public static boolean  isVariableExists(VariableSymbolTable variableSymbolTable, String variableName) {
        return variableSymbolTable.containsKey(variableName);
    }

    private void verifySingleAssigment(Pair<String, String> assignment, VariableSymbolTable symbolTable)
            throws BadLogicException {
        String variableName = assignment.getFirst();
        String value = assignment.getSecond();
        if (!isVariableExists(symbolTable, variableName)) {
            throw new BadLogicException(DOESNT_EXIST_ERR);
        }
        VariableData variableData = symbolTable.get(variableName);
        if (variableData.isFinal()) {
            throw new BadLogicException(ASSIGN_FINAL_ERR_MSG);
        }
        isValidAssign(variableData.getType(), value);
        symbolTable.put(variableName, new VariableData(variableData.getType(),
                VariableData.Modifier.ASSIGNED));
    }
}