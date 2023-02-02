package oop.ex6.SymbolTable;

import java.util.HashMap;

public class VariableSymbolTable extends HashMap<String, VariableData> {

    public void print() {
        System.out.println("Printing VariableSymbolTable");
        for (String key: keySet()) {
            System.out.println(key + " " + get(key));
        }
        System.out.println("End of VariableSymbolTable");
    }
}