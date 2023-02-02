package oop.ex6.Verifier;

import oop.ex6.SymbolTable.MethodSymbolTable;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.parser.*;

public class MethodDeclarationVerifier implements Verifier{

    private Tokenizer tokenizer;
    private final String RETURN_ERR = "The method should have a return statement prior to closing brackets";
    private final VariableSymbolTable localVariableSymbolTable;
    private final VariableSymbolTable globalVariableSymbolTable;
    private final MethodSymbolTable methodSymbolTable;

    public MethodDeclarationVerifier(Tokenizer tokenizer,
                                     VariableSymbolTable localVariableSymbolTable,
                                     VariableSymbolTable globalVariableSymbolTable,
                                     MethodSymbolTable methodSymbolTable) throws BadLineException {
        this.tokenizer = tokenizer;
        this.localVariableSymbolTable = localVariableSymbolTable;
        this.globalVariableSymbolTable = globalVariableSymbolTable;
        this.methodSymbolTable = methodSymbolTable;
        initiateLocalVariables(tokenizer.getCurrentToken().getContent());
        tokenizer.advanceToken();
    }

    /**
     * @return true if the token was handled, false otherwise
     */
    @Override
    public boolean verify() throws SJavaException, BadLogicException, BadLineException {
        Token currToken = tokenizer.getCurrentToken();
        Token prevToken = currToken;
        //Todo: what if there is no closing bracket?
        while (currToken.getType() != Token.TokenType.END_BLOCK){
            tokenizer.step(localVariableSymbolTable, globalVariableSymbolTable, methodSymbolTable);
            prevToken = currToken;
            currToken = tokenizer.getCurrentToken();
        }
        if (prevToken.getType() != Token.TokenType.RETURN_STATEMENT){
            throw new BadLogicException(RETURN_ERR);
        }
        return true;
    }

    private void initiateLocalVariables(String methodDecLine) throws BadLineException {
        String[] varDeclarations = MethodDeclarationParser.getMethodParams(methodDecLine);
        for (String varDec : varDeclarations){
            Token methodVarToken = new Token(varDec);
            new VariableDeclarationVerifier(methodVarToken, localVariableSymbolTable).verify();
        }
    }
}
