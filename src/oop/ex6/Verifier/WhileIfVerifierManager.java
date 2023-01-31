package oop.ex6.Verifier;

import oop.ex6.SymbolTable.MethodSymbolTable;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.parser.*;
import oop.ex6.utils.Utils;

import java.util.Stack;

import static oop.ex6.utils.Utils.BRACKETS_REGEX;
import static oop.ex6.utils.Utils.extractBrackets;

public class WhileIfVerifierManager implements Verifier{


    private static final String IF_WHILE_SYNTAX_REGEX = "\\s*(while|if)\\s*\\(\\s*[^&| )(]+\\s*(" +
            "(&&|\\|\\|)" +
            "\\s*[^|& )" +
            "(]+\\s*)" +
            "*\\)\\s*\\{\\s*$";

    private static final String BAD_IF_WHILE_SYNTAX_MSG = "ERROR: Bad syntax for if/while statement. ";
    private static final String BAD_IF_WHILE_PARAMS_MSG = "ERROR: Bad parameters for if/while statement. ";

    private final Tokenizer tokenizer;
    private VariableSymbolTable localVariableSymbolTable;
    private VariableSymbolTable globalVariableSymbolTable;
    private final MethodSymbolTable methodSymbolTable;



    public WhileIfVerifierManager(Tokenizer tokenizer, VariableSymbolTable localVariableSymbolTable,
                                  VariableSymbolTable globalVariableSymbolTable,
                                  MethodSymbolTable methodSymbolTable){

        this.tokenizer = tokenizer;
        this.localVariableSymbolTable = localVariableSymbolTable;
        this.globalVariableSymbolTable = globalVariableSymbolTable;
        this.methodSymbolTable = methodSymbolTable;

    }

    @Override
    public boolean verify() throws SJavaException {
        // stack of symbol table
        Stack<VariableSymbolTable> stack = new Stack<>();

        do {
        switch (tokenizer.getCurrentToken().getType()) {
                case IF_WHILE_BLOCK:
                    checkStatement();
                    stack.push(localVariableSymbolTable);
                    localVariableSymbolTable = new VariableSymbolTable();
                    tokenizer.advanceToken();
                    break;
                case END_BLOCK:
                    localVariableSymbolTable = stack.pop();
                   tokenizer.advanceToken();
                   break;
                default:
                    new VerifierManager(tokenizer, stack.lastElement(), localVariableSymbolTable,
                     methodSymbolTable).verify();
            }
        } while (stack.size() > 0);

        return true;
    }

    private void checkStatement() throws SJavaException {
        checkTokenIsInRightSyntax();
        validateBrackets();
    }

    private void checkTokenIsInRightSyntax() throws SJavaException {
        if (!Utils.isCompleteMatch(tokenizer.getCurrentToken().getContent(), IF_WHILE_SYNTAX_REGEX)) {
            throw new SJavaException(BAD_IF_WHILE_SYNTAX_MSG) ;
        }
    }

    private void validateBrackets() throws SJavaException {
        String bracketsContent = extractBrackets(tokenizer.getCurrentToken().getContent());
        // split the content by || or && and check each part is valid
        String[] params = bracketsContent.split("(&&|\\|\\|)");
        for (String param : params) {
            if (!(localVariableSymbolTable.containsKey(param) && localVariableSymbolTable.get(param).isInitialized())) {
                throw new BadParamException(BAD_IF_WHILE_PARAMS_MSG);
            }
            }
        }
}
