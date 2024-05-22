package huffman;

import huffman.util.File;
import huffman.util.Node;

import java.util.*;
import java.util.Map.Entry;

public class Huffman {

    /**
     * Generates a frequency table of each character from a text file
     * @param file the file to read
     * @return a frequency dictionary, sorted in ascending order
     */
    public static LinkedHashMap<Character, Double> countFrequency(File file) {

        HashMap<Character, Double> frequencyMap = new HashMap<>();
        int charNumber = 0;

        // creates a dictionary with the occurrence of each character
        for (String line : file.read()) {
            for (int i = 0; i < line.length(); i++) {

                charNumber++; // count all characters

                Character character = line.charAt(i);
                frequencyMap.put(character, frequencyMap.containsKey(character) ? frequencyMap.get(character) + 1 : 1);
            }
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
    public static Node createTree(LinkedHashMap<Character, Double> frequencyMap) {
        Iterator<Entry<Character, Double>> iterator = frequencyMap.entrySet().iterator();
        Entry<Character, Double> first = iterator.next();
        Entry<Character, Double> second = iterator.next();
        frequencyMap.remove(first.getKey());
        frequencyMap.remove(second.getKey());
        return createTree(new Node(second.getKey(), second.getValue()), new Node(first.getKey(), first.getValue()), frequencyMap);
    }

    private static Node createTree(Node left, Node right, LinkedHashMap<Character, Double> frequencyMap) {

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
    public static Node mergeNode(Node left, Node right) {
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
    public static HashMap<Character, String> prefixCode(Node node) {
        HashMap<Character, String> map = new HashMap<>();
        prefixCode(map, node, "");
        return map;
    }

    private static void prefixCode(HashMap<Character, String> map, Node node, String str) {
        if (node.isLeave()) {
            map.put(node.getValue(), str);
        } else {
            prefixCode(map, node.getLeftNode(), str + "0");
            prefixCode(map, node.getRightNode(), str + "1");
        }
    }

    public static void encode(File file) {
        HashMap<Character, String> map = prefixCode(createTree(countFrequency(file)));

        List<String> lines = file.read();
        ArrayList<String> encodedLines = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++)  {
            encodedLines.add("");
            for (char c : lines.get(i).toCharArray()) {
                encodedLines.set(i, encodedLines.get(i) + map.get(c));
            }
        }
        System.out.println(encodedLines);
    }

    public static void decode(File file, HashMap<Character, String> map) {

        /* CODE TEMPORAIRE */
    	map = prefixCode(createTree(countFrequency(file)));
    	/*******************/
    	
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> encodedLines = new ArrayList<>();
        encodedLines.add("01101011010110001100011000110001100");
        encodedLines.add("011101110111011101110111011101000100010001000100010001000100010001000101010101010101010101010101010101010101");
        encodedLines.add("000000000000000000000000000000000000000000000000000000000000001001001001001001001001001001");
        encodedLines.add("00100100100100100100100100100111111111111111111111111111");
        for (int i = 0; i < encodedLines.size(); i++)  {
            lines.add("");
            String suite = "";
            for (char c : encodedLines.get(i).toCharArray()) {
                suite += c;
                if (map.containsValue(suite)) {
                    lines.set(i, lines.get(i) + getKeyFromValue(map, suite));
                    suite = "";
                }
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
        File file = new File("test.txt");
        var t = countFrequency(file);
        //LinkedHashMap<Character, Double> t = new LinkedHashMap<>();
        //t.put('a', 0.02d);
        //t.put('b', 0.05d);
        //t.put('c', 0.07d);
        //t.put('d', 0.1d);
        //t.put('e', 0.1d);
        //t.put('f', 0.2d);
        //t.put('g', 0.2d);


        t.forEach((cle, valeur) -> System.out.println("Clé : " + cle + ", Valeur : " + valeur));

        Node node = createTree(t);
//
//        System.out.println(node.getFrequency());
//        System.out.println(node.getLeftNode().getFrequency() + " " + node.getRightNode().getFrequency());
//        System.out.println(node.getLeftNode().getLeftNode().getFrequency() + " " + node.getLeftNode().getRightNode().getFrequency());
//
//        System.out.println(node.getRightNode().getLeftNode().getFrequency());
//        System.out.println(node.getRightNode().getLeftNode().getLeftNode().getFrequency() + " " + node.getRightNode().getLeftNode().getRightNode().getFrequency());
//        System.out.println(node.getRightNode().getRightNode().getFrequency());


//        System.out.println(Node.printTree(node));
        HashMap<Character, String> mapp;
        mapp = prefixCode(node);
        System.out.println(mapp);

        //encode(file);
        //decode(file);
    }
}
