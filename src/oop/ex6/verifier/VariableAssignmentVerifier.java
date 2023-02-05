package oop.ex6.verifier;

import oop.ex6.symbol_table.VariableData;
import oop.ex6.symbol_table.VariableSymbolTable;
import oop.ex6.parser.DeclarationParser;
import oop.ex6.parser.Token;
import oop.ex6.parser.Tokenizer;
import oop.ex6.utils.Pair;

import java.util.List;

public class VariableAssignmentVerifier implements Verifier{

    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String APOSTROPHE = "'";
    private static final int CHAR_LENGTH = 3;

    private static final String STRING_REGEX = "\"[^\"]*\"";

    private static final String NUMBER_REGEX = "(-?\\d+(\\.\\d+)?)";

    private static final String VARIABLE_NAME_REGEX = "(_+[\\w]|[a-zA-Z])[\\w]*";

    private final static String DOESNT_EXIST_ERR = "The variable has not been declared";
    private static final String ASSIGN_FINAL_ERR_MSG = "Cannot assign a value to a final variable";
    private static final String ASSIGN_TYPE_ERR = "The value doesn't match the variable type";
    private final DeclarationParser declarationParser;
    private Tokenizer tokenizer;
    private final VariableSymbolTable localVariableSymbolTable;
    private VariableSymbolTable globalVariableSymbolTable;
    private boolean inMethod;

    /**
     * Constructor
     * @param tokenizer - the tokenizer
     * @param localVariableSymbolTable - the local variable symbol table
     * @param globalVariableSymbolTable - the global variable symbol table
     * @param inMethod - if the variable is in a method
     */
    public VariableAssignmentVerifier(Tokenizer tokenizer,
                                      VariableSymbolTable localVariableSymbolTable,
                                      VariableSymbolTable globalVariableSymbolTable,
                                      boolean inMethod) {
        this.tokenizer = tokenizer;
        this.globalVariableSymbolTable = globalVariableSymbolTable;
        this.inMethod = inMethod;
        if (tokenizer.getCurrentToken().getType() != Token.TokenType.VARIABLE_ASSIGNMENT) {
            throw new IllegalArgumentException("Token is not a variable assignment");
        }

        this.globalVariableSymbolTable = globalVariableSymbolTable;
        this.tokenizer = tokenizer;
        this.localVariableSymbolTable = localVariableSymbolTable;
        this.declarationParser = new DeclarationParser(tokenizer.getCurrentToken().getContent(),
                false);

    }

    /**
     * Checks if the assignment is valid
     * @param localVariableSymbolTable - the local variable symbol table
     * @param globalVariableSymbolTable - the global variable symbol table
     * @param type - the type of the variable
     * @param value - the value of the variable
     * @return true if the assignment is valid
     */
    public static boolean isGeneralValidAssign(VariableSymbolTable localVariableSymbolTable,
                                               VariableSymbolTable globalVariableSymbolTable,
                                               VariableData.Type type , String value) {
        if (value == null) {
            return true;
        }
        if (localVariableSymbolTable.containsKey(value) || globalVariableSymbolTable.containsKey(value)) {
            return isValidVariableAssign(localVariableSymbolTable, globalVariableSymbolTable, type, value);
        }
        return isValidAssign(type, value);
    }

    /**
     * Checks if the assignment is valid
     * @param type - the type of the variable
     * @param value - the value of the variable
     * @return true if the assignment is valid
     */
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

