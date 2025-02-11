package searchengine;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Scoring system implementation that calculates term frequencies for pages and computes scores 
 * based on the frequency of search terms.
 */
public class TermFrequencyScorer implements TermScorer {
    /**
     * Stores the term frequencies for each URL. 
     * The outer map's key is the URL, and the value is another map of terms and their frequencies.
     */
    private Map<String, Map<String, Double>> termFrequencies = new HashMap<>();

    /**
     * Constructs a new TermFrequencyScorer instance with an empty term frequency map.
     */
    public TermFrequencyScorer() {
    }
    
    /**
     * Loads a list of pages and calculates the term frequencies for each page.
     * @param pages A list of pages, where each page is a list of strings.
     *              The first string in each list represents the URL, 
     *              and subsequent strings are the terms found on the page.
     */
    public void loadPages(List<List<String>> pages) {
        this.termFrequencies = countTermFrequency(pages);
    }

    /**
     * Counts the term frequency for a collection of pages.
     *
     * @param pages A list of pages, where each page is a list of strings.
     *              The first string in each list represents the URL, 
     *              and subsequent strings are the terms found on the page.
     * @return A map where the key is the URL, and the value is another map of terms and their frequencies.
     */
    public Map<String, Map<String, Double>> countTermFrequency(List<List<String>> pages) {
        Map<String, Map<String, Double>> termFrequencies = new HashMap<>();
        for (List<String> page : pages) {
            String url = page.get(0).replace("*page:", "");
            Map<String, Double> wordCount = new HashMap<>();
            for (int i = 1; i < page.size(); i++) {
                String word = page.get(i);
                double wordCountValue = wordCount.getOrDefault(word, 0.);
                wordCount.put(word, wordCountValue + 1. / (page.size() - 1));
            }
            termFrequencies.put(url, wordCount);
        }
        return termFrequencies;
    } 

    /**
     * Gets the score for a specific URL and search term based on the term frequency.
     *
     * @param url The URL of the page to score.
     * @param searchTerm The term whose frequency will be used to calculate the score.
     * @return A double representing the term frequency score. If the URL or search term is not found, returns 0.0.
     */
    public double getScore(String url, String searchTerm) {
        url = url.split(" - ")[0];
        if (!termFrequencies.containsKey(url)) {
            return 0.0;
        }
        return termFrequencies.get(url).getOrDefault(searchTerm, 0.0);
    }

    
}
