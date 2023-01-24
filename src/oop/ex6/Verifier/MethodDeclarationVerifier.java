package oop.ex6.Verifier;

import oop.ex6.SymbolTable.MethodData;
import oop.ex6.parser.Token;

public class MethodDeclarationVerifier implements Verifier{

    private Token token;
    private final String METHOD_DECLARATION_REGEX = "";

    public MethodDeclarationVerifier(Token token){
        this.token = token;
    }

    /**
     * @return true if the token was handled, false otherwise
     */
    @Override
    public boolean verify() {
        return false;
    }
}
