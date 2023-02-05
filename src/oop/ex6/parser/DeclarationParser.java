package oop.ex6.parser;

import oop.ex6.symbol_table.VariableData;
import oop.ex6.verifier.BadLogicException;
import oop.ex6.utils.Pair;
import oop.ex6.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DeclarationParser class, inherits from Parser
 */
public class DeclarationParser extends Parser{

    private static final String VAR_RESERVED_WORDS_ERR = "Can't use a reserved name as the variable name";
    private static final String INVALID_TYPE_ERR = "Invalid type";
    private final String TYPE_REGEX = "^" + Parser.TYPE_REGEX;
    private final VariableData.Type type;
    private final String content;
    private final boolean isFinal;
    private final String assigment;

    /**
     * class constructor
     * @param content The files content
     * @param isAssigment boolean value for whether the variable is final
     */
    public DeclarationParser(String content, boolean isAssigment) {
        this.content = content;
        this.isFinal = isAssigment;
        this.type = null;
        this.assigment = content;
    }

    /**
     * class constructor
     * @param content The files content
     * @throws BadLogicException Exception for a syntax error
     */
    public DeclarationParser(String content) throws BadLogicException {
        this.content = content;
        this.isFinal = extractFinal();
        this.type = extractType();
        this.assigment = extractAssigment();
    }

    /**
     * check if the Variable is represented as a final variable
     * @return true if the variable is final, false otherwise
     */
    public boolean getIsFinal() {
        return isFinal;
    }

    /**
     * get the type of the variable
     * @return the type of the variable
     */

    public VariableData.Type getType() {
        return type;
    }

    /**
     * check if line start with final
     * @return true if line start with final, false otherwise
     */
    private boolean extractFinal() {
        // check if line start with final
        return Utils.isStartWith(content, FINAL_PREFIX_REGEX);
    }


    /**
     * parse the assigment part of the line returns string with a pair of the variable name and the value
     * @return list of pairs of the variable name and the value
     * @throws BadLogicException - if the assigment is invalid
     */
    public List<Pair<String, String>> parseAssigment() throws BadLogicException {
        // add syntax check later
        List<Pair<String, String>> variables = new ArrayList<>();
        for (String arg: assigment.split(COMMA)) {
            parseSingleArg(arg, variables);
        }
        return variables;

    }

    /**
     * extract the assigment part of the line
     * @param arg - the argument to parse
     * @param variables - the list of variables to add to
     * @throws BadLogicException - if the assigment is invalid
     */
    private void parseSingleArg(String arg, List<Pair<String, String>> variables) throws BadLogicException {
        String[] regexes = {ASSIGNED_VARIABLE_REGEX, UNASSIGNED_VARIABLE_REGEX};

        for (int i = 0; i < regexes.length; i++) {
            Pattern pattern = Pattern.compile(regexes[i]);
            Matcher matcher = pattern.matcher(arg);
            if (matcher.lookingAt()) {
                arg = matcher.replaceFirst(EMPTY_STRING);
                if (i == 0) {
                    addInitializedVariable(matcher.group(), variables);
                } else {
                    addUninitializedVariable(matcher.group(), variables);
                }
            }
        }

    }

    /**
     * add an uninitialized variable to the list
     * @param arg - the argument to parse
     * @param variables - the list of variables to add to
     * @throws BadLogicException - if the assigment is invalid
     */
    private void addUninitializedVariable(String arg, List<Pair<String, String>> variables)
            throws BadLogicException {
        arg = arg.trim();
        if (arg.matches(RESERVED_WORDS)){
            throw new BadLogicException(VAR_RESERVED_WORDS_ERR);
        }
        Pair<String, String> pair = new Pair<>(arg, null);
        variables.add(pair);
    }

    /**
     * add an initialized variable to the list
     * @param arg - the argument to parse
     * @param variables - the list of variables to add to
     * @throws BadLogicException - if the assigment is invalid
     */
    private void addInitializedVariable(String arg, List<Pair<String, String>> variables) throws
            BadLogicException {
        // remove spaces and then split
        arg = arg.trim();
        String[] split = arg.split(EQUALS);
        Pair<String, String> pair = new Pair<>(split[0].trim(), split[1].trim());
        if(split[0].trim().matches(RESERVED_WORDS)){
            throw new BadLogicException(VAR_RESERVED_WORDS_ERR);
        }
        variables.add(pair);

    }

    /**
     * extract the type of the assigment
     * @return the type of the assigment
     * @throws BadLogicException -
     */
    private VariableData.Type extractType() throws BadLogicException {
        String content = this.content;
        if (isFinal) {
            // remove final prefix
            content = content.replaceFirst(FINAL_PREFIX_REGEX, EMPTY_STRING);
        }
        Pattern pattern = Pattern.compile(TYPE_REGEX);
        Matcher matcher = pattern.matcher(content);
        if (matcher.lookingAt()) {
            String type = matcher.group();
            type = type.trim();
            switch (type) {
                case INT_TYPE:
                    return VariableData.Type.INT;
                case DOUBLE_TYPE:
                    return VariableData.Type.DOUBLE;
                case STRING_TYPE:
                    return VariableData.Type.STRING;
                case BOOLEAN_TYPE:
                    return VariableData.Type.BOOLEAN;
                case CHAR_TYPE:
                    return VariableData.Type.CHAR;
            }
        }
        throw new BadLogicException(INVALID_TYPE_ERR);
    }

    /**
     * extract the assigment part of the line
     * @return the assigment part of the line
     */
    private String extractAssigment() {
        return (content.replaceFirst(FINAL_PREFIX_REGEX, EMPTY_STRING)).replaceFirst
                (TYPE_REGEX, EMPTY_STRING);
    }

}
