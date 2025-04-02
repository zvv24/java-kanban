package managers;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager manager = new InMemoryHistoryManager();

    @Test
    public void historyManagerAddDifferentTypesOfTasks() {
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW);
        manager.addHistory(task1);
        Epic epic1 = new Epic(2, "Эпик 1", "Описание эпика 1", Status.NEW, new ArrayList<>());
        manager.addHistory(epic1);
        Subtask subtask1 = new Subtask(3, "Подзадача 1", "Описание подзадачи 1", Status.NEW, 2);
        manager.addHistory(subtask1);


        List<Task> history = manager.getHistory();
        assertEquals(task1, history.get(0));
        assertEquals(epic1, history.get(1));
        assertEquals(subtask1, history.get(2));
    }

    @Test
    public void historyManagerNotSaveRepeats() {
        manager.addHistory(new Task(1, "Задача 1", "Описание задачи 1", Status.NEW));
        manager.addHistory(new Task(1, "Задача 1", "Описание задачи 1", Status.NEW));
        manager.addHistory(new Task(1, "Задача 1", "Описание задачи 1", Status.NEW));


        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void historyIsNotLimitedInSize() {
        for (int i = 1; i <= 15; i++) {
            manager.addHistory(new Task(i, "name", "des", Status.NEW));
        }

        assertEquals(15, manager.getHistory().size());
    }


    @Test
    public void deletingTaskFromHeadHistory() {
        manager.addHistory(new Task(1, "Задача 1", "Описание задачи 1", Status.NEW));
        Epic epic1 = new Epic(2, "Эпик 1", "Описание эпика 1", Status.NEW, new ArrayList<>());
        manager.addHistory(epic1);
        Subtask subtask1 = new Subtask(3, "Подзадача 1", "Описание подзадачи 1", Status.NEW, 2);
        manager.addHistory(subtask1);

        manager.removeHistory(1);

        assertEquals(epic1, manager.getHistory().get(0));
        assertEquals(subtask1, manager.getHistory().get(1));
    }

    @Test
    public void deletingTaskFromMiddleHistory() {
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW);
        manager.addHistory(task1);
        Epic epic1 = new Epic(2, "Эпик 1", "Описание эпика 1", Status.NEW, new ArrayList<>());
        manager.addHistory(epic1);
        Task task2 = new Task(3, "Задача 2", "Описание задачи 2", Status.NEW);
        manager.addHistory(task2);

        manager.removeHistory(2);

        assertEquals(task1, manager.getHistory().get(0));
        assertEquals(task2, manager.getHistory().get(1));
    }

    @Test
    public void deletingTaskFromTailHistory() {
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW);
        manager.addHistory(task1);
        Epic epic1 = new Epic(2, "Эпик 1", "Описание эпика 1", Status.NEW, new ArrayList<>());
        manager.addHistory(epic1);
        Task task2 = new Task(3, "Задача 2", "Описание задачи 2", Status.NEW);
        manager.addHistory(task2);

        manager.removeHistory(3);

        assertEquals(task1, manager.getHistory().get(0));
        assertEquals(epic1, manager.getHistory().get(1));
    }
}