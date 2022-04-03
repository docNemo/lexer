package ru.mai;

import java.io.BufferedReader;
import java.io.IOException;

public class Analyzer {
    private Token token;
    private final Lexer lexer;
    private final BufferedReader reader;

    public Analyzer(BufferedReader reader) throws IOException {
        this.lexer = new Lexer();
        this.reader = reader;
        this.token = lexer.getNextToken(reader);
    }

    public Node analyse() throws Exception {
        return S();
    }

    private Node S() throws Exception {
        Node node = E();
        EOF();
        return node;
    }

    private void EOF() throws Exception {
        if (!token.getNameToken().equals("eof")) {
            throw new Exception("Expected EOF, unexpected " + token.toString());
        }
    }

    public Node E() throws Exception {
        Node node1 = T();
        Node node2 = E_();
        if (node2 != null && node2.getToken().getNameToken().equals("operator")) {
            node2.setLeft(node1);
            node1 = node2;
        }
        return node1;
    }

    private Node E_() throws Exception {
        Node node;
        switch (token.getLexeme()) {
            case "+" -> {
                node = new Node(token);
                nextChar("+");
                node.setRight(E());
            }
            case "-" -> {
                node = new Node(token);
                nextChar("-");
                node.setRight(E());
            }
            default -> node = null;
        }
        return node;
    }

    private Node T() throws Exception {
        Node node1 = F();
        Node node2 = T_();
        if (node2 != null && node2.getToken().getNameToken().equals("operator")) {
            node2.setLeft(node1);
            node1 = node2;
        }
        return node1;
    }

    private Node T_() throws Exception {
        Node node;
        switch (token.getLexeme()) {
            case "*" -> {
                node = new Node(token);
                nextChar("*");
                node.setRight(T());
            }
            case "/" -> {
                node = new Node(token);
                nextChar("/");
                node.setRight(T());
            }
            default -> node = null;
        }
        return node;
    }

    private Node F() throws Exception {
        Node node1 = V();
        Node node2 = F_();
        if (node2 != null && node2.getToken().getNameToken().equals("operator")) {
            node2.setLeft(node1);
            node1 = node2;
        }
        return node1;
    }

    private Node F_() throws Exception {
        if (!"^".equals(token.getLexeme())) {
            return null;
        }
        Token currToken = token;
        nextChar("^");
        return new Node(currToken, null, F());
    }

    private Node V() throws Exception {
        Node node;
        switch (token.getNameToken()) {
            case "lparen" -> {
                nextChar("(");
                node = E();
                nextChar(")");
            }
            case "id", "number" -> {
                node = new Node(token);
                nextChar(token.getLexeme());
            }
            case "operator" -> {
                node = new Node(token);
                nextChar("-");
                node.setLeft(V());
            }
            default -> throw new Exception("Unexpected terminal: " + token.toString());
        }
        return node;
    }

    private void nextChar(String currChar) throws Exception {
        if (token.getLexeme().equals(currChar)) {
            token = lexer.getNextToken(reader);
        } else {
            throw new Exception("Unexpected " + token.toString());
        }
    }
}
