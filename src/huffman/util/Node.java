package huffman.util;

public class Node {

    private final Character value;
    private final double frequency;
    private Node rightNode;
    private Node leftNode;

    /**
     * Create a node
     * @param value the character, can be null
     * @param frequency the frequency of the node
     */
    public Node(Character value, double frequency) {
        this.value = value;
        this.frequency = frequency;
        this.rightNode = null;
        this.leftNode = null;
    }

    /**
     * @return the character of the node (or null)
     */
    public Character getValue() {
        return value;
    }

    /**
     * @return the frequency of the node
     */
    public double getFrequency() {
        return frequency;
    }

    /**
     * Define a right child
     * @param node the child
     */
    public void setRightNode(Node node) {
        this.rightNode = node;
    }

    /**
     * @return the right child of the node
     */
    public Node getRightNode() {
        return rightNode;
    }

    /**
     * Define a left child
     * @param node the child
     */
    public void setLeftNode(Node node) {
        this.leftNode = node;
    }

    /**
     * @return the left child of the node
     */
    public Node getLeftNode() {
        return leftNode;
    }

    /**
     * Determines whether a node is at the end of a branch
     * @return true if node has no children, false otherwise
     */
    public boolean isLeave() {
        return getLeftNode() == null && getRightNode() == null;
    }

    public static String printTree(Node node) {
        if (node == null) {
            return "";
        } else {
            return printTree(node.getLeftNode()) + printTree(node.getRightNode()) + node.getValue();
        }
    }
}
