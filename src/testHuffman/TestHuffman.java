package testHuffman;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import org.junit.jupiter.api.Test;

import huffman.Huffman;
import huffman.util.File;
import huffman.util.Node;

class TestHuffman {
	
	// list of character
	private final List<Character> CHARACTER_LIST = new ArrayList<Character>();
	
	// array of file to test
	private final File[] FILE_TO_TEST = new File[] {
			new File ("Z:\\BUT-INFO-1\\SAE\\SAE 2.02\\Huffman\\src\\testHuffman\\ATester1.txt"), 
			new File ("Z:\\BUT-INFO-1\\SAE\\SAE 2.02\\Huffman\\src\\testHuffman\\ATester2.txt"),
			new File ("Z:\\BUT-INFO-1\\SAE\\SAE 2.02\\Huffman\\src\\testHuffman\\ATester3.txt"),
			new File ("Z:\\BUT-INFO-1\\SAE\\SAE 2.02\\Huffman\\src\\testHuffman\\ATester4.txt")
	};
	
	// array of valid file
	private final File[] VALID_FILE = new File[] {
			new File ("Z:\\BUT-INFO-1\\SAE\\SAE 2.02\\Huffman\\src\\testHuffman\\ValidFile1.txt"),
			new File ("Z:\\BUT-INFO-1\\SAE\\SAE 2.02\\Huffman\\src\\testHuffman\\ValidFile2.txt"),
			new File ("Z:\\BUT-INFO-1\\SAE\\SAE 2.02\\Huffman\\src\\testHuffman\\ValidFile3.txt"),
			new File ("Z:\\BUT-INFO-1\\SAE\\SAE 2.02\\Huffman\\src\\testHuffman\\ValidFile4.txt")
	};
	
	// creation of Huffman tree to test
	private final Huffman TREETOTEST1 = new Huffman("Z:\\BUT-INFO-1\\SAE\\SAE 2.02\\Huffman\\src\\testHuffman\\ATester1.txt");
	private final Huffman TREETOTEST2 = new Huffman("Z:\\BUT-INFO-1\\SAE\\SAE 2.02\\Huffman\\src\\testHuffman\\ATester2.txt");
	private final Huffman TREETOTEST3 = new Huffman("Z:\\BUT-INFO-1\\SAE\\SAE 2.02\\Huffman\\src\\testHuffman\\ATester3.txt");
	private final Huffman TREETOTEST4 = new Huffman("Z:\\BUT-INFO-1\\SAE\\SAE 2.02\\Huffman\\src\\testHuffman\\ATester4.txt");
	
	// creation of valid Huffman tree 
	private final Huffman VALID_TREE1 = new Huffman("Z:\\BUT-INFO-1\\SAE\\SAE 2.02\\Huffman\\src\\testHuffman\\ValidFile1.txt");
	private final Huffman VALID_TREE2 = new Huffman("Z:\\BUT-INFO-1\\SAE\\SAE 2.02\\Huffman\\src\\testHuffman\\ValidFile2.txt");
	private final Huffman VALID_TREE3 = new Huffman("Z:\\BUT-INFO-1\\SAE\\SAE 2.02\\Huffman\\src\\testHuffman\\ValidFile3.txt");
	private final Huffman VALID_TREE4 = new Huffman("Z:\\BUT-INFO-1\\SAE\\SAE 2.02\\Huffman\\src\\testHuffman\\ValidFile4.txt");
	
	// array of valid tree
	private final String[] VERIFY_TREE = new String[] {
			"urnullbt\n"
			+ "nullnullnullanull",
			
			"qpnullj\n"
			+ "nullwnullnulllvnullnull",
			
			"jfnullhk\n"
			+ "nullnullnullgnull",
			
			"vwnullcb\n"
			+ "nullnullnullxnull"
	};
	
	// size of all @FILE_TO_TEST[]
	private final int[] tabSizeToTest = {
			5, 28, 27, 28
	};

	// TODO faire la java-doc de toutes les classes de tests
	
	/**
	 * Test of Huffman.countFrenquency(File)
	 * to verify the size of the map, the number of different characters
	 */
	@Test
	void testNumberOfCharacters() {

     	assertEquals(TREETOTEST1.countFrequency().size(), tabSizeToTest[0]);
     	assertEquals(TREETOTEST2.countFrequency().size(), tabSizeToTest[1]);
     	assertEquals(TREETOTEST3.countFrequency().size(), tabSizeToTest[2]);
     	assertEquals(TREETOTEST4.countFrequency().size(), tabSizeToTest[3]);
	}
	
