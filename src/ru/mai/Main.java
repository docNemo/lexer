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
            Analyzer analyzer = new Analyzer(fileReader);
            Node tree = analyzer.analyse();

            System.out.println(tree);
            System.out.println(calculateTree(tree));

        } catch (IOException e) {
            System.err.println("Error" + e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double calculateTree(Node tree) throws Exception {
        double result = 0;
        double left = 0;
        double right = 0;

        if (tree.getLeft() != null) {
            left = calculateTree(tree.getLeft());
        } else {
            result = Double.parseDouble(tree.getToken().getLexeme());
        }

        if (tree.getRight() != null) {
            right = calculateTree(tree.getRight());
        }

        if (tree.getToken().getNameToken().equals("operator")) {
            result = switch (tree.getToken().getLexeme()) {
                case "+" -> left + right;
                case "-" -> {
                    if (tree.getRight() != null) {
                        yield  left - right;
                    } else {
                        yield -left;
                    }
                }
                case "*" -> left * right;
                case "/" -> left / right;
                case "^" -> Math.pow(left, right);
                default -> throw new Exception("Bad tree: " + tree + ", token: " + tree.getToken());
            };
        }
        return result;
    }


}
