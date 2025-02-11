package searchengine;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;

public class InvertedIndexSearchEngineTest {
    @Test
    public void testSearchWithResults() throws IOException {
        InvertedIndexSearchEngine searchEngine = new InvertedIndexSearchEngine();
        
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.write(tempFile, List.of(
            "*PAGE:doc1",
            "Title1",
            "test",
            "*PAGE:doc2",
            "Title2",
            "test"
        ));
        searchEngine.loadPages(tempFile.toString());

        ArrayList<String> results = searchEngine.search("test");
        assertEquals(2, results.size());
        assertTrue(results.contains("doc1 - title1"));
        assertTrue(results.contains("doc2 - title2"));

        
        Files.delete(tempFile);
    }

    @Test
    public void testSearchCaseInsensitive() throws IOException {
        InvertedIndexSearchEngine searchEngine = new InvertedIndexSearchEngine();
        
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.write(tempFile, List.of(
            "*PAGE:doc1",
            "Title1",
            "test",
            "*PAGE:doc2",
            "Title2",
            "test"
        ));
        searchEngine.loadPages(tempFile.toString());

        ArrayList<String> results = searchEngine.search("TEst");
        assertEquals(2, results.size());
        assertTrue(results.contains("doc1 - title1"));
        assertTrue(results.contains("doc2 - title2"));

        
        Files.delete(tempFile);
    }
    
    @Test
    public void testSearchWithoutResults() throws IOException {
        InvertedIndexSearchEngine searchEngine = new InvertedIndexSearchEngine();

        Path tempFile = Files.createTempFile("test", ".txt");
        Files.write(tempFile, List.of(
            "*PAGE:doc1",
            "Title1",
            "test",
            "*PAGE:doc2",
            "Title2",
            "test"
        ));

        searchEngine.loadPages(tempFile.toString());

        ArrayList<String> results = searchEngine.search("nothing");

        assertEquals(0, results.size());

        Files.delete(tempFile);
    }

    @Test
    public void testEmptyIndex() throws IOException {
        InvertedIndexSearchEngine searchEngine = new InvertedIndexSearchEngine();

        Path tempFile = Files.createTempFile("test", ".txt");
        searchEngine.loadPages(tempFile.toString());
        ArrayList<String> results = searchEngine.search("test");

        assertEquals(0, results.size());

        Files.delete(tempFile);

    }

    @Test
    public void testProcessPageWithSingleLine() throws IOException {
        InvertedIndexSearchEngine searchEngine = new InvertedIndexSearchEngine();

        Path tempFile = Files.createTempFile("test", ".txt");
        Files.write(tempFile, List.of(
            "*PAGE:doc1"
        ));

        searchEngine.loadPages(tempFile.toString());

        List<List<String>> pages = searchEngine.getPages();
        assertEquals(1, pages.size());
        assertTrue(pages.get(0).contains("*page:doc1"));
        Files.delete(tempFile);
    }

    @Test
    public void testHasTitleAndMissingContent() {
        InvertedIndexSearchEngine searchEngine = new InvertedIndexSearchEngine();

        List<String> lines = List.of("*PAGE:doc1", "Title", "");
        boolean result = searchEngine.hasTitleAndContent(lines, 0);

        assertFalse(result);
    }

}
