package oop.ex6.Verifier;

import oop.ex6.SymbolTable.VariableData;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.parser.DeclarationParser;
import oop.ex6.parser.Token;
import oop.ex6.parser.Tokenizer;
import oop.ex6.utils.Pair;

import java.util.List;

public class VariableDeclarationVerifier implements Verifier {



    private final String VARIABLE_NAME_REGEX = "\\s*[a-zA-Z]\\w+\\s*";

    private final String UNINITIALIZED_VAR_REGEX = "([a-zA-Z]\\w*(;|,))";

    private final String FINAL_PREFIX_REGEX = "\\s*final\\s*";
    private final Tokenizer tokenizer;
    private final VariableSymbolTable variableSymbolTable;
    private final DeclarationParser declarationParser;


    public VariableDeclarationVerifier(Tokenizer tokenizer, VariableSymbolTable variableSymbolTable) {
        if (tokenizer.getCurrentToken().getType() != Token.TokenType.VARIABLE_DECLARATION) {
            throw new IllegalArgumentException("Token is not a variable declaration");
        }
        this.tokenizer = tokenizer;
        this.variableSymbolTable = variableSymbolTable;
        this.declarationParser = new DeclarationParser(tokenizer.getCurrentToken().getContent());;
    }

    public VariableDeclarationVerifier(String decLine, VariableSymbolTable variableSymbolTable){
        tokenizer = null;
        this.variableSymbolTable = variableSymbolTable;
        this.declarationParser = new DeclarationParser(decLine);
        addToTable();
    }

    @Override
    public boolean verify() {
        addToTable();
        return true;
    }
    private void addToTable() {
        List<Pair<String, String>> variables = declarationParser.parseAssigment();;
        for (Pair<String, String> variable: variables) {
            addSingleArgument(variable);
        }
    }

    private void addSingleArgument(Pair<String, String> variable) {
        if (declarationParser.getIsFinal()) {
            addFinalVariable(variable);
        }
        else {
            addNonFinalVariable(variable);
        }
    }
    private void addNonFinalVariable(Pair<String, String> variable) {
        String name = variable.getFirst();
        String value = variable.getSecond();
        VariableData.Type type = declarationParser.getType();
        VariableData variableData;
        if (value == null) {
            variableData = new VariableData(type,  VariableData.Modifier.NONE);
        }
        else {
            variableData = new VariableData(type,  VariableData.Modifier.ASSIGNED);
        }
        if (!VariableAssignmentVerifier.isValidAssign(type, value)) {
            throw new IllegalArgumentException("Invalid assignment");
        }
        variableSymbolTable.put(name, variableData);

    }

    private void addFinalVariable(Pair<String, String> variable) {
        String name = variable.getFirst();
        String value = variable.getSecond();
        if (value == null) {
            // error handle later
        } else {
            VariableData.Type type = declarationParser.getType();
            if (!VariableAssignmentVerifier.isValidAssign(type, value)) {
                throw new IllegalArgumentException("Invalid assignment");
            }
            VariableData variableData = new VariableData(type, VariableData.Modifier.FINAL);
            variableSymbolTable.put(name, variableData);
        }
    }


}