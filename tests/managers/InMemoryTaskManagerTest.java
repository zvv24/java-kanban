package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    InMemoryTaskManager manager;
    Task task = new Task(1, "Задача", "Описание задачи", Status.NEW);
    Epic epic = new Epic(2, "Эпик", "Описание эпика", Status.DONE, new ArrayList<>());
    Subtask subtask1 = new Subtask(3, "Подзадача", "Описание подзадачи", Status.NEW, 2);
    Subtask subtask2 = (new Subtask(4, "Подзадача 2", "Подзадача эпика 1", Status.IN_PROGRESS, 2));


    @BeforeEach
    void beforeEach() {
        manager = new InMemoryTaskManager();
        manager.newTask(task);
        manager.newEpic(epic);
        manager.newSubtasks(subtask1);
        manager.newSubtasks(subtask2);
    }

    @Test
    public void inMemoryTaskManagerCanAddAndSearchForDifferentTasks() {
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
    public void deletingSubtasksTheyShouldBeDeletedFromTheEpics() {
        manager.deleteAllSubtasks();
        assertTrue(epic.getSubtask().isEmpty());
    }

    @Test
    public void deletingAllEpicsDeletedAllSubtasks() {
        manager.deleteAllEpics();
        assertTrue(manager.printAllSubtasks().isEmpty());
    }

    @Test
    public void printSubtasksOfCertainEpic() {
        ArrayList<Subtask> subtasks = manager.printSubtasksСertainEpic(2);
        assertEquals(manager.printAllSubtasks(), subtasks);
    }

    @Test
    public void inMemoryTaskManagerCanDeleteTasksById() {
        manager.deleteTaskByID(1);
        manager.deleteSubtaskByID(3);
        manager.deleteEpicByID(2);

        assertNull(manager.searchTaskByID(1));
        assertNull(manager.searchEpicByID(2));
        assertNull(manager.searchSubtaskByID(3));
    }

    @Test
    public void inMemoryTaskManagerCanUpdateTasks() {
        task.setName("1");
        epic.setDescription("2");
        subtask1.setStatus(Status.IN_PROGRESS);

        manager.updateTask(task);
        manager.updateEpic(epic);
        manager.updateSubtask(subtask1);

        assertEquals(task.getName(), manager.searchTaskByID(1).getName());
        assertEquals(epic.getDescription(), manager.searchEpicByID(2).getDescription());
        assertEquals(subtask1.getStatus(), manager.searchSubtaskByID(3).getStatus());
    }

    @Test
    public void inMemoryTaskManagerCanPrintTasks() {
        assertTrue(manager.printAllTasks().contains(task));
        assertTrue(manager.printAllEpics().contains(epic));
        assertTrue(manager.printAllSubtasks().contains(subtask1));
        assertTrue(manager.printAllSubtasks().contains(subtask2));
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
}