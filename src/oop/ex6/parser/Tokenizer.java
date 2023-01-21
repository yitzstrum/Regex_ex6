package oop.ex6.parser;

import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.Verifier.VerifierManager;

import java.io.IOException;
import java.util.List;

public class Tokenizer {
    private static Tokenizer tokenizer = null;
    private Token[] tokens;
    private int curTokenIndex;

    private Tokenizer(String filePath) throws IOException {
        curTokenIndex = 0;
        FileParser parser = new FileParser(filePath);
        initTokens(parser);
    }

    public static Tokenizer getTokenizer(String filePath) throws IOException {

        if (tokenizer == null) {
            tokenizer = new Tokenizer(filePath);
        }
        return tokenizer;
    }

    private void initTokens(FileParser parser) {

        List<String> fileContent = parser.getFileContent();
        tokens = new Token[fileContent.size()];
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = new Token(fileContent.get(i));
        }
    }

    public void step(Token token, VariableSymbolTable variableSymbolTable) {
        new VerifierManager(tokens[++curTokenIndex], variableSymbolTable).verify();
    }
    public Token[] getTokens() {
        return tokens;
    }



}
