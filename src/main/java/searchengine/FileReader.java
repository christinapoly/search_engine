package searchengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class for reading the contents of a file.
 */
public class FileReader {

    /**
     * Reads the contents of a file and returns it as a byte array.
     *
     * @param filename the path to the file to be read
     * @return a byte array containing the contents of the file,
     * or an empty byte array if the file cannot be read
     */
    public static byte[] getFile(String filename) {
    try {
      return Files.readAllBytes(Paths.get(filename));
    } catch (IOException e) {
      e.printStackTrace();
      return new byte[0];
    }
  }
}
