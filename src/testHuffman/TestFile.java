package testHuffman;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import huffman.util.File;

class TestFile {
	private final File TEST_WRITE_AND_READ = new File("ATesterWriteRead.txt");
	
	private final File[] VALID_FILE = { 
			new File ("src/testHuffman/ATester1.txt"), 
			new File ("src/testHuffman/ATester2.txt"), 
			new File ("src/testHuffman/ATester3.txt"), 
			new File ("src/testHuffman/ATester4.txt")
	};

	@Test
	void testGetPath() {
		assertEquals("src/testHuffman/ATester1.txt", VALID_FILE[0].getPath());
		assertEquals("src/testHuffman/ATester2.txt", VALID_FILE[1].getPath());
		assertEquals("src/testHuffman/ATester3.txt", VALID_FILE[2].getPath());
		assertEquals("src/testHuffman/ATester4.txt", VALID_FILE[3].getPath());
	}
	
	private final String[] VALID_FILE_CONTAINER = { 
			"azerzeazzearzeaz\n"
		     + "aezraezzaezrae\n"
		     + "zearzearz\n"
		     + "arzeaez\n",
		     
		    "zadghzgdutagf\n"
		     + "azgdzdfyuzjfxzq\n"
		     + "edvgzCJHFZTYSDBYZQETXDBETZX\n",
		     
		    "zdgaydufxevubzefvqzex\n"
		     + "dzavdhfxzyDEXEQ\n"
		     + "DEGWSFVBBEQZXHFZE\n"
		     + "vgdfzvhbyecvq\n",
		     
		     "zdavgxz\n"
			  + "devgazVXDTZ\n"
			  + "DZAFXDYVBZA\n"
			  + "EDGZXYVFZAD£DXFZETVD\n"
			  + "efztyxvtfzeyxc\n"
			  + "ezrcfztbfcyrfe\n"
			  + "exfgebzfczytrfzec\n"
			  + "fcevhstxcfz\n"
	};

	@Test
	void testRead() {
		assertEquals(VALID_FILE_CONTAINER[0], VALID_FILE[0].read());
		assertEquals(VALID_FILE_CONTAINER[1], VALID_FILE[1].read());
		assertEquals(VALID_FILE_CONTAINER[2], VALID_FILE[2].read());
		assertEquals(VALID_FILE_CONTAINER[3], VALID_FILE[3].read());
	}
	
	
	// test qui ne doivent pas passer lorsque l'on essai d'écrire une chaine 
    // qui contien autre choses que des 0 et des 1 
    @Test
    void testWriteFalseCharaters() {
    	
        String binaryString = "010a";
        assertThrows(NumberFormatException.class, () -> TEST_WRITE_AND_READ.binaryWrite(binaryString));
        String binaryString2 = "a";
        assertThrows(NumberFormatException.class, () -> TEST_WRITE_AND_READ.binaryWrite(binaryString2));
        String binaryString3 = "²";
        assertThrows(NumberFormatException.class, () -> TEST_WRITE_AND_READ.binaryWrite(binaryString3));
    }

    // Test qui doivent passer lorsque l'on essai d'écrire une chaine 
    // qui contien que des 0 et des 1 
    @Test
    void testEcritureOk() {

        String binaryString = "010";
        assertDoesNotThrow(() -> TEST_WRITE_AND_READ.binaryWrite(binaryString));
        String binaryString2 = "01001001";
        assertDoesNotThrow(() -> TEST_WRITE_AND_READ.binaryWrite(binaryString2)); 
        String binaryString3 = "";
        assertDoesNotThrow(() -> TEST_WRITE_AND_READ.binaryWrite(binaryString3));
        String binaryString4 = "01010101";
        assertDoesNotThrow(() -> TEST_WRITE_AND_READ.binaryWrite(binaryString4));
    }
    
    // test de lecture d'un fichier qui existe
    @Test
    void readBinaryFileOk() {
        assertDoesNotThrow(() -> TEST_WRITE_AND_READ.binaryRead()); 
    }
    
    // TODO regler problème
    // test de lecture d'un fichier qui n'existe pas
    @Test
    void readBinaryFileNOk() {
        File fileName = new File("chemin/invalide/fichier_inexistant.txt");
        assertThrows(IOException.class, () -> fileName.binaryRead());
        File fileName2 = new File("fichier_qui_n_existe_pas.txt");
        assertThrows(IOException.class, () -> fileName2.binaryRead());
    }
   
