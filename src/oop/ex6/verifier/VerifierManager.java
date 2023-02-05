package oop.ex6.verifier;

import oop.ex6.symbol_table.MethodSymbolTable;
import oop.ex6.symbol_table.VariableSymbolTable;
import oop.ex6.parser.BadLineException;
import oop.ex6.parser.*;

public class VerifierManager implements Verifier {

    private static final String MSG = "ERROR: Call to an unassigned variable. ";
    private final Tokenizer tokenizer;
    private Token token;
    private final VariableSymbolTable localVariableSymbolTable;
    private final VariableSymbolTable globalVariableSymbolTable;
    private final MethodSymbolTable methodSymbolTable;
    private boolean inMethod;

    public VerifierManager(Tokenizer tokenizer, VariableSymbolTable localVariableSymbolTable,
                           VariableSymbolTable globalVariableSymbolTable, MethodSymbolTable methodSymbolTable,
                           boolean inMethod) {
        this.tokenizer = tokenizer;
        this.token = tokenizer.getCurrentToken();
        this.localVariableSymbolTable = localVariableSymbolTable;
        this.globalVariableSymbolTable = globalVariableSymbolTable;
        this.methodSymbolTable = methodSymbolTable;
        this.inMethod = inMethod;
    }

    @Override
    public void verify() throws BadLogicException, BadLineException {
        Token.TokenType type = token.getType();
        switch (type) {
            case VARIABLE_DECLARATION:
            case FINAL_VARIABLE_DECLARATION:
                new VariableDeclarationVerifier(tokenizer.getCurrentToken(), localVariableSymbolTable,
                        globalVariableSymbolTable).verify();
                tokenizer.advanceToken();
                break;
            case VARIABLE_ASSIGNMENT:
                new VariableAssignmentVerifier(tokenizer, localVariableSymbolTable,
                        globalVariableSymbolTable, inMethod).verify();
                tokenizer.advanceToken();
                break;
            case IF_WHILE_BLOCK:
                new WhileIfVerifierManager(tokenizer, localVariableSymbolTable,
                        globalVariableSymbolTable, methodSymbolTable).verify();
                break;
            case METHOD_DECLARATION:
                new MethodDeclarationVerifier(tokenizer, localVariableSymbolTable,
                        globalVariableSymbolTable, methodSymbolTable).verify();
                break;
            case METHOD_CALL:
                new MethodCallVerifier(tokenizer, localVariableSymbolTable,
                        globalVariableSymbolTable, methodSymbolTable).verify();
                tokenizer.advanceToken();
                break;

            case RETURN_STATEMENT:
                tokenizer.advanceToken();
                break;
            default:
                throw new BadLogicException(MSG + token.getContent());
        }
    }
}