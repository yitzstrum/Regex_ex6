package oop.ex6.verifier;

import oop.ex6.parser.BadLineException;

public interface Verifier {

    void verify() throws BadLogicException, BadLineException;

}

