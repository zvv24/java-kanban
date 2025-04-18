package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class FileBackedTaskManagerTest {
    File file;
    FileBackedTaskManager manager;

    @BeforeEach
    public void beforeEach() throws IOException {
        file = File.createTempFile("file", ".csv");
        file.deleteOnExit();
        manager = new FileBackedTaskManager(file);
    }

    @Test
    public void savingAndUploadingEmptyFile() {
        manager.save();
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);

        assertTrue(fileBackedTaskManager.printAllTasks().isEmpty());
        assertTrue(fileBackedTaskManager.printAllEpics().isEmpty());
        assertTrue(fileBackedTaskManager.printAllSubtasks().isEmpty());
    }

    @Test
    public void savingAndUploadingSomeTasks() throws IOException {
        Task task = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW);
        Task task1 = new Task(2, "Задача 2", "Описание задачи 2", Status.DONE);

        manager.newTask(task);
        manager.newTask(task1);
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        List<Task> tasks = fileBackedTaskManager.printAllTasks();

        assertEquals(task, tasks.get(0));
        assertEquals(task1, tasks.get(1));
    }
}