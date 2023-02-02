package oop.ex6.Verifier;

import oop.ex6.SymbolTable.MethodSymbolTable;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.parser.*;
import oop.ex6.utils.Utils;

import java.util.Stack;

import static oop.ex6.utils.Utils.BRACKETS_REGEX;
import static oop.ex6.utils.Utils.extractBrackets;

public class WhileIfVerifierManager implements Verifier {


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
                                  MethodSymbolTable methodSymbolTable) {

        this.tokenizer = tokenizer;
        this.localVariableSymbolTable = unionSymbolTables(globalVariableSymbolTable, localVariableSymbolTable);
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
                    if (stack.size() > 0) {
                        localVariableSymbolTable = unionSymbolTables(stack.lastElement(),
                                localVariableSymbolTable);
                    }
                    stack.push(localVariableSymbolTable);
                    checkStatement(stack.lastElement());
                    localVariableSymbolTable = new VariableSymbolTable();
                    tokenizer.advanceToken();
                    break;
                case END_BLOCK:
                    localVariableSymbolTable = stack.pop();
                    tokenizer.advanceToken();
                    break;
                default:
                    tokenizer.step(localVariableSymbolTable, stack.lastElement(), methodSymbolTable);
            }
        } while (stack.size() > 0);

        return true;
    }

    private void checkStatement(VariableSymbolTable globalSymbolTable) throws SJavaException {
        checkTokenIsInRightSyntax();
        validateBrackets(globalSymbolTable);
    }

    private void checkTokenIsInRightSyntax() throws SJavaException {
        if (!Utils.isCompleteMatch(tokenizer.getCurrentToken().getContent(), IF_WHILE_SYNTAX_REGEX)) {
            throw new SJavaException(BAD_IF_WHILE_SYNTAX_MSG);
        }
    }

    private void validateBrackets(VariableSymbolTable globalVariableSymbolTable) throws SJavaException {
        String bracketsContent = extractBrackets(tokenizer.getCurrentToken().getContent());
        // split the content by || or && and check each part is valid
        String[] params = bracketsContent.split("(&&|\\|\\|)");
        for (String param : params) {
            if (!(globalVariableSymbolTable.containsKey(param) && globalVariableSymbolTable.get(param).isInitialized())) {
                System.out.println(param);
                throw new BadParamException(BAD_IF_WHILE_PARAMS_MSG);
            }
        }
    }

    private VariableSymbolTable unionSymbolTables(VariableSymbolTable global, VariableSymbolTable inner) {
        VariableSymbolTable unionTable = new VariableSymbolTable();
        unionTable.putAll(global);
        unionTable.putAll(inner);
        return unionTable;
    }
}



