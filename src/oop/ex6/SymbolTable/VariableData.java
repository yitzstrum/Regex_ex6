package oop.ex6.SymbolTable;

public class VariableData {

    public enum Type {
        INT,
        DOUBLE,
        STRING,
        CHAR,
        BOOLEAN
    }

    enum Modifier {
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

    public boolean isAssigned() {
        return modifier == Modifier.ASSIGNED;
    }

    private boolean isInitialized() {
        return modifier != Modifier.NONE;
    }

    public Type getType() {
        return type;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public boolean equals(VariableData other) {
        return this.type.equals(other.type) && this.modifier == other.modifier;
    }

    public String toString() {
        return "Type: " + type + ", Value: " + modifier;
    }


}
