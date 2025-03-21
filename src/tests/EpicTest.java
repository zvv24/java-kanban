package tests;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    public void epicsAreEqualIfTheIDsAreEqual() {
        Epic epic1 = new Epic(3, "Эпик 1", "Описание эпика 1", Status.NEW, new ArrayList<>());
        Epic epic2 = epic1;
        assertEquals(epic2, epic1, "Эпики не равны");
    }

    @Test
    public void deletingAllEpicsDeletedAllSubtasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic1 = new Epic(1, "Эпик 1", "В нем должно быть 2 подзадачи", Status.NEW, new ArrayList<>());
        manager.newEpic(epic1);
        Subtask subtask1 = new Subtask(2, "Подзадача 1", "Подзадача эпика 1", Status.NEW, 1);
        manager.newSubtasks(subtask1);
        Subtask subtask2 = new Subtask(3, "Подзадача 2", "Подзадача эпика 1", Status.IN_PROGRESS, 1);
        manager.newSubtasks(subtask2);

        manager.deleteAllEpics();
        List<Subtask> subtasks = manager.printAllSubtasks();
        assertTrue(subtasks.isEmpty());
    }
}