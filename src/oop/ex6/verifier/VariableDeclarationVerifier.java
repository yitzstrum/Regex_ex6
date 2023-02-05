package oop.ex6.verifier;

import oop.ex6.symbol_table.VariableData;
import oop.ex6.symbol_table.VariableSymbolTable;
import oop.ex6.parser.DeclarationParser;
import oop.ex6.parser.Token;
import oop.ex6.utils.Pair;

import java.util.List;

/**
 * VariableDeclarationVerifier class which inherits from Verifier and is in charge of validating the
 * variable declaration logic
 */
public class VariableDeclarationVerifier implements Verifier {

    private static final String VARIABLE_ALREADY_DECLARED_ERR = "Variable already declared";
    private static final String FINAL_DECLARATION_ERR =
            "Cannot declare a final variable without an assignment";
    private final static String ASSIGNMENT_ERR = "The assignment is invalid";
    private final static String DOUBLE_DEC_ERR = "The variable can't be declared twice in the same scope";

    private Token token;
    private final VariableSymbolTable localVariableSymbolTable;
    private final DeclarationParser declarationParser;
    private boolean isMethodParam = false;
    private VariableSymbolTable globalVariableSymbolTable;


    /**
     * Constructor
     * @param token - the token to verify
     * @param localVariableSymbolTable - the local variable symbol table
     * @param globalVariableSymbolTable - the global variable symbol table
     * @throws BadLogicException - if the token is not a variable declaration
     */
    public VariableDeclarationVerifier(Token token, VariableSymbolTable localVariableSymbolTable,
                                       VariableSymbolTable globalVariableSymbolTable) throws BadLogicException
    {
        if (token.getType() != Token.TokenType.VARIABLE_DECLARATION && token.getType() !=
                Token.TokenType.FINAL_VARIABLE_DECLARATION) {
            throw new IllegalArgumentException("Token is not a variable declaration");
        }

        this.token = token;
        this.globalVariableSymbolTable = globalVariableSymbolTable;
        this.localVariableSymbolTable = localVariableSymbolTable;
        this.declarationParser = new DeclarationParser(token.getContent());
    }

    /**
     * Sets the method flag
     * @param flag - the flag to set
     */
    public void setMethodFlag(boolean flag){
        isMethodParam = flag;
    }

    /**
     * Verity the token
     * @throws BadLogicException - if the token is not valid
     */
    @Override
    public void verify() throws BadLogicException {
        addToTable();
    }

    /**
     * Adds the variables to the symbol table
     * @throws BadLogicException Exception for a logic error
     */
    private void addToTable() throws BadLogicException {
        List<Pair<String, String>> variables = declarationParser.parseAssigment();
        for (Pair<String, String> variable: variables) {
            if (localVariableSymbolTable.containsKey(variable.getFirst())) {
                throw new BadLogicException(VARIABLE_ALREADY_DECLARED_ERR);
            }
            addSingleArgument(variable);
        }
    }

    /**
     * Adds a single variable to the symbol table
     * @param variable - the variable to add
     * @throws BadLogicException - if the variable is not valid
     */
    private void addSingleArgument(Pair<String, String> variable) throws BadLogicException {
        if (localVariableSymbolTable.containsKey(variable.getFirst())){
            throw new BadLogicException(DOUBLE_DEC_ERR);
        }
        if (declarationParser.getIsFinal()) {
            addFinalVariable(variable);
        }
        else {
            addNonFinalVariable(variable);
        }
    }

    /**
     * Adds a non-final variable to the symbol table
     * @param variable - the variable to add
     * @throws BadLogicException - if the variable is not valid
     */
    private void addNonFinalVariable(Pair<String, String> variable) throws BadLogicException {
        String name = variable.getFirst();
        String value = variable.getSecond();
        VariableData.Type type = declarationParser.getType();
        VariableData variableData;
        if (isMethodParam) {
            variableData = new VariableData(type,  VariableData.Modifier.ASSIGNED);
        }
        else if (value == null) {
            variableData = new VariableData(type,  VariableData.Modifier.NONE);
        }
        else {
            variableData = new VariableData(type,  VariableData.Modifier.ASSIGNED);
        }
        if (!VariableAssignmentVerifier.isGeneralValidAssign(localVariableSymbolTable,
                globalVariableSymbolTable, variableData.getType(), value)) {
            throw new BadLogicException(ASSIGNMENT_ERR);
        }
        localVariableSymbolTable.put(name, variableData);
    }


    /**
     * Adds a final variable to the symbol table
     * @param variable - the variable to add
     * @throws BadLogicException - if the variable is not valid
     */
    private void addFinalVariable(Pair<String, String> variable) throws BadLogicException {
        String name = variable.getFirst();
        String value = variable.getSecond();
        if (isMethodParam) {
            VariableData.Type type = declarationParser.getType();
            VariableData variableData = new VariableData(type, VariableData.Modifier.FINAL);
            localVariableSymbolTable.put(name, variableData);
        }

        else if (value == null) {
            throw new BadLogicException(FINAL_DECLARATION_ERR);
        } else {
            VariableData.Type type = declarationParser.getType();
            if (!VariableAssignmentVerifier.isGeneralValidAssign(globalVariableSymbolTable,
                    localVariableSymbolTable, type, value) && !isMethodParam) {
                throw new BadLogicException(ASSIGNMENT_ERR);
            }
            VariableData variableData = new VariableData(type, VariableData.Modifier.FINAL);
            localVariableSymbolTable.put(name, variableData);
        }
    }
}