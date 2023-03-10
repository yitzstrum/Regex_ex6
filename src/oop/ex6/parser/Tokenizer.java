package oop.ex6.parser;

import oop.ex6.symbol_table.MethodSymbolTable;
import oop.ex6.symbol_table.VariableData;
import oop.ex6.symbol_table.VariableSymbolTable;
import oop.ex6.verifier.VariableAssignmentVerifier;
import oop.ex6.verifier.VariableDeclarationVerifier;
import oop.ex6.verifier.VerifierManager;
import oop.ex6.verifier.BadLogicException;
import oop.ex6.utils.Pair;

import java.io.IOException;
import java.util.List;

/**
 * Tokenizer class, in charge of running the programs validation
 */
public class Tokenizer {
    private static final String METHOD_OVERLOADING_ERR =
            "Method overloading is not allowed";
    private static final String RETURN_GLOBAL_ERR =
            "Return statement is not allowed to appear in global space";
    private static final String METHOD_CALL_ERR =
            "Method call is not allowed to appear in global space";
    private static final String IF_WHILE_GLOBAL_ERR =
            "if/while statements are not allowed to appear in global space";
    private Token[] tokens;
    private int curTokenIndex;
    MethodSymbolTable methodSymbolTable;
    VariableSymbolTable globalVariableSymbolTable;

    /**
     * constructor for the class, initiates the preprocessing
     * @param filePath The file path which we want to validate
     * @throws IOException Exception for I/O error
     * @throws BadLineException Exception for syntax errors
     * @throws BadLogicException Exception for logic errors
     */
    public Tokenizer(String filePath) throws IOException, BadLineException, BadLogicException {
        methodSymbolTable = new MethodSymbolTable();
        globalVariableSymbolTable = new VariableSymbolTable();
        curTokenIndex = 0;
        FileParser parser = new FileParser(filePath);
        initTokens(parser);
        curTokenIndex = 0;
    }

    /**
     * The function runs the validation on the input
     * @throws BadLineException Exception for syntax errors
     * @throws BadLogicException Exception for logic errors
     */
    public void run() throws BadLogicException, BadLineException {
        while (curTokenIndex < tokens.length){
            step(new VariableSymbolTable(), globalVariableSymbolTable, methodSymbolTable, false);
        }
    }

    /**
     * The function runs the preprocessing
     * @param parser The parser for the file
     * @throws BadLineException Exception for syntax errors
     * @throws BadLogicException Exception for logic errors
     */
    private void initTokens(FileParser parser) throws BadLineException, BadLogicException {
        List<String> fileContent = parser.getFileContent();
        tokens = new Token[fileContent.size()];
        int bracketsCount = 0;
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = new Token(fileContent.get(i));
            if (bracketsCount == 0 && tokens[i].getType() == Token.TokenType.VARIABLE_DECLARATION){
                new VariableDeclarationVerifier(getCurrentToken(), globalVariableSymbolTable,
                        new VariableSymbolTable()).verify();
            }
            else if (bracketsCount == 0 && tokens[i].getType() == Token.TokenType.VARIABLE_ASSIGNMENT){
                new VariableAssignmentVerifier(this, globalVariableSymbolTable,
                        new VariableSymbolTable(), false).verify();
            }
            else if (tokens[i].getType() == Token.TokenType.METHOD_DECLARATION){
                Pair<String, List<VariableData>> methodData = new MethodDeclarationParser(tokens[i]).
                        getMethodData();
                if (methodSymbolTable.containsKey(methodData.getFirst())){
                    throw new BadLogicException(METHOD_OVERLOADING_ERR);
                }
                methodSymbolTable.put(methodData.getFirst(), methodData.getSecond());
                bracketsCount ++;
            }
            else if (tokens[i].getType() == Token.TokenType.IF_WHILE_BLOCK) {
                if (bracketsCount == 0){
                    throw new BadLogicException(IF_WHILE_GLOBAL_ERR);
                }
                bracketsCount ++;
            }
            else if (tokens[i].getType() == Token.TokenType.END_BLOCK){
                bracketsCount --;
            }
            else if (tokens[i].getType() == Token.TokenType.RETURN_STATEMENT && bracketsCount == 0){
                throw new BadLogicException(RETURN_GLOBAL_ERR);
            }
            else if (tokens[i].getType() == Token.TokenType.METHOD_CALL && bracketsCount == 0){
                throw new BadLogicException(METHOD_CALL_ERR);
            }
            curTokenIndex ++;
        }
    }

    /**
     * This method is responsible for verifying the current line
     * @param localVariableSymbolTable - the local variable symbol table
     * @param globalVariableSymbolTable - the global variable symbol table
     * @param methodSymbolTable - the method symbol table
     * @param inMethod - if we are in a method
     * @throws BadLogicException - if the logic is bad
     * @throws BadLineException - if the line is bad
     */
    public void step(VariableSymbolTable localVariableSymbolTable,
                     VariableSymbolTable globalVariableSymbolTable,
                     MethodSymbolTable methodSymbolTable, boolean inMethod) throws
            BadLogicException, BadLineException {
        new VerifierManager(this,
                localVariableSymbolTable,
                globalVariableSymbolTable,
                methodSymbolTable, inMethod).verify();
    }

    /**
     * checks if there is a next token
     * @return the next token
     */
    public boolean hasNext(){
        return curTokenIndex < tokens.length;
    }

    /**
     * @return the current token
     */
    public Token getCurrentToken() {
        return tokens[curTokenIndex];
    }

    /**
     * advances the token index
     */
    public void advanceToken() {
        curTokenIndex++;
    }

}



