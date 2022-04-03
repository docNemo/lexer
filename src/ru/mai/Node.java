package ru.mai;

public class Node {
    private final Token token;
    private Node left;
    private Node right;

    public Node(Token token, Node left, Node right) {
        this.token = token;
        this.left = left;
        this.right = right;
    }

    public Node(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public String toString() {
        StringBuilder node = new StringBuilder();
        node.append(token.getLexeme());

        if (left != null || right != null) {
            node.append("(");
            if (left != null) {
                node.append(left);
            }
            if (right != null) {
                node.append(", ").append(right);
            }
            node.append(")");
        }
        return node.toString();
    }
}
