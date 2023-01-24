package oop.ex6.Verifier;

import oop.ex6.SymbolTable.MethodSymbolTable;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.parser.Token;
import oop.ex6.parser.Tokenizer;

public class WhileIfVerifierManager implements Verifier{

    private final Tokenizer tokenizer;
    private VariableSymbolTable variableSymbolTable;
    private MethodSymbolTable methodSymbolTable;

    public WhileIfVerifierManager(Tokenizer tokenizer, VariableSymbolTable variableSymbolTable){

        this.tokenizer = tokenizer;
        this.variableSymbolTable = variableSymbolTable;


    }

    @Override
    public boolean verify() {
        return false;
    }


//    @Override
//    public boolean verify() {
//
//        Tokenizer tokenizer = Tokenizer.getTokenizer();
//        if (!verifyContent()) {
//            return false;
//        }
//        while (Tokenizer.getTok)
//            step();
//    }
//        return false;
//    }
//
//    private boolean verifyContent() {
//
//        return true;
//    }
}


//while () {
//    int x = 5;
//      int y = 6;
//     if (statement) {
//        }
//        }