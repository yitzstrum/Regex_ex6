package oop.ex6.verifier;

import oop.ex6.exceptions.BadLineException;
import oop.ex6.exceptions.BadLogicException;


/**
 * Interface for all verifiers
 */
public interface Verifier {

    /**
     * Verifies the current line
     * @throws BadLineException - if the line is not valid
     * @throws BadLogicException - if the logic is not valid
     */
    void verify() throws BadLogicException, BadLineException;

}