    //test pour verifier que la méthode convertBinaryStringToByteArray  
    //renvoie bien un tableau de byte correspondant à la String 
    @Test
    void convertBinaryStringToByteArrayOk() {
        String binaryString = "01010101";
        byte[] expectedByteArray = { 0b01010101 };
        byte[] resultByteArray = TEST_WRITE_AND_READ.convertBinaryStringToByteArray(binaryString);
        assertArrayEquals(expectedByteArray, resultByteArray);
        
        String binaryString2 = "00000";
        byte[] expectedByteArray2 = { 0b00000 };
        byte[] resultByteArray2 = TEST_WRITE_AND_READ.convertBinaryStringToByteArray(binaryString2);
        assertArrayEquals(expectedByteArray2, resultByteArray2);
        
        String binaryString3 = "11111111";
        byte[] expectedByteArray3 = {(byte) 0b11111111 };  
        byte[] resultByteArray3 = TEST_WRITE_AND_READ.convertBinaryStringToByteArray(binaryString3);
        assertArrayEquals(expectedByteArray3, resultByteArray3);
        
        String binaryString4 = "101011";
        byte[] expectedByteArray4 = { (byte) 0b10101100 }; // ajout de 2 0 pour completer à 8
        byte[] resultByteArray4 = TEST_WRITE_AND_READ.convertBinaryStringToByteArray(binaryString4);
        assertArrayEquals(expectedByteArray4, resultByteArray4);
        
        String binaryString5 = "10000";
        byte[] expectedByteArray5 = { (byte) 0b10000000};  // ajout de 3 0 pour completer à 8
        byte[] resultByteArray5 = TEST_WRITE_AND_READ.convertBinaryStringToByteArray(binaryString5);
        assertArrayEquals(expectedByteArray5, resultByteArray5);

        String binaryString6 = "10010101110101001001010111010100100101011101010"
                + "01001010111010100100101011101010010010101110101001001010111"
                + "01010010010101110101001001010111010100100101011101010010010"
                + "10111010100100101011101010010010101110101001001010111010100"
                + "10010101110101001001010111010100";

        byte[] expectedByteArray6 = {
            (byte) 0b10010101, (byte) 0b11010100, (byte) 0b10010101, (byte) 0b11010100,
            (byte) 0b10010101, (byte) 0b11010100, (byte) 0b10010101, (byte) 0b11010100,
            (byte) 0b10010101, (byte) 0b11010100, (byte) 0b10010101, (byte) 0b11010100,
            (byte) 0b10010101, (byte) 0b11010100, (byte) 0b10010101, (byte) 0b11010100, 
            (byte) 0b10010101, (byte) 0b11010100, (byte) 0b10010101, (byte) 0b11010100,
            (byte) 0b10010101, (byte) 0b11010100, (byte) 0b10010101, (byte) 0b11010100,
            (byte) 0b10010101, (byte) 0b11010100, (byte) 0b10010101, (byte) 0b11010100,
            (byte) 0b10010101, (byte) 0b11010100, (byte) 0b10010101, (byte) 0b11010100,
        };

        byte[] resultByteArray6 = TEST_WRITE_AND_READ.convertBinaryStringToByteArray(binaryString6);
        
        assertArrayEquals(expectedByteArray6, resultByteArray6);
    }

