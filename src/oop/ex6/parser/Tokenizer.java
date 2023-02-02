package oop.ex6.parser;

import oop.ex6.SymbolTable.MethodSymbolTable;
import oop.ex6.SymbolTable.VariableData;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.Verifier.VariableDeclarationVerifier;
import oop.ex6.Verifier.VerifierManager;
import oop.ex6.utils.Pair;

import java.io.IOException;
import java.util.List;

public class Tokenizer {
    private static Tokenizer tokenizer = null;
    private Token[] tokens;
    private int curTokenIndex;
    MethodSymbolTable methodSymbolTable;
    VariableSymbolTable globalVariableSymbolTable;

    public Tokenizer(String filePath) throws IOException, BadLineException {
        methodSymbolTable = new MethodSymbolTable();
        globalVariableSymbolTable = new VariableSymbolTable();
        curTokenIndex = 0;
        FileParser parser = new FileParser(filePath);
        initTokens(parser);
        curTokenIndex = 0;
    }

    public void run() throws SJavaException, BadLogicException, BadLineException {
        while (curTokenIndex < tokens.length){
            step(new VariableSymbolTable(), globalVariableSymbolTable, methodSymbolTable);
        }
    }

    private void initTokens(FileParser parser) throws BadLineException {
        List<String> fileContent = parser.getFileContent();
        tokens = new Token[fileContent.size()];
        int bracketsCount = 0;
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = new Token(fileContent.get(i));
            if (bracketsCount == 0 && tokens[i].getType() == Token.TokenType.VARIABLE_DECLARATION){
                new VariableDeclarationVerifier(getCurrentToken(), globalVariableSymbolTable).verify();
            }
            if (tokens[i].getType() == Token.TokenType.METHOD_DECLARATION){
                Pair<String, List<VariableData>> methodData = new MethodDeclarationParser(tokens[i]).getMethodData();
                methodSymbolTable.put(methodData.getFirst(), methodData.getSecond());
                bracketsCount ++;
            }
            if (tokens[i].getType() == Token.TokenType.IF_WHILE_BLOCK) {
                bracketsCount ++;
            }
            if (tokens[i].getType() == Token.TokenType.END_BLOCK){
                bracketsCount --;
            }
            curTokenIndex ++;
        }
    }


    public void step(VariableSymbolTable localVariableSymbolTable,
                     VariableSymbolTable globalVariableSymbolTable, MethodSymbolTable methodSymbolTable) throws SJavaException, BadLogicException, BadLineException {
        new VerifierManager(this,
                localVariableSymbolTable,
                globalVariableSymbolTable,
                methodSymbolTable).verify();
        advanceToken();
    }

    public MethodSymbolTable getMethodSymbolTable() {
        return methodSymbolTable;
    }

    public Token[] getTokens() {
        return tokens;
    }


    public Token getCurrentToken() {
        return tokens[curTokenIndex];
    }

    public void advanceToken() {
        curTokenIndex++;
    }

}



