package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    public T manager;
    public Task task;
    public Epic epic;
    public Subtask subtask1;
    public Subtask subtask2;

    public abstract T createManager();

    @BeforeEach
    public void setUp() throws IOException {
        manager = createManager();
        task = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        epic = new Epic(2, "Эпик 1", "Описание эпика 1", Status.NEW, new ArrayList<>());
        subtask1 = new Subtask(3, "Подзадача 1", "Описание подзадачи 1", Status.NEW, 2,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(15));
        subtask2 = new Subtask(4, "Подзадача 2", "Описание подзадачи 2", Status.IN_PROGRESS, 2,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(20));

        manager.newTask(task);
        manager.newEpic(epic);
        manager.newSubtasks(subtask1);
        manager.newSubtasks(subtask2);
    }

    @Test
    public void addAndSearchForDifferentTasks() {
        assertEquals(task, manager.searchTaskByID(1), "Задача не найдена");
        assertEquals(epic, manager.searchEpicByID(2), "Эпик не найдена");
        assertEquals(subtask1, manager.searchSubtaskByID(3), "Подзадача не найдена");
    }

    @Test
    public void taskDoesNotChangeWhenAddedToManager() {
        Task addedTask = manager.searchTaskByID(1);

        assertEquals(task.getId(), addedTask.getId(), "id не должно изменяться при добавлении в менеджер");
        assertEquals(task.getName(), addedTask.getName(), "имя не должно изменяться при добавлении в менеджер");
        assertEquals(task.getDescription(), addedTask.getDescription(), "описание не должно изменяться при добавлении в менеджер");
        assertEquals(task.getStatus(), addedTask.getStatus(), "статус не должен изменяться при добавлении в менеджер");
    }

    @Test
    public void canDeleteTasksById() {
        manager.deleteTaskByID(1);
        manager.deleteSubtaskByID(3);
        manager.deleteEpicByID(2);

        assertNull(manager.searchTaskByID(1));
        assertNull(manager.searchEpicByID(2));
        assertNull(manager.searchSubtaskByID(3));
    }

    @Test
    public void canUpdateTasks() {
        Task newTask = new Task(1, "Обновленная задача", "Новое описание", Status.DONE,
                LocalDateTime.now().plusHours(3), Duration.ofMinutes(45));
        Epic newEpic = new Epic(2, "Обновленный эпик", "Новое описание", Status.DONE, new ArrayList<>());
        Subtask newSubtask = new Subtask(3, "Обновленная подзадача", "Новое описание", Status.DONE, 2,
                LocalDateTime.now().plusHours(4), Duration.ofMinutes(10));

        manager.updateTask(newTask);
        manager.updateEpic(newEpic);
        manager.updateSubtask(newSubtask);
        assertEquals(newTask, manager.searchTaskByID(1), "Задача не обновлена");
        assertEquals(newEpic, manager.searchEpicByID(2), "Эпик не обновлен");
        assertEquals(newSubtask, manager.searchSubtaskByID(3), "Подзадача не обновлена");
    }

    @Test
    public void searchMethodsAddedToHistory() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(manager.searchSubtaskByID(3));
        tasks.add(manager.searchTaskByID(1));
        tasks.add(manager.searchEpicByID(2));
        tasks.add(manager.searchSubtaskByID(4));

        assertEquals(tasks, manager.getHistory());
        assertEquals(epic, manager.getHistory().get(2));
    }

    @Test
    public void printSubtasksOfCertainEpic() {
        ArrayList<Subtask> subtasks = manager.printSubtasksСertainEpic(2);
        assertEquals(manager.printAllSubtasks(), subtasks);
    }

    @Test
    public void notSaveRepeats() {
        manager.searchTaskByID(1);
        manager.searchTaskByID(1);
        manager.searchTaskByID(1);

        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void checkTaskOverlap() {
        Task task1 = new Task(5, "Пересекающаяся задача", "Описание", Status.NEW,
                task.getStartTime().plusMinutes(10), Duration.ofMinutes(20));
        assertTrue(manager.taskNotOverlap(task1), "Не обнаружено пересечение");
    }

}