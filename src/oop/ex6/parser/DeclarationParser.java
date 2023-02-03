package oop.ex6.parser;

import oop.ex6.SymbolTable.VariableData;
import oop.ex6.utils.Pair;
import oop.ex6.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static oop.ex6.utils.Utils.EMPTY_STRING;

public class DeclarationParser {

    private static final String COMMA = ",";
    private static final String EQUALS = "=";
    private final String TYPE_REGEX = "^\\s*(int|double|String|boolean|char)\\s*";

    private static final String EMPTY_STRING = "";
    private final String VARIABLE_ASSIGMENT_REGEX =
            "((([a-zA-Z]\\w*\\s*)|([a-zA-Z]\\w*\\s*\\=\\s*\\w+))(,\\s*|;$))";
    private static final String UNASSIGNED_VARIABLE_REGEX = "^\\s*[a-zA-Z]\\w*\\s*";
    private static final String ASSIGNED_VARIABLE_REGEX = "^\\s*[a-zA-Z]\\w*\\s*=\\s*\\w+\\s*";
    private final VariableData.Type type;
    private final String content;
    private final String FINAL_PREFIX_REGEX = "^\\s*final\\s*";
    private final boolean isFinal;

    private static final String INT_TYPE = "int";
    private static final String DOUBLE_TYPE = "double";
    private static final String STRING_TYPE = "String";
    private static final String BOOLEAN_TYPE = "boolean";
    private static final String CHAR_TYPE = "char";
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

    public String getAssigment() {
        return assigment;
    }

    private boolean extractFinal() {
        // check if line start with final
        return Utils.isMatch(content, FINAL_PREFIX_REGEX);
    }


    public List<Pair<String, String>> parseAssigment() {
        // add syntax check later

        List<Pair<String, String>> variables = new ArrayList<>();
        for (String arg: assigment.split(COMMA)) {
            parseSingleArg(arg, variables);
        }
        return variables;

    }
    private void parseSingleArg(String arg, List<Pair<String, String>> variables) {
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
    private void addUninitializedVariable(String arg, List<Pair<String, String>> variables) {
        arg = Utils.removeSpaces(arg);
        Pair<String, String> pair = new Pair<>(arg, null);
        variables.add(pair);
    }
    private void addInitializedVariable(String arg, List<Pair<String, String>> variables) {
        // remove spaces and then split
        arg = Utils.removeSpaces(arg);
        String[] split = arg.split(EQUALS);
        Pair<String, String> pair = new Pair<>(split[0], split[1]);
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
            content = matcher.replaceFirst("");
            String type = matcher.group();
            type = Utils.removeSpaces(type);
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
