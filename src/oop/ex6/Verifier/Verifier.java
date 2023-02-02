package oop.ex6.Verifier;

import oop.ex6.exceptions.BadLineException;
import oop.ex6.exceptions.BadLogicException;
import oop.ex6.parser.SJavaException;

public interface Verifier {

    /**
     *
     * @return true if the token was handled, false otherwise
     */
    void verify() throws SJavaException, BadLogicException, BadLineException;

}

