package ru.mai;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        String pathToInputFile = args[0];

        try (
                FileInputStream inputStream = new FileInputStream(pathToInputFile);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader fileReader = new BufferedReader(inputStreamReader)
        ) {
            Lexer lexer = new Lexer();
            Token token;

            while (fileReader.ready()) {
                token = lexer.getNextToken(fileReader);
                if (token != null) {
                    System.out.println("Token: " + token.getNameToken());
                    System.out.println("Lexeme: " + token.getLexeme());
                    System.out.println("-".repeat(30));
                }
            }
        } catch (IOException e) {
            System.err.println("Error" + e);
        }
    }


}
