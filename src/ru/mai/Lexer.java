package ru.mai;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Lexer {
    Set<Integer> acceptingState;
    Map<Integer, String> tokenStates;

    public Lexer() {
        tokenStates = createStateNameToken();
        acceptingState = createSetAcceptingState();
    }

    public Token getNextToken(BufferedReader reader) throws IOException {
        int state = 0;
        int lastAccepting = -1;
        int lastPos = -1;

        reader.mark(1000);

        char currentChar = 0;

        for (int pos = 0; state >= 0; pos++) {
            if (isAcceptingState(state)) {
                lastAccepting = state;
                lastPos = pos;

            }
            currentChar = (char) reader.read();
            state = dfaTransitions(state, currentChar);
        }
        if (lastAccepting >= 0) {
            reader.reset();
            return new Token(
                    getTokenNameForState(lastAccepting),
                    readLexeme(reader, lastPos)
            );
        } else if (currentChar == ' ' || currentChar == '\n') {
            skipSpaces(reader);
            return getNextToken(reader);
        } else if (currentChar == (char) -1) {
            return new Token("eof", "");
        } else {
            throw new RuntimeException("Bad lexeme");
        }
    }

    private void skipSpaces(BufferedReader reader) throws IOException {
        do {
            reader.mark(1);
        } while (((char) reader.read()) == ' ');
        reader.reset();
    }

    private boolean isAcceptingState(int state) {
        return acceptingState.contains(state);
    }

    private String getTokenNameForState(int state) {
        return tokenStates.get(state);
    }

    private Map<Integer, String> createStateNameToken() {
        Map<Integer, String> tokenStates = new HashMap<>();
        tokenStates.put(1, "number");
        tokenStates.put(2, "number");
        tokenStates.put(5, "number");
        tokenStates.put(6, "operator");
        tokenStates.put(7, "id");
        tokenStates.put(8, "lparen");
        tokenStates.put(9, "rparen");
        tokenStates.put(10, "comma");
        return tokenStates;
    }

    private Set<Integer> createSetAcceptingState() {
        return tokenStates.keySet();
    }

    private String readLexeme(BufferedReader reader, int numChars) throws IOException {
        StringBuilder lexeme = new StringBuilder();
        while (reader.ready() && numChars > 0) {
            lexeme.append((char) reader.read());
            numChars--;
        }
        return lexeme.toString();
    }

    private int dfaTransitions(int state, char character) {
        return switch (state) {
            case 0 -> {
                if (character >= 'a' && character <= 'z' || character == '_') {
                    yield 7;
                } else if (character == '(') {
                    yield 8;
                } else if (character == ')') {
                    yield 9;
                } else if (character == ',') {
                    yield 10;
                } else if (
                        character == '+'
                                || character == '*'
                                || character == '/'
                                || character == '^'
                                || character == '-'
                ) {
                    yield 6;
                } else if (character >= '0' && character <= '9') {
                    yield 1;
                } else {
                    yield -1;
                }
            }
            case 1 -> {
                if (character >= '0' && character <= '9') {
                    yield 1;
                } else if (character == '.') {
                    yield 2;
                } else if (character == 'e' || character == 'E') {
                    yield 3;
                } else {
                    yield -1;
                }
            }
            case 2 -> {
                if (character >= '0' && character <= '9') {
                    yield 2;
                } else if (character == 'e' || character == 'E') {
                    yield 3;
                } else {
                    yield -1;
                }
            }
            case 3 -> {
                if (character == '+' || character == '-') {
                    yield 4;
                } else if (character >= '0' && character <= '9') {
                    yield 5;
                } else {
                    yield -1;
                }
            }
            case 4, 5 -> {
                if (character >= '0' && character <= '9') {
                    yield 5;
                } else {
                    yield -1;
                }
            }
            case 7 -> {
                if (character >= 'a' && character <= 'z' || character == '_' || character >= '0' && character <= '9') {
                    yield 7;
                } else {
                    yield -1;
                }
            }
            default -> -1;
        };

    }

}
