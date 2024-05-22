package huffman.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public List<String> read() {
        try {
            FileReader fileReader = new FileReader(getPath());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            return bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write() {}

}
