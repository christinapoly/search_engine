package searchengine;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface for a search engine that provides methods to load web pages, and perform searches.
 */
public interface SearchEngine {
   /**
     * Loads web pages from a specified file.
     * @param filename the name of the file containing the pages to load.
     * @throws IOException if an error occurs while reading the file.
     */
   public void loadPages(String filename) throws IOException;

   /**
     * Searches for a given term across the loaded web pages.
     * @param searchTerm the term to search for.
     * @return a list of URLs and titles of pages containing the search term.
     */
   public ArrayList<String> search(String searchTerm);

   /**
     * Retrieves the list of all loaded web pages.
     * @return a list of pages, where each page is represented as a list of strings.
     */
   public List<List<String>> getPages();
}

