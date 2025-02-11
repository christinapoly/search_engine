package searchengine;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;


/**
 * Sets up and runs an HTTP server and provides a search engine for indexed pages.
 * <p>
 * The WebServer class sets up an HTTP server that listens on a port and handles user's search requests. It allows pages to be loaded from a file and then queried through the search engine.
 * </p>
 */
public class WebServer {
  static final int BACKLOG = 0;
  static final Charset CHARSET = StandardCharsets.UTF_8;

  List<List<String>> pages = new ArrayList<>();
  HttpServer server;
  private final SearchEngine searchEngine;
  private final TermScorer termScorer;


  /**
 * Constructor: Initializes a new web server with the specified port and loads a search engine.
 * 
 * @param port the number of the port on which the server will listen
 * @param filename the name of the file that contains the webpages that the search engine is going to load.
 * @throws IOException if there is an issue reading the specified file or starting the server. 
 */
  WebServer(int port, String filename) throws IOException {
    // Prep the objects
    searchEngine = new InvertedIndexSearchEngine();
    searchEngine.loadPages(filename);

    // By default, we use the Term Frequency scorer
    // Change it to new TFIDScorer() to use the TFIDF scorer
    termScorer = new TermFrequencyScorer();
    termScorer.loadPages(searchEngine.getPages());

    // Start the server
    server = HttpServer.create(new InetSocketAddress(port), BACKLOG);
    createContexts();     
    
  }


  /**
   * This method starts the server by defining routes (contexts) for handling HTTP requests.
   * <p> 
   * The method configures several endpoints (contexts) for the server, associates each with specific request handlers, and then starts the server. After starting, it logs the server address and port to the console.
   * </p>
   * 
   * <ul>
   * <li>"/" - Serves the homepage (HTML file).</li>
   * <li>"/search" - Handles search requests with the {@code search(io)} method.</li>
   * <li>"/favicon.ico" - Serves the favicon (browser icon).</li>
   * <li>"/code.js" - Serves JavaScript file for functionality.</li>
   * <li>"/style.css" - Serves CSS files for styling the pages.</li>
   * </ul>
   * 
   * The method also prints a message to the console when the server is running, indicating the server's address and port, styled with box-drawing characters.
   */
  private void createContexts() {
    server.createContext("/", io -> respond(io, 200, "text/html", FileReader.getFile("web/index.html")));
    server.createContext("/search", io -> search(io));
    server.createContext("/favicon.ico", io -> respond(io, 200, "image/x-icon", FileReader.getFile("web/favicon.ico")));
    server.createContext("/code.js", io -> respond(io, 200, "application/javascript", FileReader.getFile("web/code.js")));
    server.createContext("/style.css", io -> respond(io, 200, "text/css", FileReader.getFile("web/style.css")));
    server.start();
    String msg = " WebServer running on http://localhost:" + server.getAddress().getPort() + " ";
    System.out.println("╭"+"─".repeat(msg.length())+"╮");
    System.out.println("│"+msg+"│");
    System.out.println("╰"+"─".repeat(msg.length())+"╯");
  }
  
  /**
 * Handles HTTP requests and returns the results in JSON format.
 * <p>
 * This method processes incoming search requests by extracting the query parameter from the URI. It then uses the {@link QueryHandler} to search for matching web pages in the search engine. The results are formatted as a list of JSON objects containing the URLs and titles of the matching pages, and the response is sent back to the client in JSON format.
 * </p>
 * 
 * @param io the HTTP exchange object that contains the request and allows sending the response.
 */
  void search(HttpExchange io) {
    // Extract the query from the URI
    String query = io.getRequestURI().getRawQuery().split("=")[1];
    QueryHandler queryHandler = new QueryHandler(searchEngine, termScorer);

    // Get the matching web pages
    List<String> results = queryHandler.getMatchingWebPages(query);
    List<String> response = new ArrayList<>();

    // Format the results
    for (String page : results) {
        String url = page.split(" - ")[0];
        String title = page.split(" - ")[1];
        response.add(String.format("{\"url\": \"%s\", \"title\": \"%s\"}", url, title));
    }

    byte[] bytes = response.toString().getBytes(CHARSET);
    respond(io, 200, "application/json", bytes);
}


  /**
 * This method sends an HTTP response to the client.
 * <p>
 * It sets the response headers, including the "Content-Type" header and the character set (UTF-8). It then sends the response with a specified status code and body content. The response body is written to the client, and the connection is closed afterward.
 * </p>
 *
 * @param io the HTTP exchange object that contains the request and allows sending the response.
 * @param code the HTTP status code to send in the response (e.g. 200 for OK).
 * @param mime the content type of the response.
 * @param response the response body as a byte array, which will be sent to the client.
 * @throws IOException if an error occurs while sending the response.
 */
  void respond(HttpExchange io, int code, String mime, byte[] response) {
    try {
      io.getResponseHeaders()
          .set("Content-Type", String.format("%s; charset=%s", mime, CHARSET.name()));
      io.sendResponseHeaders(200, response.length);
      io.getResponseBody().write(response);
    } catch (Exception e) {
    } finally {
      io.close();
    }
  }

}