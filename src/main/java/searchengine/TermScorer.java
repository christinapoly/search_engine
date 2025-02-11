package searchengine;

import java.util.List;

/**
 * An interface that defines the operations for a scoring system used in a search engine.
 */
public interface TermScorer {

   /**
    * Computes the relevance score of a given URL based on a search term.
    * @param url The URL of the webpage to be scored.
    * @param searchTerm The search term used to calculate the score.
    * @return A double representing the relevance score of the webpage.
    */
    public double getScore(String url, String searchTerm);

   /**
    * Loads a collection of pages.
    * @param pages A list of pages, where each page is represented as a list of strings.
    */
    public void loadPages(List<List<String>> pages);
}
