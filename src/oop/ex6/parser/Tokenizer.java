package oop.ex6.parser;

import oop.ex6.SymbolTable.MethodSymbolTable;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.Verifier.VerifierManager;

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
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = new Token(fileContent.get(i));
        }
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

    public Token getCurrentToken() {
        return tokens[curTokenIndex];
    }
}



