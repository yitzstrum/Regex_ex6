package oop.ex6.parser;

import oop.ex6.SymbolTable.MethodSymbolTable;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.Verifier.VerifierManager;
import oop.ex6.utils.Utils;

import java.io.IOException;
import java.util.List;

public class Tokenizer {
    private static Tokenizer tokenizer = null;
    private Token[] tokens;
    private int curTokenIndex;
    MethodSymbolTable methodSymbolTable;
    VariableSymbolTable variableSymbolTable;

    public Tokenizer(String filePath) throws IOException {
        methodSymbolTable = new MethodSymbolTable();
        variableSymbolTable = new VariableSymbolTable();
        curTokenIndex = 0;
        FileParser parser = new FileParser(filePath);
        initTokens(parser);
    }

    private void initTokens(FileParser parser) {
        List<String> fileContent = parser.getFileContent();
        tokens = new Token[fileContent.size()];
        int bracketsCount = 0;
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = new Token(fileContent.get(i));
            if (bracketsCount == 0 && tokens[i].getType() == Token.TokenType.VARIABLE_DECLARATION){
                insertMethodToSymbolTable(tokens[i]);
            }
            if (lineWithOpenBracket(tokens[i])){
                bracketsCount ++;
            }
            if (tokens[i].getType() == Token.TokenType.END_BLOCK){
                bracketsCount --;
            }
        }
    }

    private void insertMethodToSymbolTable(Token token){
        String methodString = token.getContent();

    }

    private boolean lineWithOpenBracket(Token token){
        return token.getType() == Token.TokenType.METHOD_DECLARATION ||
                token.getType() == Token.TokenType.IF_WHILE_BLOCK;
    }

    public void step(VariableSymbolTable variableSymbolTable, MethodSymbolTable methodSymbolTable) {
        new VerifierManager(this, tokens[++curTokenIndex], variableSymbolTable, methodSymbolTable).verify();
    }

    public MethodSymbolTable getMethodSymbolTable() {
        return methodSymbolTable;
    }

    public Token[] getTokens() {
        return tokens;
    }

}



