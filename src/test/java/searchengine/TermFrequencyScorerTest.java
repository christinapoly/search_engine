package searchengine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class TermFrequencyScorerTest {

  private TermFrequencyScorer scorer;

  @BeforeEach
  void setUp() {
    scorer = new TermFrequencyScorer();
  }

  @Test
  void testLoadPages() {
    List<List<String>> pages = Arrays.asList(Arrays.asList("*page:example.com", "java", "python", "java"), Arrays.asList("*page:test.com", "python", "python", "java"));

    scorer.loadPages(pages);

    Map<String, Map<String, Double>> termFrequencies = scorer.countTermFrequency(pages);
    assertEquals(termFrequencies, scorer.countTermFrequency(pages));
  }


  @Test
  void testCountTermFrequency() {
    List<List<String>> pages = Arrays.asList(Arrays.asList("*page:example.com", "java", "python", "java"), Arrays.asList("*page:test.com", "python", "python", "java"));

    Map<String, Map<String, Double>> termFrequencies = scorer.countTermFrequency(pages);

    assertEquals(2.0 / 3, termFrequencies.get("example.com").get("java"));
    assertEquals(1.0 / 3, termFrequencies.get("example.com").get("python"));
    assertEquals(2.0 / 3, termFrequencies.get("test.com").get("python"));
    assertEquals(1.0 / 3, termFrequencies.get("test.com").get("java"));
  }


  @Test
  void testGetScore() {
    List<List<String>> pages = Arrays.asList(Arrays.asList("*page:example.com", "java", "python", "java"), Arrays.asList("*page:test.com", "python", "python", "java"));

    scorer.loadPages(pages);

    assertEquals(2.0 / 3, scorer.getScore("example.com", "java"));
    assertEquals(1.0 / 3, scorer.getScore("example.com", "python"));
    assertEquals(2.0 / 3, scorer.getScore("test.com", "python"));
    assertEquals(0.0, scorer.getScore("nonexistent.com", "java"));
    assertEquals(0.0, scorer.getScore("example.com", "nonexistent"));
  }
  

  @Test
  void testGetScoreWithExtraDataInUrl() {
    List<List<String>> pages = Arrays.asList(Arrays.asList("*page:example.com", "java", "python", "java"));

    scorer.loadPages(pages);

    assertEquals(2.0 / 3, scorer.getScore("example.com - extra data", "java"));
    assertEquals(0.0, scorer.getScore("example.com - extra data", "nonexistent"));
  }
}
