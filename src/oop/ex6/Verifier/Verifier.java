package oop.ex6.Verifier;

import oop.ex6.parser.BadLineException;
import oop.ex6.parser.BadLogicException;
import oop.ex6.parser.SJavaException;
import oop.ex6.parser.Token;

public interface Verifier {

    /**
     *
     * @return true if the token was handled, false otherwise
     */
    void verify() throws SJavaException, BadLogicException, BadLineException;

}