    /**
     * Verifies a assigment of variable
     * @param localVariableSymbolTable - the local variable symbol table
     * @param globalVariableSymbolTable - the global variable symbol table
     * @param type - the type of the variable
     * @param value - the value of the variable
     * @return true if the assignment is valid
     */
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
        return symbolTable.get(value).canBeAssignedWith(type) && symbolTable.get(value).isInitialized();
    }

    /**
     * Checks if the value is a valid int
     * @param value - the value
     * @return true if the value is a valid int
     */
    private static boolean isValidInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * Checks if the value is a valid double
     * @param value - the value
     * @return true if the value is a valid double
     */
    private static boolean isValidDouble(String value) {
        return value.matches(NUMBER_REGEX);
    }

    /**
     * Checks if the value is a valid string
     * @param value - the value
     * @return true if the value is a valid string
     */
    private static boolean isValidString(String value) {
        return value.matches(STRING_REGEX);
    }

    /**
     * Checks if the value is a valid boolean
     * @param value - the value
     * @return true if the value is a valid boolean
     */
    private static boolean isValidBoolean(String value) {
        return value.equals(TRUE) || value.equals(FALSE) || isValidDouble(value);
    }

    /**
     * Checks if the value is a valid char
     * @param value - the value
     * @return true if the value is a valid char
     */
    private static boolean isValidChar(String value) {
        return value.length() == CHAR_LENGTH && value.startsWith(APOSTROPHE) && value.endsWith(APOSTROPHE);
    }

    /**
     * Verifies the assignment
     * @throws BadLogicException - if the assignment is invalid
     */
    @Override
    public void verify() throws BadLogicException {
        List<Pair<String, String>> assignments = declarationParser.parseAssigment();
        for (Pair<String, String> assignment : assignments) {
            verifySingleAssigment(assignment);
        }
    }

    /**
     * Checks if the variable exists in the symbol table
     * @param variableSymbolTable - the symbol table
     * @param variableName - the variable name
     * @return true if the variable exists
     */
    private static boolean  isVariableExists(VariableSymbolTable variableSymbolTable, String variableName) {
        return variableSymbolTable.containsKey(variableName);
    }

    private void verifySingleAssigmentInMethod(VariableSymbolTable variableSymbolTable, String variableName) {
        VariableData tempVar = globalVariableSymbolTable.get(variableName);
        if (tempVar.isFinal()){
            localVariableSymbolTable.put(variableName, new VariableData(tempVar.getType(),
                    VariableData.Modifier.FINAL));
        }
        else {
            localVariableSymbolTable.put(variableName, new VariableData(tempVar.getType(),
                    VariableData.Modifier.ASSIGNED));
        }
    }

    /**
     * Verifies that the variable exists and returns the symbol table it exists in
     * @param variableName - the variable name
     * @return the symbol table the variable exists in
     * @throws BadLogicException - if the variable doesn't exist
     */
    private VariableSymbolTable verifyVarExists(String variableName)
            throws BadLogicException {
        if (isVariableExists(localVariableSymbolTable, variableName)) {
            return localVariableSymbolTable;
        }
        else if (isVariableExists(globalVariableSymbolTable, variableName)){
            if (inMethod){
                verifySingleAssigmentInMethod(globalVariableSymbolTable, variableName);
                return localVariableSymbolTable;
            }
            else{
                return globalVariableSymbolTable;
            }
        }
        else{
            throw new BadLogicException(DOESNT_EXIST_ERR);
        }
    }

    /**
     * Verifies a single assignment
     * @param assignment - the assignment
     * @throws BadLogicException - if the assignment is invalid
     */
    private void verifySingleAssigment(Pair<String, String> assignment)
            throws BadLogicException {

        String variableName = assignment.getFirst();
        String value = assignment.getSecond();
        VariableSymbolTable variableSymbolTable = verifyVarExists(variableName);

        VariableData variableData = variableSymbolTable.get(variableName);
        if (variableData.isFinal()) {
            throw new BadLogicException(ASSIGN_FINAL_ERR_MSG);
        }
        if (!isGeneralValidAssign(localVariableSymbolTable, globalVariableSymbolTable,
                variableData.getType(), value)){
            throw new BadLogicException(ASSIGN_TYPE_ERR);
        }
        if (!inMethod){
            variableSymbolTable.put(variableName, new VariableData(variableData.getType(),
                    VariableData.Modifier.ASSIGNED));
        }
    }
}