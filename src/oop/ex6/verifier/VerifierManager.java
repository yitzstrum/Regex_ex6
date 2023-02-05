package oop.ex6.verifier;

import oop.ex6.symbol_table.MethodSymbolTable;
import oop.ex6.symbol_table.VariableSymbolTable;
import oop.ex6.parser.BadLineException;
import oop.ex6.parser.*;


/**
 * This class is responsible for verifying the logic of the code.
 */
public class VerifierManager implements Verifier {

    private static final String MSG = "unexpected token - ";
    private final Tokenizer tokenizer;
    private Token token;
    private final VariableSymbolTable localVariableSymbolTable;
    private final VariableSymbolTable globalVariableSymbolTable;
    private final MethodSymbolTable methodSymbolTable;
    private boolean inMethod;


    /**
     * Constructor.
     * @param tokenizer the tokenizer.
     * @param localVariableSymbolTable the local variable symbol table.
     * @param globalVariableSymbolTable the global variable symbol table.
     * @param methodSymbolTable the method symbol table.
     * @param inMethod whether we are in a method or not.
     */
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

    /**
     * Verifies the logic of the code. Uses Factory design pattern.
     * @throws BadLogicException if the logic is bad.
     * @throws BadLineException if the syntax is bad.
     */
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