package huffman;

import huffman.util.File;
import huffman.util.Node;

import java.util.*;
import java.util.Map.Entry;

public class Huffman {
	
	private final File file;
	private HashMap<Character, String> tree;
	
	public Huffman(String path) {
		this.file = new File(path);
		tree = prefixCode(createTree(countFrequency(file)));
	}
	
	public Huffman(String path, HashMap<Character, String> tree) {
		this.file = new File(path);
		this.tree = tree;
	}

    /**
     * Generates a frequency table of each character from a text file
     * @param file the file to read
     * @return a frequency dictionary, sorted in ascending order
     */
    public LinkedHashMap<Character, Double> countFrequency(File file) {

        HashMap<Character, Double> frequencyMap = new HashMap<>();
        int charNumber = 0;

        // creates a dictionary with the occurrence of each character
        String line = file.read();
        for (int i = 0; i < line.length(); i++) {

            charNumber++; // count all characters

            Character character = line.charAt(i);
            frequencyMap.put(character, frequencyMap.containsKey(character) ? frequencyMap.get(character) + 1 : 1);
        }

        // transforms occurrences into frequencies by dividing by the total number of characters
        for (Character character : frequencyMap.keySet()) {
            frequencyMap.put(character, frequencyMap.get(character) / charNumber);
        }

        // sort the hashmap
        LinkedHashMap<Character, Double> sortedMap = new LinkedHashMap<>();
        while (!frequencyMap.isEmpty()) {
            Character key = frequencyMap.keySet().iterator().next(); // get the first key
            double max = 1d; // max frequency

            // gets the minimum frequency
            for (Character character : frequencyMap.keySet()) {
                double frequency = frequencyMap.get(character);
                if (frequency <= max) {
                    key = character;
                    max = frequency;
                }
            }

            frequencyMap.remove(key);
            sortedMap.put(key, max);
        }

        return sortedMap;
    }

    /**
     * Create a Huffman tree from a map of frequencies
     * @param frequencyMap the map of frequencies
     * @return the root node of the Huffman tree
     */
    public Node createTree(LinkedHashMap<Character, Double> frequencyMap) {
        Iterator<Entry<Character, Double>> iterator = frequencyMap.entrySet().iterator();
        Entry<Character, Double> first = iterator.next();
        Entry<Character, Double> second = iterator.next();
        frequencyMap.remove(first.getKey());
        frequencyMap.remove(second.getKey());
        return createTree(new Node(second.getKey(), second.getValue()), new Node(first.getKey(), first.getValue()), frequencyMap);
    }

    private Node createTree(Node left, Node right, LinkedHashMap<Character, Double> frequencyMap) {

        /* stop recursion if there is 1 or 0 element in the list */
        if (frequencyMap.size() <= 1) {
            if (frequencyMap.size() == 0) {
                return mergeNode(left, right);
            } else {
                Entry<Character, Double> last = frequencyMap.entrySet().iterator().next();
                Node node1 = mergeNode(left, right);
                Node node2 = new Node(last.getKey(), last.getValue());
                return node1.getFrequency() < node2.getFrequency() ? mergeNode(node2, node1) : mergeNode(node1, node2);
            }
        }

        Iterator<Entry<Character, Double>> iterator = frequencyMap.entrySet().iterator();
        Entry<Character, Double> first = iterator.next();
        Entry<Character, Double> second = iterator.next();

        Node node1 = mergeNode(left, right);
        frequencyMap.remove(first.getKey());

        Node node2;
        /* if the two values in the list are equal, and one is smaller than the one in the tree, then both are added */
        // if (Objects.equals(first.getValue(), second.getValue()) && first.getValue() < node1.getFrequency()) {
        if (first.getValue() < node1.getFrequency() && second.getValue() < node1.getFrequency()) {
            node2 = mergeNode(new Node(second.getKey(), second.getValue()), new Node(first.getKey(), first.getValue()));
            frequencyMap.remove(second.getKey());
        } else { /* else, the first in the list forms a pair of the two smallest with the one in the tree */
            node2 = new Node(first.getKey(), first.getValue());
        }
        return node1.getFrequency() < node2.getFrequency() ? createTree(node2, node1, frequencyMap) : createTree(node1, node2, frequencyMap);
    }

    /**
     * Combines 2 nodes to form a new one, whose frequency is the sum of the two
     * @param left the left node
     * @param right the right node
     * @return a new node
     */
    public Node mergeNode(Node left, Node right) {
        Node newNode = new Node(null, left.getFrequency() + right.getFrequency());
        newNode.setLeftNode(left);
        newNode.setRightNode(right);
        return newNode;
    }


    public void saveTree() {}
    public void loadTree() {}

    /**
     * Modifies the map passed in parameter so that it contains the character prefix code
     * @param node the root of the tree
     */
    public HashMap<Character, String> prefixCode(Node node) {
        HashMap<Character, String> map = new HashMap<>();
        prefixCode(map, node, "");
        return map;
    }

    private void prefixCode(HashMap<Character, String> map, Node node, String str) {
        if (node.isLeave()) {
            map.put(node.getValue(), str);
        } else {
            prefixCode(map, node.getLeftNode(), str + "0");
            prefixCode(map, node.getRightNode(), str + "1");
        }
    }

    public void encode(File outputFile) {
        String lines = this.file.read();
        String encodedLines = "";
        for (int i = 0; i < lines.length(); i++)  {
        	char c = lines.charAt(i);
            encodedLines += this.tree.get(c);
        }
        
        // write in file
        outputFile.binaryWrite(encodedLines);
        
        System.out.println(encodedLines);
    }

