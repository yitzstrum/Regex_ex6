package oop.ex6.Verifier;

import oop.ex6.SymbolTable.VariableData;
import oop.ex6.SymbolTable.VariableSymbolTable;
import oop.ex6.exceptions.BadLogicException;
import oop.ex6.parser.DeclarationParser;
import oop.ex6.parser.Token;
import oop.ex6.parser.Tokenizer;
import oop.ex6.utils.Pair;

import java.util.List;

    public class VariableAssignmentVerifier implements Verifier{

        private final static String DOESNT_EXIST_ERR = "The variable has not been declared";
        private final VariableSymbolTable variableSymbolTable;
        private final DeclarationParser declarationParser;
        private Tokenizer tokenizer;

        public VariableAssignmentVerifier(Tokenizer tokenizer, VariableSymbolTable variableSymbolTable) {
            this.tokenizer = tokenizer;
            if (tokenizer.getCurrentToken().getType() != Token.TokenType.VARIABLE_ASSIGNMENT) {
                throw new IllegalArgumentException("Token is not a variable assignment");
            }

            this.tokenizer = tokenizer;
            this.variableSymbolTable = variableSymbolTable;
            this.declarationParser = new DeclarationParser(tokenizer.getCurrentToken().getContent(), true);

        }

        public static boolean isValidAssign(VariableData.Type type , String value) {
            if (value == null) {
                return true;
            }
            switch (type) {
                case INT:
                    return isValidInt(value);
                case DOUBLE:
                    return isValidDouble(value);
                case STRING:
                    return isValidString(value);
                case BOOLEAN:
                    return isValidBoolean(value);
                case CHAR:
                    return isValidChar(value);
                default:
                    throw new IllegalArgumentException("Invalid type");
            }
        }

        private static boolean isValidInt(String value) {
            try {
                Integer.parseInt(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        private static boolean isValidDouble(String value) {
            if (isValidInt(value)) {
                return true;
            }
            try {
                Double.parseDouble(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        private static boolean isValidString(String value) {
            return value.startsWith("\"") && value.endsWith("\"");
        }

        private static boolean isValidBoolean(String value) {
            return value.equals("true") || value.equals("false") || isValidDouble(value);
        }

        public static boolean isValidChar(String value) {
            return value.length() == 3 && value.startsWith("'") && value.endsWith("'");
        }


        @Override
        public void verify() throws BadLogicException {
            List<Pair<String, String>> assignments = declarationParser.parseAssigment();
            for (Pair<String, String> assignment : assignments) {
                verifySingleAssigment(assignment);
            }
        }

        public static boolean  isVariableExists(VariableSymbolTable variableSymbolTable, String variableName) {
            return variableSymbolTable.containsKey(variableName);
        }

        private void verifySingleAssigment(Pair<String, String> assignment) throws BadLogicException {
            String variableName = assignment.getFirst();
            String value = assignment.getSecond();
            if (!isVariableExists(variableSymbolTable, variableName)) {
                throw new BadLogicException(DOESNT_EXIST_ERR);
            }
            VariableData variableData = variableSymbolTable.get(variableName);
            isValidAssign(variableData.getType(), value);
        }
    }