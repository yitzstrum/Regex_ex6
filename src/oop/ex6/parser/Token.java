package oop.ex6.parser;


import oop.ex6.utils.Utils;

/**
 * Token class, inherits from Parser
 */
public class Token extends Parser{

    private static final String VARIABLE_ASSIGNMENT_REGEX = "\\s*[a-zA-Z]\\w*\\s*=\\s*";
    private static final String BLOCK_END_REGEX = "\\s*}\\s*";
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

    /**
     * Constructor
     * @param line - the line to parse
     * @throws BadLineException - if the line is not a valid token
     */
    public Token(String line) throws BadLineException {
        this.type = getTokenType(line);
        this.content = line;
    }

    /**
     * get the token type
     * @param line - the line to parse
     * @return - the token type
     * @throws BadLineException - if the line is not a valid token
     */
    private TokenType getTokenType(String line) throws BadLineException {
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
        } else {
            throw new BadLineException(line);
        }

    }

    /**
     * check if the line is a variable declaration
     * @param line - the line to check
     * @return - true if the line is a variable declaration, false otherwise
     */
    private boolean isVariableDeclaration(String line) {
        return line.matches(VARIABLE_DEC) ;
    }

    /**
     * check if the line is a variable assignment
     * @param line - the line to check
     * @return - true if the line is a variable assignment, false otherwise
     */
    private boolean isVariableAssignment(String line) {
        return Utils.isStartWith(line, VARIABLE_ASSIGNMENT_REGEX);
    }


    /**
     * check if the line is a method declaration
     * @param line - the line to check
     * @return - true if the line is a method declaration, false otherwise
     */
    private boolean isMethodDeclaration(String line) {
        return line.matches(METHOD_DEC_REGEX);
    }

    /**
     * check if the line is a block end
     * @param line - the line to check
     * @return - true if the line is a block end, false otherwise
     */
    private boolean isEndBlock(String line) {
        return line.matches(BLOCK_END_REGEX);
    }

    /**
     * check if the line is a if/while block
     * @param line - the line to check
     * @return - true if the line is a if/while block, false otherwise
     */

    private boolean ifIsWhileBlock(String line) {
        return line.matches(IF_WHILE_LINE_REGEX);
    }

    /**
     * check if the line is a method call
     * @param line - the line to check
     * @return - true if the line is a method call, false otherwise
     */
    private boolean isMethodCall(String line) {return line.matches(METHOD_CALL_REGEX);
    }

    /**
     * check if the line is a return statement
     * @param line - the line to check
     * @return - true if the line is a return statement, false otherwise
     */

    private boolean isReturnStatement(String line) {
        return Utils.isStartWith(line, RETURN_STATEMENT_REGEX);
    }

    /**
     * get the token type
     * @return - the token type
     */

    public TokenType getType() {
        return type;
    }

    /**
     * get the token content
     * @return - the token content
     */

    public String getContent() {
        return content;
    }

}