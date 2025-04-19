package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        Epic epic = new Epic(2, "Эпик 1", "Описание эпика 1", Status.NEW, new ArrayList<>());
        Subtask subtask = new Subtask(3, "Подзадача 1", "Описание подзадачи 1", Status.NEW, 2);

        manager.newTask(task);
        manager.newEpic(epic);
        manager.newSubtasks(subtask);
        List<Task> tasks1 = manager.getAllTasks();

        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        List<Task> tasks2 = fileBackedTaskManager.getAllTasks();

        assertEquals(tasks1, tasks2);
    }
}