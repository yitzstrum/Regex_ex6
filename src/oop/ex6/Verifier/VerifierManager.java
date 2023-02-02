package oop.ex6.Verifier;

import oop.ex6.SymbolTable.MethodSymbolTable;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.parser.SJavaException;
import oop.ex6.parser.Token;
import oop.ex6.parser.Tokenizer;

public class VerifierManager implements Verifier {

    private static final String MSG = "ERROR: Call to an unassigned variable. ";
    private final Tokenizer tokenizer;
    private Token token;
    private final VariableSymbolTable localVariableSymbolTable;
    private final VariableSymbolTable globalVariableSymbolTable;
    private final MethodSymbolTable methodSymbolTable;

    public VerifierManager(Tokenizer tokenizer, VariableSymbolTable localVariableSymbolTable,
                           VariableSymbolTable globalVariableSymbolTable, MethodSymbolTable methodSymbolTable) {
        this.tokenizer = tokenizer;
        this.token = tokenizer.getCurrentToken();
        this.localVariableSymbolTable = localVariableSymbolTable;
        this.globalVariableSymbolTable = globalVariableSymbolTable;
        this.methodSymbolTable = methodSymbolTable;
    }

    @Override
    public boolean verify() throws SJavaException {
        Token.TokenType type = token.getType();
        switch (type) {
            case VARIABLE_DECLARATION:
                return new VariableDeclarationVerifier(tokenizer, localVariableSymbolTable).verify();
            case VARIABLE_ASSIGNMENT:
                return new VariableAssignmentVerifier(tokenizer, localVariableSymbolTable).verify();
            case IF_WHILE_BLOCK:
                return new WhileIfVerifierManager(tokenizer, localVariableSymbolTable,
                        globalVariableSymbolTable,
                         methodSymbolTable).verify();
        }

        return false; // error
    }
}
