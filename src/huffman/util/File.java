package huffman.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.stream.Collectors;

public class File {

    private final String path;

    public File(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    /**
     * Read a file
     * @return all lines of the file
     */
    public String read() {
    	List<String> lines;
        try {
            FileReader fileReader = new FileReader(getPath());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            lines = bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        String content = "";
        for (String line : lines) {
        	content += line + '\n';
        }
        // remove last line carrier
        content.substring(0, content.length() - 2);
        return content;
    }

    public void write() {}
    
    public String binaryRead() {
    	try (FileInputStream inputStream = new FileInputStream(getPath())) {
            // Lire la taille de la chaîne binaire (en nombre de bits)
    		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4);
    	    // ints are 32 bit, 4 bytes
    	    byteBuffer.put((byte)inputStream.read());
    	    byteBuffer.put((byte)inputStream.read());
    	    byteBuffer.put((byte)inputStream.read());
    	    byteBuffer.put((byte)inputStream.read());
    	    // put the bytes in right order to read int
    	    byteBuffer.flip();
    	    int tailleDeLaChaine = byteBuffer.getInt();

            // Lire les données binaires 
            StringBuilder binaryStringBuilder = new StringBuilder();
            int data;
            while ((data = inputStream.read()) != -1) {
                String binaryByte = String.format("%8s", Integer.toBinaryString(data & 0xFF)).replace(' ', '0');
                binaryStringBuilder.append(binaryByte);
            }

            // Limiter la chaîne binaire à la taille lue
//            String binaryString = binaryStringBuilder.substring(0, tailleDeLaChaine);
            System.out.println(tailleDeLaChaine);
            System.out.println("Contenu binaire du fichier:");
            System.out.println(binaryStringBuilder.toString());
            System.out.println("Lecture du fichier terminée.");
            return binaryStringBuilder.toString();
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage()); 
            return null;
        }
    }
    
    public void binaryWrite(String binaryString) {
        try (FileOutputStream outputStream = new FileOutputStream(getPath())) {
            // Convertir la chaîne binaire en tableau de bytes
            byte[] byteArray = convertBinaryStringToByteArray(binaryString);

            // Écrire la taille de la chaîne binaire (en nombre de bits)
            System.out.println(binaryString.length());
//            outputStream.write(binaryString.length());
            byte[] length = ByteBuffer.allocate(4).putInt(binaryString.length()).array();
            outputStream.write(length);

            // Écrire les données binaires dans le fichier
            outputStream.write(byteArray);

            System.out.println("Données binaires écrites dans le fichier avec succès.");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture des données binaires dans le fichier : " + e.getMessage());
        }
    }
    
    /**
     * Convertie une String en tableau de byte
     * @param binaryString la String à convertir en tableau
     * @return un tableau de byte correspondant à la String
     */
    public static byte[] convertBinaryStringToByteArray(String binaryString) {
        
        int tailleDeLaChaine;
        tailleDeLaChaine= binaryString.length();
        int nbDe0ajouter;
        nbDe0ajouter = tailleDeLaChaine % 8;
        if (nbDe0ajouter != 0) {
            // Ajouter des zéros à la fin pour compléter le dernier octet
            binaryString += "0".repeat(8 - nbDe0ajouter);
            tailleDeLaChaine = binaryString.length();
        }

        int byteLength = tailleDeLaChaine / 8; // Calculer le nombre d'octets nécessaires
        byte[] byteArray = new byte[byteLength];

        // Parcourir la chaîne binaire récupère 8 bits et les convertir en bytes
        for (int i = 0; i < tailleDeLaChaine; i += 8) {
            String byteString = binaryString.substring(i, i + 8);
            byte b = (byte) Integer.parseInt(byteString, 2);
            byteArray[i / 8] = b;
        }
        return byteArray;
        
        
        
    }
}
