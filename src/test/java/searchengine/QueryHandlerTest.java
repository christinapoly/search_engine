package searchengine;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * This class tests the QueryHandler class.
 * It checks if AND and OR searches work as expected using a fake search engine.
 */
public class QueryHandlerTest {

    /**
     * To test the andSearch method.
     * To check if it correctly finds pages with all the words in the query.
     */
    @Test
    public void testAndSearch() {
        InvertedIndexSearchEngine searchEngine = new TestSearchEngine();
        TermScorer termScorer = new TestScoringSystem();
        QueryHandler queryHandler = new QueryHandler(searchEngine, termScorer);

        HashMap<String, Double> result = queryHandler.andSearch("java programming");
        HashMap<String, Double> expected = new HashMap<>();
        expected.put("page2", 2.0);

        assertEquals(expected, result, "The AND search should return the correct pages.");
    }

    /**
     * To test the getMatchingWebPages method.
     * To check if it correctly finds pages with any of the words in the query.
     */
    @Test
    public void testGetMatchingWebPages() {
        InvertedIndexSearchEngine searchEngine = new TestSearchEngine();
        TermScorer termScorer = new TestScoringSystem();
        QueryHandler queryHandler = new QueryHandler(searchEngine, termScorer);

        List<String> result = queryHandler.getMatchingWebPages("java OR programming");
        List<String> expected = Arrays.asList("page1", "page2", "page3");

        assertTrue(result.containsAll(expected) && expected.containsAll(result),
                "The OR search should return the correct list of pages.");
    }

    /**
     * A simple subclass of InvertedIndexSearchEngine to simulate its behavior for testing.
     * To provide predefined responses for specific search terms.
     */
    private static class TestSearchEngine extends InvertedIndexSearchEngine {
        /**
         * To search for a word and returns a fixed list of pages.
         * For example, "java" returns "page1" and "page2".
         */
        @Override
        public ArrayList<String> search(String word) {
            if ("java".equalsIgnoreCase(word)) {
                return new ArrayList<>(Arrays.asList("page1", "page2"));
            } else if ("programming".equalsIgnoreCase(word)) {
                return new ArrayList<>(Arrays.asList("page2", "page3"));
            }
            return new ArrayList<>();
        }
    }

    /**
     * A siimple implementation of ScoringSystem for testing.
     * To provide predefined scores for specific pages and words.
     */

     private static class TestScoringSystem implements TermScorer {
        @Override
        public double getScore(String page, String word) {
            return 1.0;
        }

        public void loadPages(List<List<String>>pages) {
            //Do nothing
        }
    }
}