package oop.ex6.parser;

import oop.ex6.SymbolTable.MethodSymbolTable;
import oop.ex6.SymbolTable.VariableData;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.Verifier.VariableAssignmentVerifier;
import oop.ex6.Verifier.VariableDeclarationVerifier;
import oop.ex6.Verifier.VerifierManager;
import oop.ex6.exceptions.BadLineException;
import oop.ex6.exceptions.BadLogicException;
import oop.ex6.utils.Pair;

import java.io.IOException;
import java.util.List;

public class Tokenizer {
    private static final String METHOD_OVERLOADING_ERR = "Method overloading is not allowed";
    private static final String RETURN_GLOBAL_ERR = "Return statement is not allowed to appear in global space";
    private static final String METHOD_CALL_ERR = "Method call is not allowed to appear in global space";
    private static final String IF_WHILE_GLOBAL_ERR = "if/while statements are not allowed to appear in global space";
    private static Tokenizer tokenizer = null;
    private Token[] tokens;
    private int curTokenIndex;
    MethodSymbolTable methodSymbolTable;
    VariableSymbolTable globalVariableSymbolTable;

    public Tokenizer(String filePath) throws IOException, BadLineException, BadLogicException {
        methodSymbolTable = new MethodSymbolTable();
        globalVariableSymbolTable = new VariableSymbolTable();
        curTokenIndex = 0;
        FileParser parser = new FileParser(filePath);
        initTokens(parser);
        curTokenIndex = 0;
    }

    public void run() throws BadLogicException, BadLineException {
        while (curTokenIndex < tokens.length){
            step(new VariableSymbolTable(), globalVariableSymbolTable, methodSymbolTable, false);
        }
    }

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
                Pair<String, List<VariableData>> methodData = new MethodDeclarationParser(tokens[i]).getMethodData();
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


    public void step(VariableSymbolTable localVariableSymbolTable,
                     VariableSymbolTable globalVariableSymbolTable,
                     MethodSymbolTable methodSymbolTable, boolean inMethod) throws
            BadLogicException, BadLineException {
        new VerifierManager(this,
                localVariableSymbolTable,
                globalVariableSymbolTable,
                methodSymbolTable, inMethod).verify();
    }

    public boolean hasNext(){
        return curTokenIndex < tokens.length;
    }

    public Token getCurrentToken() {
        return tokens[curTokenIndex];
    }

    public void advanceToken() {
        curTokenIndex++;
    }

}



