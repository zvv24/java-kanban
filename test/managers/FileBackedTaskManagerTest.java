package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File file;

    @BeforeEach
    public void setUp() throws IOException {
        file = File.createTempFile("tasks", ".csv");
        file.deleteOnExit();
        super.setUp();
    }

    @Override
    public FileBackedTaskManager createManager() {
        return new FileBackedTaskManager(file);
    }

    @Test
    public void savingAndUploadingEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("empty", ".csv");
        emptyFile.deleteOnExit();
        FileBackedTaskManager emptyManager = new FileBackedTaskManager(emptyFile);

        emptyManager.deleteAllTasks();
        emptyManager.deleteAllEpics();
        emptyManager.deleteAllSubtasks();
        emptyManager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(emptyFile);

        assertTrue(loadedManager.printAllTasks().isEmpty(), "Список задач должен быть пустым");
        assertTrue(loadedManager.printAllEpics().isEmpty(), "Список эпиков должен быть пустым");
        assertTrue(loadedManager.printAllSubtasks().isEmpty(), "Список подзадач должен быть пустым");
    }

    @Test
    public void savingAndUploadingSomeTasks() {
        manager.save();
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(manager.printAllTasks(), fileBackedTaskManager.printAllTasks());
        assertEquals(manager.printAllEpics(), fileBackedTaskManager.printAllEpics());
        assertEquals(manager.printAllSubtasks(), fileBackedTaskManager.printAllSubtasks());
    }
}