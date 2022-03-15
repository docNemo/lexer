package ru.mai;

public class Token {
    private String nameToken;
    private String lexeme;

    public Token(String nameToken, String lexeme) {
        this.lexeme = lexeme;
        this.nameToken = nameToken;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String getNameToken() {
        return nameToken;
    }

    @Override
    public String toString() {
        return "Token{" +
                "nameToken='" + nameToken + '\'' +
                ", lexeme='" + lexeme + '\'' +
                '}';
    }
}