    //test pour verifier que la méthode convertBinaryStringToByteArray 
    //ne renvoie pas un tableau de byte correspondant à la String 
    @Test
    void convertBinaryStringToByteArrayNOk() {
        // Définir une chaîne binaire invalide contenant un caractère non binaire
        String invalidBinaryString = "0102";
        // Vérifier que la méthode lève une exception pour une entrée invalide
        assertThrows(NumberFormatException.class, () -> TEST_WRITE_AND_READ.convertBinaryStringToByteArray(invalidBinaryString));
        
        String invalidBinaryString2 = "10111001a";
        // Vérifier que la méthode lève une exception pour une entrée invalide
        assertThrows(NumberFormatException.class, () -> TEST_WRITE_AND_READ.convertBinaryStringToByteArray(invalidBinaryString2));
        
        String invalidBinaryString3 = "010-0101";
        // Vérifier que la méthode lève une exception pour une entrée invalide
        assertThrows(NumberFormatException.class, () -> TEST_WRITE_AND_READ.convertBinaryStringToByteArray(invalidBinaryString3));
        
        String invalidBinaryString4 = "010//0111";
        // Vérifier que la méthode lève une exception pour une entrée invalide
        assertThrows(NumberFormatException.class, () -> TEST_WRITE_AND_READ.convertBinaryStringToByteArray(invalidBinaryString4));
        
        String invalidBinaryString5 = "(010111100100";
        // Vérifier que la méthode lève une exception pour une entrée invalide
        assertThrows(NumberFormatException.class, () -> TEST_WRITE_AND_READ.convertBinaryStringToByteArray(invalidBinaryString5));
        
        String binaryString = "01010111";
        byte[] expectedByteArray = { 0b01010101 };
        byte[] resultByteArray = TEST_WRITE_AND_READ.convertBinaryStringToByteArray(binaryString);
        assertNotEquals(expectedByteArray, resultByteArray);
        
        String binaryString2 = "01010111";
        byte[] expectedByteArray2 = { 0b010101 };
        byte[] resultByteArray2 = TEST_WRITE_AND_READ.convertBinaryStringToByteArray(binaryString);
        assertNotEquals(expectedByteArray2, resultByteArray2);

        String binaryString22 = "00000";
        byte[] expectedByteArray22 = { 0b00100 };
        byte[] resultByteArray22 = TEST_WRITE_AND_READ.convertBinaryStringToByteArray(binaryString22);
        assertNotEquals(expectedByteArray22, resultByteArray22);
        
        String binaryString3 = "11110111";
        byte[] expectedByteArray3 = {(byte) 0b11111111 };  
        byte[] resultByteArray3 = TEST_WRITE_AND_READ.convertBinaryStringToByteArray(binaryString3);
        assertNotEquals(expectedByteArray3, resultByteArray3);
        
        
        String binaryString4 = "1010110000";
        byte[] expectedByteArray4 = { (byte) 0b10101100 }; // ajout de 2 0 pour completer à 8
        byte[] resultByteArray4 = TEST_WRITE_AND_READ.convertBinaryStringToByteArray(binaryString4);
        assertNotEquals(expectedByteArray4, resultByteArray4);
        
        
        String binaryString5 = "100001";
        byte[] expectedByteArray5 = { (byte) 0b10000000};  // ajout de 3 0 pour completer à 8
        byte[] resultByteArray5 = TEST_WRITE_AND_READ.convertBinaryStringToByteArray(binaryString5);
        assertNotEquals(expectedByteArray5, resultByteArray5);

        String binaryString6 = "10010101110101001001010111010100100101011101010"
                + "01001010111010100100101011101010010010101110101001001010111"
                + "010100100101011101010011010111010100100101011101010010010"
                + "10111010100100101011101010010010101110101001001010111010100"
                + "10010101110101001001010111010100";

        byte[] expectedByteArray6 = {
            (byte) 0b10010101, (byte) 0b11010100, (byte) 0b10010101, (byte) 0b11010100,
            (byte) 0b10010101, (byte) 0b11010100, (byte) 0b10010101, (byte) 0b11010100,
            (byte) 0b10011101, (byte) 0b11010100, (byte) 0b10010101, (byte) 0b10010100,
            (byte) 0b10010101, (byte) 0b11010100, (byte) 0b10010101, (byte) 0b11010100,
            (byte) 0b10010101, (byte) 0b11010100, (byte) 0b10010101, (byte) 0b11010100,
            (byte) 0b10110101, (byte) 0b11010100, (byte) 0b10010101, (byte) 0b11010100,
            (byte) 0b10010101, (byte) 0b11010100, (byte) 0b10000101, (byte) 0b11010100,
            (byte) 0b10010101, (byte) 0b11010100, (byte) 0b10010101, (byte) 0b11010100,
        };

        byte[] resultByteArray6 = TEST_WRITE_AND_READ.convertBinaryStringToByteArray(binaryString6);
        
        assertNotEquals(expectedByteArray6, resultByteArray6);
    }
    
    
    // TODO regler problème
    // test qui verifie qu'il est impossible de lire un fichier inexistant
    @Test
    void testReadBinaryFile_NonExistentFile() {
        File fileName = new File("chemin/invalide/fichier_inexistant.txt");
        assertThrows(FileNotFoundException.class, () -> fileName.binaryRead());
        
        File fileName2 = new File("fichier_inexistant.txt");
        assertThrows(FileNotFoundException.class, () -> fileName2.binaryRead());
        
        File fileName3 = new File("");
        assertThrows(FileNotFoundException.class, () -> fileName3.binaryRead());
    }
}

