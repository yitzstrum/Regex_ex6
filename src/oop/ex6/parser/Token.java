package oop.ex6.parser;


import oop.ex6.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Token {

    private static final String VARIABLE_DECLARATION_REGEX = "\\s*(int|double|char|boolean)\\s";

    private static final String FINAL_VARIABLE_DECLARATION_REGEX =
            "\\s*(final)\\s+(int|double|char|boolean)\\s";

    private static final String VARIABLE_ASSIGNMENT_REGEX = "\\s*[a-zA-Z]\\w*\\s*=\\s*";
    private static final String METHOD_DECLARATION_REGEX = "\\s*void\\s+";
    private static final String BLOCK_END_REGEX = "\\s*}\\s*";
    private static final String IF_WHILE_BLOCK_REGEX = "\\s*(if|while)\\s*";
    private static final String METHOD_CALL_REGEX = "\\s*[a-zA-Z]\\w*\\s*\\(.*\\)\\s*;"; // assume it valid
    private static final String RETURN_STATEMENT_REGEX = "\\s*return\\s*;\\s*"; // assume it valid

    private final TokenType type;
    private final String content;

    public enum TokenType {

        METHOD_DECLARATION,
        END_BLOCK,
        IF_WHILE_BLOCK,
        METHOD_CALL,
        VARIABLE_DECLARATION,
        VARIABLE_ASSIGNMENT,
        FINAL_VARIABLE_DECLARATION,
        RETURN_STATEMENT
    }

    public Token(String line) {
        this.type = getTokenType(line);
        this.content = line;
    }

    private TokenType getTokenType(String line) {
        if (isVariableAssignment(line)) {
            return TokenType.VARIABLE_ASSIGNMENT;
        } else if (isVariableDeclaration(line)) {
            return TokenType.VARIABLE_DECLARATION;
        } else if (isMethodDeclaration(line)) {
            return TokenType.METHOD_DECLARATION;
        } else if (isEndBlock(line)) {
            return TokenType.END_BLOCK;
        } else if (ifIsWhileBlock(line)) {
            return TokenType.IF_WHILE_BLOCK;
        } else if (isMethodCall(line)) {
            return TokenType.METHOD_CALL;
        } else if (isReturnStatement(line)) {
            return TokenType.RETURN_STATEMENT;
        } else if (isFinalVariableDeclaration(line)) {
            return TokenType.FINAL_VARIABLE_DECLARATION;
        } else {
            return null; // cant parse line throw exception later
        }

    }

    private boolean isFinalVariableDeclaration(String line) {
        return Utils.isMatch(line, FINAL_VARIABLE_DECLARATION_REGEX);
    }

    private boolean isVariableDeclaration(String line) {
        return Utils.isMatch(line, VARIABLE_DECLARATION_REGEX) ;
    }

    private boolean isVariableAssignment(String line) {
        return Utils.isMatch(line, VARIABLE_ASSIGNMENT_REGEX);
    }

    private boolean isMethodDeclaration(String line) {
        // method line must start with void
        return Utils.isMatch(line, METHOD_DECLARATION_REGEX);
    }
    
    private boolean isEndBlock(String line) {
        return Utils.isMatch(line, BLOCK_END_REGEX);
    }

    private boolean ifIsWhileBlock(String line) {
        return Utils.isMatch(line, IF_WHILE_BLOCK_REGEX);
    }

    private boolean isMethodCall(String line) {
        return Utils.isMatch(line, METHOD_CALL_REGEX);
    }

    private boolean isReturnStatement(String line) {
        return Utils.isMatch(line, RETURN_STATEMENT_REGEX);
    }



    public TokenType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return  ("Token type: " + type + " content: " + content); // for debug edit it later
    }
}




