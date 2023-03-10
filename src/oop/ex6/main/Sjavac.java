package oop.ex6.main;

import oop.ex6.parser.BadLineException;
import oop.ex6.verifier.BadLogicException;
import oop.ex6.parser.*;
import java.io.IOException;

/**
 * Sjavac class, responsible for validating an Sjavac file
 */
public class Sjavac {

    private static final int VALID_ARGS_COUNT = 1;
    private static final int ARG_ONE = 0;
    private static final int SUCCESSFUL_RUN = 0;
    private static final int ILLEGAL_CODE = 1;
    private static final int IO_EXCEPTION = 2;
    private static final String ARGS_ERR_MSG = "ERROR: The number of arguments is invalid";

    /**
     * Main function to run the validation
     * @param args the user input arguments, the first arg is the file path
     */
    public static void main(String[] args) {
        try {
            checkArguments(args);
            Tokenizer tokenizer = new Tokenizer(args[ARG_ONE]);
            tokenizer.run();
            System.out.println(SUCCESSFUL_RUN);
        }
        catch (IOException e) {
            System.out.println(IO_EXCEPTION);
            System.out.println(e.getMessage());
        }
        catch (BadLineException | BadLogicException e) {
            System.out.println(ILLEGAL_CODE);
            System.out.println(e.getMessage());
        }
    }

    /* Checks that the number of arguments is valid */
    private static void checkArguments(String[] args) throws IllegalArgumentException {
        if (args.length != VALID_ARGS_COUNT){
            throw new IllegalArgumentException(ARGS_ERR_MSG);
        }
    }
}
