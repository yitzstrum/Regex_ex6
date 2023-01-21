package oop.ex6.Verifier;

import oop.ex6.SymbolTable.MethodSymbolTable;
import oop.ex6.SymbolTable.VariableData;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.parser.Token;

public class VariableDeclarationVerifier implements Verifier {

    private final Token token;

    VariableDeclarationVerifier(Token token, VariableSymbolTable variableSymbolTable) {
        if (token.getType() != Token.TokenType.VARIABLE_DECLARATION) {
            throw new IllegalArgumentException("Token is not a variable declaration");
        }
        this.token = token;
    }

    @Override
    public boolean verify() {
        return false;
    }

    private VariableData.Type extractType(String content) {
        // regex for check if it int double char boolean
        return null;
    }

}


