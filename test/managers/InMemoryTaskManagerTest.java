package managers;

import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryTaskManagerTest extends managers.TaskManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
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
    public void deletesSubtaskFromHistoryManagerIfEpicIsDeleted() {
        manager.searchTaskByID(1);
        manager.searchEpicByID(2);
        manager.searchSubtaskByID(3);
        manager.searchSubtaskByID(4);

        assertEquals(4, manager.getHistory().size());

        manager.deleteEpicByID(2);

        assertEquals(1, manager.getHistory().size());
    }

    @Test
    void calculatingEpicStatusShenAllSubtasksNew() {
        Subtask subtask1 = new Subtask(3, "Подзадача 1", "Описание подзадачи 1", Status.NEW, 2,
                LocalDateTime.now().plusHours(3), Duration.ofMinutes(15));
        Subtask subtask2 = new Subtask(4, "Подзадача 2", "Описание подзадачи 2", Status.NEW, 2,
                LocalDateTime.now().plusHours(4), Duration.ofMinutes(20));
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void calculatingEpicStatusWhenAllSubtasksDone() {
        Subtask subtask1 = new Subtask(3, "Подзадача 1", "Описание подзадачи 1", Status.DONE, 2,
                LocalDateTime.now().plusHours(3), Duration.ofMinutes(15));
        Subtask subtask2 = new Subtask(4, "Подзадача 2", "Описание подзадачи 2", Status.DONE, 2,
                LocalDateTime.now().plusHours(4), Duration.ofMinutes(20));
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void calculatingEpicStatusWhenSubtasksNewAndDone() {
        Subtask subtask1 = new Subtask(3, "Подзадача 1", "Описание подзадачи 1", Status.NEW, 2,
                LocalDateTime.now().plusHours(3), Duration.ofMinutes(15));
        Subtask subtask2 = new Subtask(4, "Подзадача 2", "Описание подзадачи 2", Status.DONE, 2,
                LocalDateTime.now().plusHours(4), Duration.ofMinutes(20));
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void calculatingEpicStatusWhenAllSubtasksInProgress() {
        Subtask subtask1 = new Subtask(3, "Подзадача 1", "Описание подзадачи 1", Status.IN_PROGRESS, 2,
                LocalDateTime.now().plusHours(3), Duration.ofMinutes(15));
        Subtask subtask2 = new Subtask(4, "Подзадача 2", "Описание подзадачи 2", Status.IN_PROGRESS, 2,
                LocalDateTime.now().plusHours(4), Duration.ofMinutes(20));
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}