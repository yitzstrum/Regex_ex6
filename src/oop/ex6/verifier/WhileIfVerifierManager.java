package oop.ex6.verifier;

import oop.ex6.symbol_table.MethodSymbolTable;
import oop.ex6.symbol_table.VariableData;
import oop.ex6.symbol_table.VariableSymbolTable;
import oop.ex6.parser.BadLineException;
import oop.ex6.parser.*;
import oop.ex6.utils.Utils;

import java.util.Stack;

import static oop.ex6.utils.Utils.extractBrackets;

public class WhileIfVerifierManager implements Verifier {


    private static final String IF_WHILE_SYNTAX_REGEX = "\\s*(while|if)\\s*\\(\\s*[^&| )(]+\\s*(" +
            "(&&|\\|\\|)" +
            "\\s*[^|& )" +
            "(]+\\s*)" +
            "*\\)\\s*\\{\\s*$";

    private static final String BAD_IF_WHILE_SYNTAX_MSG = "ERROR: Bad syntax for if/while statement. ";
    private static final String BAD_IF_WHILE_PARAMS_MSG = "ERROR: Bad parameters for if/while statement. ";

    private static final String NUMBER_REGEX = "(-?\\d+(\\.\\d+)?)";
    private static final String BOOLEAN_REGEX = "\\s*(true)|(false)" +"|" + NUMBER_REGEX + "\\s*";
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
    public void verify() throws BadLineException, BadLogicException {
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
                    tokenizer.step(localVariableSymbolTable, stack.lastElement(), methodSymbolTable, true);
            }
        } while (stack.size() > 0);
    }

    private void checkStatement(VariableSymbolTable globalSymbolTable) throws BadLogicException {
        checkTokenIsInRightSyntax();
        validateBrackets(globalSymbolTable);
    }

    private void checkTokenIsInRightSyntax() throws BadLogicException {
        if (!Utils.isCompleteMatch(tokenizer.getCurrentToken().getContent(), IF_WHILE_SYNTAX_REGEX)) {
            throw new BadLogicException(BAD_IF_WHILE_SYNTAX_MSG);
        }
    }

    private boolean isVariableBoolean(VariableSymbolTable globalVariableSymbolTable, String param) {
        if (!globalVariableSymbolTable.containsKey(param)) {
            return false;
        }
        VariableData variableData = globalVariableSymbolTable.get(param);
        return variableData.isRepresentBoolean() && variableData.isInitialized();

    }

    private void validateBrackets(VariableSymbolTable globalVariableSymbolTable) throws BadLogicException {
        String bracketsContent = extractBrackets(tokenizer.getCurrentToken().getContent());
        // split the content by || or && and check each part is valid
        String[] params = bracketsContent.split("(&&|\\|\\|)");
        for (String param : params) {
            param = param.trim();
            boolean isBoolean = param.matches(BOOLEAN_REGEX);
            if (!(isBoolean || isVariableBoolean(globalVariableSymbolTable, param))) {
                throw new BadLogicException(tokenizer.getCurrentToken().getContent());
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