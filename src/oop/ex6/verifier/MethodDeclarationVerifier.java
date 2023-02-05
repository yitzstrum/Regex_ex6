package oop.ex6.verifier;

import oop.ex6.symbol_table.MethodSymbolTable;
import oop.ex6.symbol_table.VariableSymbolTable;
import oop.ex6.parser.BadLineException;
import oop.ex6.parser.*;

/**
 * MethodDeclarationVerifier class which inherits from Verifier and is in charge of validating the
 * method declaration logic
 */
public class MethodDeclarationVerifier implements Verifier{

    private final Tokenizer tokenizer;
    private static final String FINAL = "final";
    private static final String RETURN_ERR =
            "The method should have a return statement prior to closing brackets";
    private static final String CLOSING_BRACKET_ERR = "The method has no closing bracket";
    private static final String INNER_METHOD_ERR = "Can't declare a method within a method";

    private static final String SEMICOLON = ";";

    private final VariableSymbolTable localVariableSymbolTable;
    private final VariableSymbolTable globalVariableSymbolTable;
    private final MethodSymbolTable methodSymbolTable;

    /**
     * Constructor for the method declaration verifier
     * @param tokenizer the tokenizer
     * @param localVariableSymbolTable the local variable symbol table
     * @param globalVariableSymbolTable the global variable symbol table
     * @param methodSymbolTable the method symbol table
     * @throws BadLineException if the line is bad
     * @throws BadLogicException if the logic is bad
     */
    public MethodDeclarationVerifier(Tokenizer tokenizer,
                                     VariableSymbolTable localVariableSymbolTable,
                                     VariableSymbolTable globalVariableSymbolTable,
                                     MethodSymbolTable methodSymbolTable) throws BadLineException,
            BadLogicException {
        this.tokenizer = tokenizer;
        this.localVariableSymbolTable = localVariableSymbolTable;
        this.globalVariableSymbolTable = globalVariableSymbolTable;
        this.methodSymbolTable = methodSymbolTable;
        initiateLocalVariables(tokenizer.getCurrentToken().getContent());
        tokenizer.advanceToken();
    }

    /**
     * This method is responsible for verifying the current line
     * @throws BadLogicException - if the logic is bad
     * @throws BadLineException - if the syntax is bad
     */
    @Override
    public void verify() throws BadLogicException, BadLineException {
        Token currToken = tokenizer.getCurrentToken();
        Token prevToken = currToken;
        while (currToken.getType() != Token.TokenType.END_BLOCK){
            if(currToken.getType() == Token.TokenType.METHOD_DECLARATION){
                throw new BadLogicException(INNER_METHOD_ERR);
            }
            tokenizer.step(localVariableSymbolTable, globalVariableSymbolTable,
                    methodSymbolTable, true);
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

    /**
     * This method is responsible for initiating the local variables
     * @param methodDecLine the method declaration line
     * @throws BadLineException if the syntax is bad
     * @throws BadLogicException if the logic is bad
     */
    private void initiateLocalVariables(String methodDecLine) throws BadLineException, BadLogicException {
        String[] varDeclarations = MethodDeclarationParser.getMethodParams(methodDecLine);
        for (String varDec : varDeclarations){
            Token methodVarToken = new Token(varDec + SEMICOLON);
            VariableDeclarationVerifier variableDeclarationVerifier =
                    new VariableDeclarationVerifier(methodVarToken, localVariableSymbolTable,
                            globalVariableSymbolTable);
            if (varDec.trim().startsWith(FINAL)){
                variableDeclarationVerifier.setMethodFlag(true);

            }
            variableDeclarationVerifier.setMethodFlag(true);

            variableDeclarationVerifier.verify();
        }
    }
}
