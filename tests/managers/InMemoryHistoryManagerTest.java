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
    public void historyManagerNotSaveRepeats() {
        manager.newTask(new Task(1, "Задача 1", "Описание задачи 1", Status.NEW));
        manager.searchTaskByID(1);
        manager.searchTaskByID(1);
        manager.searchTaskByID(1);

        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void historyIsNotLimitedInSize() {
        for (int i = 1; i <= 9999; i++) {
            manager.newTask(new Task(i, "name", "des", Status.NEW));
            manager.searchTaskByID(i);
        }

        assertEquals(9999, manager.getHistory().size());
    }

    @Test
    public void historyManagerDeletesSubtasksIfEpicIsDeleted() {
        manager.newTask(new Task(1, "Задача 1", "Описание задачи 1", Status.NEW));
        manager.newEpic(new Epic(2, "Эпик 1", "Описание эпика 1", Status.NEW, new ArrayList<>()));
        manager.newSubtasks(new Subtask(3, "Подзадача 1", "Описание подзадачи 1", Status.NEW, 2));
        manager.newSubtasks(new Subtask(4, "Подзадача 1", "Описание подзадачи 1", Status.NEW, 2));

        manager.searchTaskByID(1);
        manager.searchEpicByID(2);
        manager.searchSubtaskByID(3);
        manager.searchSubtaskByID(4);

        assertEquals(4, manager.getHistory().size());

        manager.deleteEpicByID(2);

        assertEquals(1, manager.getHistory().size());
    }
}