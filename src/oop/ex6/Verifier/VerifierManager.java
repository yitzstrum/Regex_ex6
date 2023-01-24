package oop.ex6.Verifier;

import oop.ex6.SymbolTable.MethodSymbolTable;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.parser.Token;
import oop.ex6.parser.Tokenizer;

public class VerifierManager implements Verifier {

    private static final String MSG = "ERROR: Call to an unassigned variable. ";
    private final Tokenizer tokenizer;
    private Token token;
    private final VariableSymbolTable variableSymbolTable;
    private final MethodSymbolTable methodSymbolTable;

    public VerifierManager(Tokenizer tokenizer, Token token, VariableSymbolTable variableSymbolTable,
                           MethodSymbolTable methodSymbolTable) {
      this.tokenizer = tokenizer;
      this.token = token;
      this.variableSymbolTable = variableSymbolTable;
      this.methodSymbolTable = methodSymbolTable;
    }

    @Override
    public boolean verify() {
        Token.TokenType type = token.getType();
        switch (type) {
            case VARIABLE_DECLARATION:
                return new VariableDeclarationVerifier(tokenizer, variableSymbolTable).verify();
        }

        return false; // error
    }
}
