package oop.ex6.parser;

import oop.ex6.SymbolTable.VariableData;
import oop.ex6.exceptions.BadLogicException;
import oop.ex6.utils.Pair;
import oop.ex6.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeclarationParser extends Parser{

    private static final String VAR_RESERVED_WORDS_ERR = "Can't use a reserved name as the variable name";
    private final String TYPE_REGEX = "^\\s*(int|double|String|boolean|char)\\s*";

    private final VariableData.Type type;
    private final String content;
    private final boolean isFinal;
    private final String assigment;

    public DeclarationParser(String content, boolean isAssigment) {
        this.content = content;
        this.isFinal = false;
        this.type = null;
        this.assigment = extractAssigment();
    }

    public DeclarationParser(String content) throws BadLogicException {
        this.content = content;
        this.isFinal = extractFinal();
        this.type = extractType();
        this.assigment = extractAssigment();
    }

    public boolean getIsFinal() {
        return isFinal;
    }

    public VariableData.Type getType() {
        return type;
    }

    private boolean extractFinal() {
        // check if line start with final
        return Utils.isMatch(content, FINAL_PREFIX_REGEX);
    }


    public List<Pair<String, String>> parseAssigment() throws BadLogicException {
        // add syntax check later
        List<Pair<String, String>> variables = new ArrayList<>();
        for (String arg: assigment.split(COMMA)) {
            parseSingleArg(arg, variables);
        }
        return variables;

    }
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
    private void addUninitializedVariable(String arg, List<Pair<String, String>> variables) throws BadLogicException {
        arg = Utils.removeSpaces(arg);
        if (arg.matches(RESERVED_WORDS)){
            throw new BadLogicException(VAR_RESERVED_WORDS_ERR);
        }
        Pair<String, String> pair = new Pair<>(arg, null);
        variables.add(pair);
    }
    private void addInitializedVariable(String arg, List<Pair<String, String>> variables) throws BadLogicException {
        // remove spaces and then split
        arg = Utils.removeSpaces(arg);
        String[] split = arg.split(EQUALS);
        Pair<String, String> pair = new Pair<>(split[0], split[1]);
        if(split[0].matches(RESERVED_WORDS)){
            throw new BadLogicException(VAR_RESERVED_WORDS_ERR);
        }
        variables.add(pair);

    }

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
        throw new BadLogicException("Invalid type");
    }


    private String extractAssigment() {
        return (content.replaceFirst(FINAL_PREFIX_REGEX, EMPTY_STRING)).replaceFirst(TYPE_REGEX, EMPTY_STRING);
    }

    // for debug
    @Override
    public String toString() {
        return "DeclarationParser{" +
                ", content='" + content + '\'' +
                "type=" + type +
                ", isFinal=" + isFinal +
                ", assigment='" + assigment + '\'' +
                '}';
    }
}
