package oop.ex6.verifier;

import oop.ex6.symbol_table.MethodSymbolTable;
import oop.ex6.symbol_table.VariableData;
import oop.ex6.symbol_table.VariableSymbolTable;
import oop.ex6.parser.BadLineException;
import oop.ex6.parser.*;
import oop.ex6.utils.Pair;

import java.util.List;

/**
 * MethodCallVerifier class which inherits from Verifier and is in charge of validating the method call logic
 */
public class MethodCallVerifier implements Verifier{

    private final static String METHOD_DOESNT_EXIST_ERR = "The method you are trying to call doesn't exist";
    private final static String VAR_TYPE_ERR = "The variable types don't match";
    private final static String VAR_NOT_ASSIGNED_ERR = "The variable has not been initialized";
    private final static String VARIABLE_COUNT_ERR = "The number of variables sent " +
            "don't match the method variable count";
    private Tokenizer tokenizer;
    private VariableSymbolTable localVariableSymbolTable;
    private VariableSymbolTable globalVariableSymbolTable;
    private MethodSymbolTable methodSymbolTable;

    /**
     * Constructor for the class
     * @param tokenizer the tokenizer
     * @param localVariableSymbolTable the local variable symbol table
     * @param globalVariableSymbolTable the global variable symbol table
     * @param methodSymbolTable the method symbol table
     */
    public MethodCallVerifier(Tokenizer tokenizer,
                              VariableSymbolTable localVariableSymbolTable,
                              VariableSymbolTable globalVariableSymbolTable,
                              MethodSymbolTable methodSymbolTable) {
        this.tokenizer = tokenizer;
        this.localVariableSymbolTable = localVariableSymbolTable;
        this.globalVariableSymbolTable = globalVariableSymbolTable;
        this.methodSymbolTable = methodSymbolTable;
    }

    /**
     * Verifies that the method call is valid
     * @throws BadLogicException - if the method call is invalid
     * @throws BadLineException  - if the line is invalid
     */
    @Override
    public void verify() throws BadLogicException, BadLineException {
        MethodCallParser methodCallParser = new MethodCallParser(tokenizer.getCurrentToken());
        Pair<String, String[]> methodData = methodCallParser.getMethodData();
        String methodName = methodData.getFirst();
        String[] methodValues = methodData.getSecond();
        if (!methodSymbolTable.containsKey(methodName)){
            throw new BadLogicException(METHOD_DOESNT_EXIST_ERR);
        }
        List<VariableData> variableDataList = methodSymbolTable.get(methodName);
        verifyValues(methodValues, variableDataList);
    }

    /**
     * Verifies that the values sent to the method are valid
     * @param methodValues - the values sent to the method
     * @param variableDataList - the method variable data
     * @throws BadLogicException - if the values are invalid
     */
    private void verifyValues(String[] methodValues, List<VariableData> variableDataList)
            throws BadLogicException {
        if (methodValues.length != variableDataList.size()){
            throw new BadLogicException(VARIABLE_COUNT_ERR);
        }
        for (int i = 0; i < methodValues.length; ++i){
            String value = methodValues[i].trim();
            if (localVariableSymbolTable.containsKey(value)){
                if (localVariableSymbolTable.get(value).getType() != variableDataList.get(i).getType()){
                    throw new BadLogicException(VAR_TYPE_ERR);
                }
                if (!localVariableSymbolTable.get(value).isInitialized()){
                    throw new BadLogicException(VAR_NOT_ASSIGNED_ERR);
                }
            }
            else if (globalVariableSymbolTable.containsKey(value)){
                if (globalVariableSymbolTable.get(value).getType() != variableDataList.get(i).getType()){
                    throw new BadLogicException(VAR_TYPE_ERR);
                }
                if (!globalVariableSymbolTable.get(value).isInitialized()){
                    throw new BadLogicException(VAR_NOT_ASSIGNED_ERR);
                }
            }
            else{
                if (!VariableAssignmentVerifier.isValidAssign(variableDataList.get(i).getType(), value)){
                    throw new BadLogicException(VAR_TYPE_ERR);
                }
            }
        }
    }


}
