package oop.ex6.Verifier;

import oop.ex6.SymbolTable.MethodSymbolTable;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.parser.BadLineException;
import oop.ex6.parser.BadLogicException;
import oop.ex6.parser.SJavaException;
import oop.ex6.parser.Tokenizer;

public class MethodCallVerifier implements Verifier{

    private Tokenizer tokenizer;
    private VariableSymbolTable localVariableSymbolTable;
    private VariableSymbolTable globalVariableSymbolTable;
    private MethodSymbolTable methodSymbolTable;

    public MethodCallVerifier(Tokenizer tokenizer,
                              VariableSymbolTable localVariableSymbolTable,
                              VariableSymbolTable globalVariableSymbolTable,
                              MethodSymbolTable methodSymbolTable) throws BadLineException {
        this.tokenizer = tokenizer;
        this.localVariableSymbolTable = localVariableSymbolTable;
        this.globalVariableSymbolTable = globalVariableSymbolTable;
        this.methodSymbolTable = methodSymbolTable;
    }

    @Override
    public void verify() throws SJavaException, BadLogicException, BadLineException {

    }



}
