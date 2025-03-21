package tests;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    @Test
    public void InMemoryTaskManagerCanAddAndSearchForDifferentTasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task = new Task(1, "Задача", "Описание задачи", Status.NEW);
        manager.newTask(task);
        Epic epic = new Epic(2, "Эпик", "Описание эпика", Status.DONE, new ArrayList<>());
        manager.newEpic(epic);
        Subtask subtask = new Subtask(3, "Подзадача", "Описание подзадачи", Status.NEW, 2);
        manager.newSubtasks(subtask);

        assertEquals(task, manager.searchTaskByID(1), "Задача не найдена");
        assertEquals(epic, manager.searchEpicByID(2), "Эпик не найдена");
        assertEquals(subtask, manager.searchSubtaskByID(3), "Подзадача не найдена");
    }

    @Test
    public void taskDoesNotChangeWhenAddedToManager(){
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task = new Task(1, "Задача", "Описание задачи", Status.NEW);
        manager.newTask(task);
        Task addedTask = manager.searchTaskByID(1);

        assertEquals(task.getId(), addedTask.getId(), "id не должно изменяться при добавлении в менеджер");
        assertEquals(task.getName(), addedTask.getName(), "имя не должно изменяться при добавлении в менеджер");
        assertEquals(task.getDescription(), addedTask.getDescription(), "описание не должно изменяться при добавлении в менеджер");
        assertEquals(task.getStatus(), addedTask.getStatus(), "статус не должен изменяться при добавлении в менеджер");
    }

    @Test
    public void historyManagerAddDifferentTypesOfTasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW);
        manager.newTask(task1);
        Epic epic1 = new Epic(2, "Эпик 1", "Описание эпика 1", Status.NEW, new ArrayList<>());
        manager.newEpic(epic1);
        Subtask subtask1 = new Subtask(3, "Подзадача 1", "Описание подзадачи 1", Status.NEW, 2);
        manager.newSubtasks(subtask1);

        manager.searchTaskByID(1);
        manager.searchEpicByID(2);
        manager.searchSubtaskByID(3);

        List<Task> history =  manager.getHistory().getHistory();
        assertEquals(task1, history.get(0));
        assertEquals(epic1, history.get(1));
        assertEquals(subtask1, history.get(2));
    }

    @Test
    public void historyManagerDeletesTheOldestTask() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        for (int i = 1; i <= 10; i++) {
            manager.newTask(new Task(i, "name", "des", Status.NEW));
            manager.searchTaskByID(i);
        }

        List<Task> history =  manager.getHistory().getHistory();
        Task task1 = new Task(1, "name", "des", Status.NEW);
        Task task11 = new Task(11, "Задача", "Описание", Status.NEW);
        manager.newTask(task11);

        assertEquals(task1, history.get(0));
        manager.searchTaskByID(11);
        assertNotEquals(task1, history.get(0));
        assertEquals(task11, history.get(9));
    }
}