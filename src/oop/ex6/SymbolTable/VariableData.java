package oop.ex6.SymbolTable;

public class VariableData {

    public enum Type {
        INT,
        DOUBLE,
        STRING,
        CHAR,
        BOOLEAN,
    }


    public enum Modifier {
        FINAL,
        ASSIGNED,
        NONE
    }

    private final Type type;
    private final Modifier modifier;

    public VariableData(Type type, Modifier modifier) {
        this.type = type;
        this.modifier = modifier;
    }

    public boolean isFinal() {
        return modifier == Modifier.FINAL;
    }

    public boolean isInitialized() {
        return modifier != Modifier.NONE;
    }

    public Type getType() {
        return type;
    }

    public boolean isRepresentBoolean() {
        return type == Type.BOOLEAN || type == Type.DOUBLE || type == Type.INT;
    }

    public String toString() {
        return "Type: " + type + ", Value: " + modifier;
    }


}
