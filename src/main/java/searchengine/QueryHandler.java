package searchengine;

import java.util.*;
import java.util.stream.Collectors;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * A class to handle user queries for a search engine.
 * This class processes queries and gets web pages based on AND or OR logic.
 * The class orders the pages based on the relevance of the search terms.
 */
public class QueryHandler {
    private final SearchEngine searchEngine;
    private TermScorer termScorer;

    /**
     * Constructor to create a QueryHandler object.
     * The search engine is used to look up words in the query.
     * The term scorer is used to calculate the relevance of pages.
     * 
     * @param searchEngine the search engine to look up words in the query.
     * @param termScorer the term scorer to calculate the relevance of pages.
     */
    public QueryHandler(SearchEngine searchEngine, TermScorer termScorer) {
        this.searchEngine = searchEngine;
        this.termScorer = termScorer;
    }

    /**
     * This method searches for web pages where all words in the query are present.
     * It splits the query into words and finds pages that contain all of them.
     * The method then calculates the relevance of each page based on the search terms.
     * @param query the search query containing words to be matched.
     * @return a HashMap where the keys are page URLs and the values are their scores.
     */
 
    public HashMap<String, Double> andSearch(String query) {
        Set<String> results = new HashSet<>();

        List<String> clauseWords = Arrays.asList(query.split("\\s+"));
        List<List<String>> pageLists = clauseWords.stream()
            .map(word -> searchEngine.search(word.toLowerCase()))
            .collect(Collectors.toList());
        Set<String> clauseResult = findCommonPages(pageLists);
        results.addAll(clauseResult);

        HashMap<String, Double> pagesWithScores = new HashMap<>();
        for (String page : clauseResult) {
           for (String word : clauseWords) {
               pagesWithScores.put(page, pagesWithScores.getOrDefault(page, 0.0) + termScorer.getScore(page, word));
           }
        }

        return pagesWithScores;
    }

    /**
     * This method gets pages that match any part of the query.
     * The query can have multiple words separated by "OR".
     * The method then calculates the relevance of each page based on the search terms.
     * The pages are ordered by relevance.
     * @param query the search query, which can contain "OR" clauses.
     * @return a list of page URLs sorted by relevance in descending order.

     */
    public List<String> getMatchingWebPages(String query) {
        String decodedQuery = URLDecoder.decode(query, StandardCharsets.UTF_8);
        String[] orClauses = decodedQuery.split("(?i)\\s+OR\\s+");

        HashMap<String, Double> results = new HashMap<String, Double>();
        for (String searchTerm : orClauses) {
            HashMap<String, Double> andResults = andSearch(searchTerm);
            for (String page : andResults.keySet()) {
                if (results.containsKey(page)) {
                   results.put(page, Math.max(results.get(page), andResults.get(page)));
               } else {
                   results.put(page, andResults.get(page));
               }
            }
        }

        List<String> sortedKeys = results.entrySet().stream()
           .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
           .map(Map.Entry::getKey)
           .collect(Collectors.toList());

        return sortedKeys;
    }

    /**
     * Helper method to find pages that are common across all given lists.
     * Used for AND searches to find pages containing all words.
     * @param pageLists a list of lists of page URLs
     * @return a set of page URLs that are common across all lists
     */
    private Set<String> findCommonPages(List<List<String>> pageLists) {
        if (pageLists.isEmpty()) {
            return Collections.emptySet();
        }

        Set<String> commonPages = new HashSet<>(pageLists.get(0));
        for (List<String> pages : pageLists) {
            commonPages.retainAll(pages);
        }

        return commonPages;
    }
}