	/**
	 * Test of Huffman.countFrenquency(File)
	 * to verify if the map is arranged in ascending order
	 */
	@Test
	void testFrequencies() {

		for (Character character : TREETOTEST1.countFrequency().keySet()) {
			 CHARACTER_LIST.add(character);
			 // System.out.println(character);
		}
		
		for (int i = 0; i + 1 < CHARACTER_LIST.size(); i++) {
			assertTrue(lowerOrEquals(TREETOTEST1.countFrequency().get(CHARACTER_LIST.get(i)), 
					                 TREETOTEST1.countFrequency().get(CHARACTER_LIST.get(i+1)))
									 );
		}
	}
	
	/**
	 * Test of Huffman.countFrenquency(File)
	 * to verify if the final frequency is valid
	 */
	@Test
	void testFrequenciesInterval() {

		for (int i = 0; i < CHARACTER_LIST.size(); i++) {
     		assertTrue(lowerOrEquals(TREETOTEST1.countFrequency().get(CHARACTER_LIST.get(i)), 1.0));
     		assertTrue(supOrEquals(TREETOTEST1.countFrequency().get(CHARACTER_LIST.get(i)), 0.0));
     		// System.out.println(validMap.get(characterList.get(i)));
		}
	}
	
	/**
	 * Test of Huffman.countFrenquency(File)
	 * to verify if the arrange map contain same characters of the file
	 */
	@Test
	void testSameMapContent() {
		
		Huffman treeToTest = new Huffman("Z:\\BUT-INFO-1\\SAE\\SAE 2.02\\Huffman\\src\\testHuffman\\ATester1.txt");
		
		List<Character> characterListToTest = new ArrayList<Character>();
		
		treeToTest.countFrequency().forEach((cle, valeur) -> characterListToTest.add(cle));
		
		List<Character> characterList = new ArrayList<Character>();
		
        for (int i = 0; i < FILE_TO_TEST[0].read().length(); i++) {

           Character character = FILE_TO_TEST[0].read().charAt(i);
           
           if (!characterList.contains(character)) {
               characterList.add(character);
           }

        }

		for (int i = 0; i <= treeToTest.countFrequency().size(); i++) {
			assertEquals(new HashSet<>(characterListToTest), new HashSet<>(characterList));
		}
	}
	
	
	
	/**
	 * Compare 2 doubles, if the first is lower or equals to the second
	 * @param double1 first double to compare with second double
	 * @param double2 second double compared with the first double
	 * @return a boolean
	 *         <ul><li>true
	 *                 if double1 lower or equals to double2</li>
	 *             <li>false
	 *                 if double1 strictly superior to double2</li>
	 *         </ul>
	 */
	private boolean lowerOrEquals(Double double1, Double double2) {
		return double1 <= double2 ? true : false;
	}
	
	/**
	 * Compare 2 doubles, if the first is superior or equals to the second
	 * @param double1 first double to compare with second double
	 * @param double2 second double compared with the first double
	 * @return a boolean
	 *         <ul><li>true
	 *                 if double1 superior or equals to double2</li>
	 *             <li>false
	 *                 if double1 strictly lower to double2</li>
	 *         </ul>
	 */
	private boolean supOrEquals(Double double1, Double double2) {
		return double1 <= double2 ? true : false;
	}
	
	/**
	 * Test of Huffman.createTree(LinkedHashMap)
	 * to verify if the Huffman tree has been created correctly
	 */
	@Test
	void testCreateTree() {
		
		assertEquals(VERIFY_TREE[0], Node.printTree(VALID_TREE1.createTree(VALID_TREE1.countFrequency())));
		assertEquals(VERIFY_TREE[1], Node.printTree(VALID_TREE2.createTree(VALID_TREE2.countFrequency())));
		assertEquals(VERIFY_TREE[2], Node.printTree(VALID_TREE3.createTree(VALID_TREE3.countFrequency())));
		assertEquals(VERIFY_TREE[3], Node.printTree(VALID_TREE4.createTree(VALID_TREE4.countFrequency())));
	}

	@Test
	void testEncode() {
		fail("Not yet implemented");
	}
	
	@Test
	void testDecode() {
		fail("Not yet implemented");
	}
	
	@Test
	void testSaveTree() {
		fail("Not yet implemented");
	}

	@Test
	void testLoadTree() {
		fail("Not yet implemented");
	}
}
