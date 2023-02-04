package oop.ex6.Verifier;

import oop.ex6.SymbolTable.VariableData;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.exceptions.BadLogicException;
import oop.ex6.parser.DeclarationParser;
import oop.ex6.parser.Token;
import oop.ex6.utils.Pair;

import java.util.List;

public class VariableDeclarationVerifier implements Verifier {

    private Token token;

    private static final String VARIABLE_ALREADY_DECLARED_ERR = "Variable already declared";
    private static final String FINAL_DECLARATION_ERR =
            "Cannot declare a final variable without an assignment";
    private final static String ASSIGNMENT_ERR = "The assignment is invalid";
    private final VariableSymbolTable variableSymbolTable;
    private final DeclarationParser declarationParser;
    private boolean isMethodParam = false;


    public VariableDeclarationVerifier(Token token, VariableSymbolTable variableSymbolTable) throws BadLogicException {
        if (token.getType() != Token.TokenType.VARIABLE_DECLARATION && token.getType() != Token.TokenType.FINAL_VARIABLE_DECLARATION) {
            throw new IllegalArgumentException("Token is not a variable declaration");
        }
        this.token = token;
        this.variableSymbolTable = variableSymbolTable;
        this.declarationParser = new DeclarationParser(token.getContent());
    }

    public void setMethodFlag(boolean flag){
        isMethodParam = flag;
    }

    @Override
    public void verify() throws BadLogicException {
        addToTable();
    }

    private void addToTable() throws BadLogicException {
        List<Pair<String, String>> variables = declarationParser.parseAssigment();
        for (Pair<String, String> variable: variables) {
            if (variableSymbolTable.containsKey(variable.getFirst())) {
                throw new BadLogicException(VARIABLE_ALREADY_DECLARED_ERR);
            }
            addSingleArgument(variable);
        }
    }

    private void addSingleArgument(Pair<String, String> variable) throws BadLogicException {
        if (declarationParser.getIsFinal()) {
            addFinalVariable(variable);
        }
        else {
            addNonFinalVariable(variable);
        }
    }
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
        if (!VariableAssignmentVerifier.isValidAssign(type, value)) {
            throw new BadLogicException(ASSIGNMENT_ERR);
        }
        variableSymbolTable.put(name, variableData);
    }

    private void addFinalVariable(Pair<String, String> variable) throws BadLogicException {
        String name = variable.getFirst();
        String value = variable.getSecond();
        // TODO CHECK LATER
        if (isMethodParam) {
            VariableData.Type type = declarationParser.getType();
            VariableData variableData = new VariableData(type, VariableData.Modifier.FINAL);
            variableSymbolTable.put(name, variableData);
        }

        else if (value == null && !isMethodParam) {
            throw new BadLogicException(FINAL_DECLARATION_ERR);
        } else {
            VariableData.Type type = declarationParser.getType();
            if (!VariableAssignmentVerifier.isValidAssign(type, value) && !isMethodParam) {
                throw new BadLogicException(ASSIGNMENT_ERR);
            }
            VariableData variableData = new VariableData(type, VariableData.Modifier.FINAL);
            variableSymbolTable.put(name, variableData);
        }
    }
}