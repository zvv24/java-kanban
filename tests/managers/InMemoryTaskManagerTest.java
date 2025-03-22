package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    InMemoryTaskManager manager;

    @BeforeEach
    void beforeEach() {
        manager = new InMemoryTaskManager();
    }

    @Test
    public void inMemoryTaskManagerCanAddAndSearchForDifferentTasks() {
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
    public void taskDoesNotChangeWhenAddedToManager() {
        Task task = new Task(1, "Задача", "Описание задачи", Status.NEW);
        manager.newTask(task);
        Task addedTask = manager.searchTaskByID(1);

        assertEquals(task.getId(), addedTask.getId(), "id не должно изменяться при добавлении в менеджер");
        assertEquals(task.getName(), addedTask.getName(), "имя не должно изменяться при добавлении в менеджер");
        assertEquals(task.getDescription(), addedTask.getDescription(), "описание не должно изменяться при добавлении в менеджер");
        assertEquals(task.getStatus(), addedTask.getStatus(), "статус не должен изменяться при добавлении в менеджер");
    }

    @Test
    public void deletingSubtasksTheyShouldBeDeletedFromTheEpics() {
        Epic epic1 = new Epic(1, "Эпик 1", "В нем должно быть 2 подзадачи", Status.NEW, new ArrayList<>());
        manager.newEpic(epic1);
        manager.newSubtasks(new Subtask(2, "Подзадача 1", "Подзадача эпика 1", Status.NEW, 1));
        manager.newSubtasks(new Subtask(3, "Подзадача 2", "Подзадача эпика 1", Status.IN_PROGRESS, 1));

        manager.deleteAllSubtasks();
        assertTrue(epic1.getSubtask().isEmpty());
    }

    @Test
    public void deletingAllEpicsDeletedAllSubtasks() {
        Epic epic1 = new Epic(1, "Эпик 1", "В нем должно быть 2 подзадачи", Status.NEW, new ArrayList<>());
        manager.newEpic(epic1);
        manager.newSubtasks(new Subtask(2, "Подзадача 1", "Подзадача эпика 1", Status.NEW, 1));
        manager.newSubtasks(new Subtask(3, "Подзадача 2", "Подзадача эпика 1", Status.IN_PROGRESS, 1));

        manager.deleteAllEpics();
        assertTrue(manager.printAllSubtasks().isEmpty());
    }

    @Test
    public void printSubtasksOfCertainEpic() {
        Epic epic1 = new Epic(1, "Эпик 1", "В нем должно быть 2 подзадачи", Status.NEW, new ArrayList<>());
        manager.newEpic(epic1);
        manager.newSubtasks(new Subtask(2, "Подзадача 1", "Подзадача эпика 1", Status.NEW, 1));
        manager.newSubtasks(new Subtask(3, "Подзадача 2", "Подзадача эпика 1", Status.IN_PROGRESS, 1));

        ArrayList<Subtask> subtasks = manager.printSubtasksСertainEpic(1);
        assertEquals(manager.printAllSubtasks(), subtasks);
    }

    @Test
    public void inMemoryTaskManagerCanDeleteTasksById() {
        Task task = new Task(1, "Задача", "Описание задачи", Status.NEW);
        manager.newTask(task);
        Epic epic = new Epic(2, "Эпик", "Описание эпика", Status.DONE, new ArrayList<>());
        manager.newEpic(epic);
        Subtask subtask = new Subtask(3, "Подзадача", "Описание подзадачи", Status.NEW, 2);
        manager.newSubtasks(subtask);

        manager.deleteTaskByID(1);
        manager.deleteSubtaskByID(3);
        manager.deleteEpicByID(2);

        assertNull(manager.searchTaskByID(1));
        assertNull(manager.searchEpicByID(2));
        assertNull(manager.searchSubtaskByID(3));
    }

    @Test
    public void inMemoryTaskManagerCanUpdateTasks() {
        Task task = new Task(1, "Задача", "Описание задачи", Status.NEW);
        manager.newTask(task);
        Epic epic = new Epic(2, "Эпик", "Описание эпика", Status.DONE, new ArrayList<>());
        manager.newEpic(epic);
        Subtask subtask = new Subtask(3, "Подзадача", "Описание подзадачи", Status.NEW, 2);
        manager.newSubtasks(subtask);

        task.setName("1");
        epic.setDescription("2");
        subtask.setStatus(Status.IN_PROGRESS);

        manager.updateTask(task);
        manager.updateEpic(epic);
        manager.updateSubtask(subtask);

        assertEquals(task.getName(), manager.searchTaskByID(1).getName());
        assertEquals(epic.getDescription(), manager.searchEpicByID(2).getDescription());
        assertEquals(subtask.getStatus(), manager.searchSubtaskByID(3).getStatus());
    }

    @Test
    public void inMemoryTaskManagerCanPrintTasks() {
        manager.newTask(new Task(1, "Задача", "Описание задачи", Status.NEW));
        manager.newEpic(new Epic(2, "Эпик", "Описание эпика", Status.DONE, new ArrayList<>()));
        manager.newSubtasks(new Subtask(3, "Подзадача", "Описание подзадачи", Status.NEW, 2));

        assertNotNull(manager.printAllTasks());
        assertNotNull(manager.printAllEpics());
        assertNotNull(manager.printAllSubtasks());

    }
}