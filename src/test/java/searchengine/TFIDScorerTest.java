package searchengine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TFIDScorerTest {

    private TFIDScorer tfidScorer;

    @BeforeEach
    void setUp() {
        tfidScorer = new TFIDScorer();
    }

    @Test
    void loadPagesCalculatesCorrectScores() {
        List<List<String>> pages = Arrays.asList(
                Arrays.asList("*page:page1", "test1", "test2", "test1"),
                Arrays.asList("*page:page2", "test2", "test3", "test2", "test2"),
                Arrays.asList("*page:page3", "test1", "test3", "test1", "test1")
        );

        tfidScorer.loadPages(pages);

        double test1Page1Score = tfidScorer.getScore("page1", "test1");
        double test2Page1Score = tfidScorer.getScore("page1", "test2");
        double test3Page2Score = tfidScorer.getScore("page2", "test3");

        assertTrue(test1Page1Score > 0, "TFIDF score for 'test1' in page1 should be greater than 0");
        assertTrue(test2Page1Score > 0, "TFIDF score for 'test2' in page1 should be greater than 0");
        assertTrue(test3Page2Score > 0, "TFIDF score for 'test3' in page2 should be greater than 0");
    }

    @Test
    void getScoreReturnsCorrectValues() {
        List<List<String>> pages = Arrays.asList(
                Arrays.asList("*page:page1", "test1", "test2", "test1"),
                Arrays.asList("*page:page2", "test2", "test3"),
                Arrays.asList("*page:page3", "test1", "test3")
        );
        tfidScorer.loadPages(pages);

        assertEquals(0.0, tfidScorer.getScore("page1", "test3"), "TFIDF score for 'test3' in page1 should be 0");
        assertNotEquals(0.0, tfidScorer.getScore("page2", "test2"), "TFIDF score for 'test2' in page2 should not be 0");
        assertNotEquals(0.0, tfidScorer.getScore("page3", "test1"), "TFIDF score for 'test1' in page3 should not be 0");
    }

    @Test
    void countTFIDScoreHandlesEmptyPages() {
        List<List<String>> pages = Collections.emptyList();
        Map<String, Map<String, Double>> tfidfScores = tfidScorer.countTFIDScore(pages);

        assertTrue(tfidfScores.isEmpty(), "TFIDF scores should be empty for no input pages");
    }

    @Test
    void countTFIDScoreHandlesSinglePageWithTerms() {
        List<List<String>> pages = Collections.singletonList(
                Arrays.asList("*page:page1", "test1", "test2", "test1")
        );

        Map<String, Map<String, Double>> tfidfScores = tfidScorer.countTFIDScore(pages);

        assertTrue(tfidfScores.containsKey("page1"), "TFIDF scores should include 'page1'");
        assertTrue(tfidfScores.get("page1").containsKey("test1"), "TFIDF scores should include term 'test1'");
        assertTrue(tfidfScores.get("page1").containsKey("test2"), "TFIDF scores should include term 'test2'");
    }
}
