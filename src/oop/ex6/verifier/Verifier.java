package oop.ex6.verifier;

import oop.ex6.parser.BadLineException;


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

