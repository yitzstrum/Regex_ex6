package oop.ex6.verifier;

import oop.ex6.exceptions.BadLineException;
import oop.ex6.exceptions.BadLogicException;

public interface Verifier {

    void verify() throws BadLogicException, BadLineException;

}