    public void decode() {

        String lines = "";
//        String encodedLines = "00000000000000001000000100010000000000111100011000010000000010100000000001000000100010000011100100000000000000000110110000011001001011000011000000010100001011010100000000011000001000000110000011110101000000011000000000000000011000000000010000000000000100010001100000000010011000000001011000000100000000000000001100001111000110001111000110000101000000011110101001100010000000000011011000010000011000001001000000001010000000000110110100000101101010000001000110101101100100000000000001110000000111101010000001111000011010000000010000000100000000000000100010001110010001000110000000001111000110000100000000101000110001100000011000001000010110001100001001010000000011000001000000110000000001001100000000100001100000100001101010000000111110000000000110000010000000000000000000110110000000010110000000100000001000000000000111110000110110000000001100110000111110000000001000010001101001001100011000010100100010000011100001011010100000000010000001000100000000000000010000000000000001000001111010100110000011110000110100000000000000011000000100000001000000000000000000001010000000000101100110001111100001011010100000010000110000100000000000000000001000110001001010000000110000000000100000000001000000100100000000000100101001000100000111000010110101000000000010000100110001100000001100000000111101000000001000000010000000000000010001011100000000111101010000011000001000110101000000011111000010000000000001001100000000000000000000011001000001100000001010000001101100010011000000110000110000010001100001001010110000101000000000110000010000110000101100100000000000001100000001000000010000000000001101100110100101100011000010100000000001000110000101100100101100000111100011000010100001011001000001101010000000001000001101100011000000111101010011001100010110101000000000001100010011000110000000110000000011110100000001000000000000000010000001000000110000011111000000011110101001100000000011001100001100000100001101010000010000011011000000000000000000001100011110000111000000000000000000000101000000001100000000000000000000010000010000000000010100000001100000000000101100100000000000001100000000000000110000001000000010000000000000000101110000000000110000010000000000000000000110110000000010110010000011100110011000100000000000100000000101000000001100000100000011000001111100011000101011010000010100000000010011001000000001100000000111110110000101000000000011110010000101110000000000000011000000100000000000011001000110000000101000011011000000001000100000000000000000010000011111001000111000000000001111011001000000001010000000000000100010000000000001000110001010110100000101000000001100000100000011001100000000000010001011101100001010000100110011000000111000000000000001100000010000000000001100000000000100010000111001100000000100110000000111110010000001000011100000000000111100000001110011000000000111100000001110001000011100000000000110000110000000000000000000000000010110000000000000000001000000000100010110010000101100000001000000010000000000001100000000000100110010000010000011111000000110011000001111000000010101100011100000000000110000010001100001001100011000010000000010100001000000000000100011000110000000000000010001100001100000111110000000000000100010001110000000000110000010000100110011000010100000000000000110000001000000010000000000001111100000000000000001100000100000000000000000010000010111011010000010100101100000110000000000110001001100000000001100010111001100101000000000010000011001100011000000011111000000011110101000011001000000000001011010000000100000000000000100010001110000000001101100011000010111000010011100000101100000110000000001000000000000000000110000101110110000101001011100000110011000010100000001111100001000000000000100110000110000000110000000011110000111000000000000000000000001000000100000000000000000000110000000000001100110001000000000001010010001000001110000101110010000001000010100110010100000011001100010001111001010000000111101010000000001100100000000000101101000000001000000010000000000000000010110101001100110001011010100000001111100000000010000000000010011000110000101000010000000000001011000001100000000010000000000000000001100000001100000000111100011000010100000001111100000011011000011000000011000000001111000011000000000000001100000010";
        String encodedLines = file.binaryRead();
        String suite = "";
        for (int i = 0; i < encodedLines.length(); i++)  {
            suite += encodedLines.charAt(i);
            if (this.tree.containsValue(suite)) {
                lines += getKeyFromValue(this.tree, suite);
                suite = "";
            }
        }
        System.out.println(lines);
    }

    public static char getKeyFromValue(HashMap<Character, String> map, String value) {
        for (Map.Entry<Character, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
//        File file = new File("test.txt");
////        var t = countFrequency(file);
//        LinkedHashMap<Character, Double> t = new LinkedHashMap<>();
//        t.put('a', 0.02d);
//        t.put('b', 0.05d);
//        t.put('c', 0.07d);
//        t.put('d', 0.1d);
//        t.put('e', 0.1d);
//        t.put('f', 0.2d);
//        t.put('g', 0.2d);
//
//
//        t.forEach((cle, valeur) -> System.out.println("Clé : " + cle + ", Valeur : " + valeur));
//
//        Node node = createTree(t);
//
//        System.out.println(node.getFrequency());
//        System.out.println(node.getLeftNode().getFrequency() + " " + node.getRightNode().getFrequency());
//        System.out.println(node.getLeftNode().getLeftNode().getFrequency() + " " + node.getLeftNode().getRightNode().getFrequency());
//
//        System.out.println(node.getRightNode().getLeftNode().getFrequency());
//        System.out.println(node.getRightNode().getLeftNode().getLeftNode().getFrequency() + " " + node.getRightNode().getLeftNode().getRightNode().getFrequency());
//        System.out.println(node.getRightNode().getRightNode().getFrequency());
//
//
////        System.out.println(Node.printTree(node));
//        HashMap<Character, String> mapp;
//        mapp = prefixCode(node);
//        System.out.println(mapp);
//
//        encode(file);
//        decode(file, null);
    	
    	Huffman testEncode = new Huffman("test.txt");
    	testEncode.encode(new File("test.bin"));
    	
    	Huffman testDecode = new Huffman("test.bin", testEncode.tree);
    	testDecode.decode();
    }
}
