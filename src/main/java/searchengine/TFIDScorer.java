package searchengine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Scoring system that calculates Term Frequency Inverse Document Frequency 
 * scores for pages and computes relevance scores for given search terms.
 */
public class TFIDScorer implements TermScorer { 
    /**
     * Stores the TFIDF scores for each URL. 
     * The outer map's key is the URL, and the value is another map of search terms and their TFIDF scores.
     */
    private Map<String, Map<String, Double>> termFrequencies = new HashMap<>();

    /**
     * Constructs a new TFIDScorer instance.
     */
    public TFIDScorer() {
    }
    
    /**
     * Loads a list of pages and calculates the TFIDF scores for each term on each page.
     *
     * @param pages A list of pages, where each page is a list of strings.
     *              The first string in each list represents the URL, 
     *              and subsequent strings are the terms found on the page.
     */
    public void loadPages(List<List<String>> pages) {
        this.termFrequencies = countTFIDScore(pages);
    }

    /**
     * Calculates the TFIDF scores for each term on each page.
     *
     * @param pages A list of pages, where each page is a list of strings.
     *              The first string in each list represents the URL, 
     *              and subsequent strings are the terms found on the page.
     * @return A map where the key is the URL, and the value is another map of terms and their TFIDF scores.
     */
    public Map<String, Map<String, Double>> countTFIDScore(List<List<String>> pages) {
        Map<String, Map<String, Double>> tfidfScores = new HashMap<>();
        Map<String, Integer> documentFrequencies = new HashMap<>();
        int totalPages = pages.size();

        // Calculate document frequencies for each term
        for (List<String> page : pages) {
            Set<String> uniqueWords = new HashSet<>(page.subList(1, page.size())); 
            for (String word : uniqueWords) {
                documentFrequencies.put(word, documentFrequencies.getOrDefault(word, 0) + 1);
            }
        }


        // Calculate TFIDF scores for each term on each page.
        for (List<String> page : pages) {
            String url = page.get(0).replace("*page:", "");
            Map<String, Double> wordScores = new HashMap<>();

            for (int i = 1; i < page.size(); i++) {
                String word = page.get(i);

                // Calculate term frequency
                long wordCount = page.subList(1, page.size()).stream().filter(w -> w.equals(word)).count();
                double termFrequency = (double) wordCount / (page.size() - 1);

                // Calculate inverse document frequency
                int docFrequency = documentFrequencies.get(word);
                double idf = Math.log((double) totalPages / (double) docFrequency);

                // Compute TFIDF score
                wordScores.put(word, termFrequency * idf);
            }

            tfidfScores.put(url, wordScores);
        }

        return tfidfScores;
    }

    /**
     * Gets the TFIDF score for a specific URL and search term.
     *
     * @param url The URL of the page to score.
     * @param searchTerm The term whose score will be retrieved.
     * @return A double representing the TFIDF score. If the URL or search term is not found, the function returns 0.0.
     */
    public double getScore(String url, String searchTerm) {
        url = url.split(" - ")[0];
        url = url.toLowerCase();
        if (!termFrequencies.containsKey(url)) {
            return 0.0;
        }
        return termFrequencies.get(url).getOrDefault(searchTerm, 0.0);
    }

}
