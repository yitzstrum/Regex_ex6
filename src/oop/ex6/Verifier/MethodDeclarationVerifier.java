package oop.ex6.Verifier;

import oop.ex6.SymbolTable.MethodSymbolTable;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.exceptions.BadLineException;
import oop.ex6.exceptions.BadLogicException;
import oop.ex6.parser.*;

public class MethodDeclarationVerifier implements Verifier{

    private Tokenizer tokenizer;
    private final String FINAL = "final";
    private final String RETURN_ERR = "The method should have a return statement prior to closing brackets";
    private final String CLOSING_BRACKET_ERR = "The method has no closing bracket";
    private final String INNER_METHOD_ERR = "Can't declare a method within a method";

    private final VariableSymbolTable localVariableSymbolTable;
    private final VariableSymbolTable globalVariableSymbolTable;
    private final MethodSymbolTable methodSymbolTable;

    public MethodDeclarationVerifier(Tokenizer tokenizer,
                                     VariableSymbolTable localVariableSymbolTable,
                                     VariableSymbolTable globalVariableSymbolTable,
                                     MethodSymbolTable methodSymbolTable) throws BadLineException, BadLogicException {
        this.tokenizer = tokenizer;
        this.localVariableSymbolTable = localVariableSymbolTable;
        this.globalVariableSymbolTable = globalVariableSymbolTable;
        this.methodSymbolTable = methodSymbolTable;
        initiateLocalVariables(tokenizer.getCurrentToken().getContent());
        tokenizer.advanceToken();
    }

    @Override
    public void verify() throws BadLogicException, BadLineException {
        Token currToken = tokenizer.getCurrentToken();
        Token prevToken = currToken;
        while (currToken.getType() != Token.TokenType.END_BLOCK){
            if(currToken.getType() == Token.TokenType.METHOD_DECLARATION){
                throw new BadLogicException(INNER_METHOD_ERR);
            }
            tokenizer.step(localVariableSymbolTable, globalVariableSymbolTable, methodSymbolTable);
            if (!tokenizer.hasNext()){
                throw new BadLogicException(CLOSING_BRACKET_ERR);
            }
            prevToken = currToken;
            currToken = tokenizer.getCurrentToken();
        }
        tokenizer.advanceToken();
        if (prevToken.getType() != Token.TokenType.RETURN_STATEMENT){
            throw new BadLogicException(RETURN_ERR);
        }
    }

    private void initiateLocalVariables(String methodDecLine) throws BadLineException, BadLogicException {
        String[] varDeclarations = MethodDeclarationParser.getMethodParams(methodDecLine);
        for (String varDec : varDeclarations){
            Token methodVarToken = new Token(varDec + ";");
            VariableDeclarationVerifier variableDeclarationVerifier =
                    new VariableDeclarationVerifier(methodVarToken, localVariableSymbolTable,
                            globalVariableSymbolTable);
            if (varDec.trim().startsWith(FINAL)){
                variableDeclarationVerifier.setMethodFlag(true); // TODO CHECK LATER

            }
            variableDeclarationVerifier.setMethodFlag(true);

            variableDeclarationVerifier.verify();
        }
    }
}
