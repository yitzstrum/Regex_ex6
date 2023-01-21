package oop.ex6.main;

import oop.ex6.parser.FileParser;
import oop.ex6.parser.Token;
import oop.ex6.parser.Tokenizer;

import java.io.IOException;

public class Sjavac {

    public static void main(String[] args) {

        try {
            Tokenizer parser = Tokenizer.getTokenizer("C:\\Users\\danie\\IdeaProjects\\RegexEx6\\src\\oop\\ex6" +
                    "\\main" +
                    "\\test" +
                    ".txt");
            for (Token token : parser.getTokens()) {
                System.out.println(token);
            }

        }
        catch (IOException e) {
            System.out.println("error"); // change later
        }
    }
}
