package oop.ex6.Verifier;

import oop.ex6.SymbolTable.MethodSymbolTable;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.parser.Token;

public class VerifierManager implements Verifier {

    private static final String MSG = "ERROR: Call to an unassigned variable. ";
    private final Token token;
    private final VariableSymbolTable variableSymbolTable;
    private final MethodSymbolTable methodSymbolTable;

    public VerifierManager(Token token, VariableSymbolTable variableSymbolTable,
                           MethodSymbolTable methodSymbolTable) {
      this.token = token;
      this.variableSymbolTable = variableSymbolTable;
      this.methodSymbolTable = methodSymbolTable;
    }

    @Override
    public boolean verify() {
        Token.TokenType type = token.getType();
        switch (type) {
            case VARIABLE_DECLARATION:
                return new VariableDeclarationVerifier(token, variableSymbolTable).verify();
        }

        return false; // error
    }

}

