package oop.ex6.Verifier;

import oop.ex6.SymbolTable.MethodSymbolTable;
import oop.ex6.SymbolTable.VariableData;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.exceptions.BadLineException;
import oop.ex6.exceptions.BadLogicException;
import oop.ex6.parser.*;
import oop.ex6.utils.Pair;

import java.util.List;

public class MethodCallVerifier implements Verifier{

    private final static String METHOD_DOESNT_EXIST_ERR = "The method you are trying to call doesn't exist";
    private final static String VAR_TYPE_ERR = "The variable types don't match";
    private final static String VARIABLE_COUNT_ERR = "The number of variables sent " +
            "don't match the method variable count";
    private Tokenizer tokenizer;
    private VariableSymbolTable localVariableSymbolTable;
    private VariableSymbolTable globalVariableSymbolTable;
    private MethodSymbolTable methodSymbolTable;

    public MethodCallVerifier(Tokenizer tokenizer,
                              VariableSymbolTable localVariableSymbolTable,
                              VariableSymbolTable globalVariableSymbolTable,
                              MethodSymbolTable methodSymbolTable) {
        this.tokenizer = tokenizer;
        this.localVariableSymbolTable = localVariableSymbolTable;
        this.globalVariableSymbolTable = globalVariableSymbolTable;
        this.methodSymbolTable = methodSymbolTable;
    }

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

    private void verifyValues(String[] methodValues, List<VariableData> variableDataList) throws BadLogicException {
        if (methodValues.length != variableDataList.size()){
            throw new BadLogicException(VARIABLE_COUNT_ERR);
        }
        for (int i = 0; i < methodValues.length; ++i){
            String value = methodValues[i].trim();
            if (localVariableSymbolTable.containsKey(value)){
                if (localVariableSymbolTable.get(value).getType() != variableDataList.get(i).getType()){
                    throw new BadLogicException(VAR_TYPE_ERR);
                }
            }
            else if (globalVariableSymbolTable.containsKey(value)){
                if (globalVariableSymbolTable.get(value).getType() != variableDataList.get(i).getType()){
                    throw new BadLogicException(VAR_TYPE_ERR);
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
