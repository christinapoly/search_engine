package searchengine;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The InvertedIndexSearchEngine class implements a search engine using an inverted index.
 * It allows loading pages from a file and searching for terms within those pages.
 */

public class InvertedIndexSearchEngine implements SearchEngine {
    List<List<String>> pages = new ArrayList<>();
    Map<String, ArrayList<String>> pageIndex = new HashMap<String, ArrayList<String>>();

    /**
     * Loads pages from a file and builds the inverted index.
     * @param filename the name of the file containing the pages
     * @throws IOException if an I/O error occurs while reading from the file
     */
    public void loadPages(String filename) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {
            String line;
            List<String> pageBuffer = new ArrayList<>();

            // Read the file line by line, check if the line has a title and content, and process the page
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("*PAGE")) {
                    if (!pageBuffer.isEmpty()) {
                        if (hasTitleAndContent(pageBuffer, 0)) {
                            processPage(pageBuffer);
                        }
                        pageBuffer.clear();
                    }
                }
                pageBuffer.add(line);
            }

            if (!pageBuffer.isEmpty()) {
                processPage(pageBuffer);
            }
        }

    }

    /**
     * Processes a single page buffer, extracts the URL and title, and adds words to the inverted index.
     * @param pageBuffer the list of strings representing the content of a page
     */
    private void processPage(List<String> pageBuffer) {
        String url = pageBuffer.get(0);
        url = url.replace("*PAGE:", ""); 
        String title = pageBuffer.size() > 1 ? pageBuffer.get(1).toLowerCase() : "";

        // Add all words to the inverted index
        for (String word : pageBuffer) {
            if (!word.equalsIgnoreCase(pageBuffer.get(0))) {
            pageIndex.computeIfAbsent(word.toLowerCase(), k -> new ArrayList<>())
                    .add(url + " - " + title);
            }
        }
    
        // Collect all terms in lowercase and add them to the list of pages
        List<String> terms = new ArrayList<>();
        for (String term : pageBuffer) {
            terms.add(term.toLowerCase());
        }
        pages.add(terms);
}

    /**
     * Returns the list of pages.
     * @return a list of lists. Each inner list represents a page.
     */
    public List<List<String>> getPages() {
        return pages;
    }

    /**
     * Search() searches for pages containing the specified term.
     *
     * @param searchTerm the term to search for
     * @return a list of URLs and titles of pages containing the term
     */
    public ArrayList<String> search(String searchTerm) {
        String searchTermToLower = searchTerm.toLowerCase();
        if (pageIndex.containsKey(searchTermToLower)) {
            return pageIndex.get(searchTermToLower);
        }
        return new ArrayList<String>();
    }

  /**
   * Checks if the line in the file has a title and content.
   * 
   * @param lines is the list of lines in the file
   * @param index is the index of the line to check
   * @return true if the line has a title and a content, false if not
   */
    public boolean hasTitleAndContent(List<String> lines, int index) {
        if (!lines.get(index).startsWith("*PAGE")) {
            return false;
        }
        if (index + 2 >= lines.size()) {
            return false;
        }
        if (lines.get(index + 1) == null || lines.get(index + 1).isEmpty()) {
            return false;
        }
        if (lines.get(index + 2) == null || lines.get(index + 2).isEmpty()) {
            return false;
        }
        return true;
 } 

}