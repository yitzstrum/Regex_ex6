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

    private Type type;
    private Modifier modifier;

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

    public Type getType() {
        return type;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean equals(VariableData other) {
        return this.type.equals(other.type) && this.modifier == other.modifier;
    }

    public String toString() {
        return "Type: " + type + ", Value: " + modifier;
    }


}
