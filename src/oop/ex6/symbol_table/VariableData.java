package oop.ex6.symbol_table;

/**
 * Variable data class, represents a single variable
 */
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

    /**
     * Constructor for the class
     * @param type The variable type
     * @param modifier The variables modifier
     */
    public VariableData(Type type, Modifier modifier) {
        this.type = type;
        this.modifier = modifier;
    }

    /**
     * The function checks whether the variable is final
     * @return true if final, false otherwise
     */
    public boolean isFinal() {
        return modifier == Modifier.FINAL;
    }

    /**
     * The function checks whether the variable is initialized
     * @return true if initialized, false otherwise
     */
    public boolean isInitialized() {
        return modifier != Modifier.NONE;
    }

    /**
     * Getter function for the variables type
     * @return the variables type
     */
    public Type getType() {
        return type;
    }

    /**
     * Boolean function that checks if the variable is a boolean
     * @return true if the variable represents a boolean, false otherwise
     */
    public boolean isRepresentBoolean() {
        return type == Type.BOOLEAN || type == Type.DOUBLE || type == Type.INT;
    }

    /**
     * The function checks if the variable can be assigned with the given type
     * @param type the type we want to assign to the variable
     * @return true if we can assign the given type to the variable, false otherwise
     */
    public boolean canBeAssignedWith(VariableData.Type type) {
        switch (type) {
            case DOUBLE:
                return this.type == type || this.type == Type.INT;
            case BOOLEAN:
                return this.type == type || this.type == Type.INT || this.type == Type.DOUBLE;
            default:
                return this.type == type;
        }
    }

}
