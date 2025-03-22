package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskTest {

    @Test
    public void subtestsAreEqualIfTheIDsAreEqual() {
        Subtask subtask1 = new Subtask(2, "подзадача 1", "Описание подзадачи 1", Status.NEW, 1);
        Subtask subtask2 = new Subtask(2, "подзадача 1", "Описание подзадачи 1", Status.NEW, 1);
        assertEquals(subtask2, subtask1, "Подзадачи не равны");
    }

}