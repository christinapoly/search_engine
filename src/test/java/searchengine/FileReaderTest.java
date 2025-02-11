package searchengine;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileReaderTest {

    @Test
    void getFile() throws IOException {
        Path tempFile = Files.createTempFile("code", ".js");

        try {
            String expectedContent = "This is a test file.";
            Files.writeString(tempFile, expectedContent);

            byte[] fileContent = FileReader.getFile(tempFile.toString());

            assertNotNull(fileContent, "File content should not be null");
            assertArrayEquals(expectedContent.getBytes(), fileContent, "File content does not match the expected content");
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
}
