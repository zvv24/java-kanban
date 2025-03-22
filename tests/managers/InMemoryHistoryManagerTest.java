package managers;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class InMemoryHistoryManagerTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();

    @Test
    public void historyManagerAddDifferentTypesOfTasks() {
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW);
        manager.newTask(task1);
        Epic epic1 = new Epic(2, "Эпик 1", "Описание эпика 1", Status.NEW, new ArrayList<>());
        manager.newEpic(epic1);
        Subtask subtask1 = new Subtask(3, "Подзадача 1", "Описание подзадачи 1", Status.NEW, 2);
        manager.newSubtasks(subtask1);

        manager.searchTaskByID(1);
        manager.searchEpicByID(2);
        manager.searchSubtaskByID(3);

        List<Task> history = manager.getHistory();
        assertEquals(task1, history.get(0));
        assertEquals(epic1, history.get(1));
        assertEquals(subtask1, history.get(2));
    }

    @Test
    public void historyManagerDeletesTheOldestTask() {
        for (int i = 1; i <= 10; i++) {
            manager.newTask(new Task(i, "name", "des", Status.NEW));
            manager.searchTaskByID(i);
        }

        Task task1 = new Task(1, "name", "des", Status.NEW);
        Task task11 = new Task(11, "Задача", "Описание", Status.NEW);
        manager.newTask(task11);

        assertEquals(task1, manager.getHistory().get(0));
        manager.searchTaskByID(11);
        assertNotEquals(task1, manager.getHistory().get(0));
        assertEquals(task11, manager.getHistory().get(9));
    }
}