package searchengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Main {
  /**
 * The entry point for the search engine.
 * <p>
 * It reads the configuration file and starts the web server on the specified port.
 * </p>
 *
 * @param args Command-line arguments.
 * @throws IOException If there is an error reading the configuration file.
 */
  public static void main(final String... args) throws IOException {
    String filename = Files.readString(Paths.get("config.txt")).strip();
    int PORT = 8080; 
    new WebServer(PORT, filename);
  }
}