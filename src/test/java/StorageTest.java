import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StorageTest {
    @Test
    public void testLoad() {
        Storage storage = new Storage("storageTest.txt");
        try {
            List<Task> tasks = storage.load();
            assertEquals(tasks.size(), 2);
        } catch (DukeException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testLoad_badInput() {
        Storage storage = new Storage("storageTestException.txt");
        assertThrows(DukeException.class, () -> storage.load());
    }
}