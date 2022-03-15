package ru.mai;

import java.io.BufferedReader;
import java.io.IOException;

public class Analyzer {
    private Token token;
    private Lexer lexer;
    private BufferedReader reader;

    public Analyzer(BufferedReader reader) throws IOException {
        this.lexer = new Lexer();
        this.reader = reader;
        this.token = lexer.getNextToken(reader);
    }

    public void analyse() throws Exception {
        S();
    }

    private void S() throws Exception {
        E();
        EOF();
    }

    private void EOF() throws Exception {
        if (!token.getNameToken().equals("eof")) {
            throw new Exception("Expected EOF, unexpected " + token.toString());
        }
    }

    public void E() throws Exception {
        T(); E_();
    }

    private void E_() throws Exception {
        switch (token.getLexeme()) {
            case "+" -> {
                nextChar("+");
                T(); E_();
            }
            case "-" -> {
                nextChar("-");
                T(); E_();
            }
            default -> {
            }
        }
    }

    private void T() throws Exception {
        F(); T_();
    }

    private void T_() throws Exception {
        switch (token.getLexeme()) {
            case "*" -> {
                nextChar("*");
                F(); T_();
            }
            case "/" -> {
                nextChar("/");
                F(); T_();
            }
            default -> {
            }
        }
    }

    private void F() throws Exception {
        V(); F_();
    }

    private void F_() throws Exception {
        switch (token.getLexeme()) {
            case "^" -> {
                nextChar("^");
                F();
            }
            default -> {
            }
        }
    }

    private void V() throws Exception {
        switch (token.getNameToken()) {
            case "lparen" -> {
                nextChar("(");
                E();
                nextChar(")");
            }
            case "id" -> {
                nextChar(token.getLexeme());
            }
            case "number" -> nextChar(token.getLexeme());
            case "operator" -> nextChar("-");
            default -> throw new Exception("Unexpected terminal: " + token.toString());
        }
    }

    private void nextChar(String currChar) throws Exception {
        if (token.getLexeme().equals(currChar)) {
            token = lexer.getNextToken(reader);
        }
        else {
            throw new Exception("Unexpected " + token.toString());
        }
    }
}
