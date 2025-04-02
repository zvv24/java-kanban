package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    @Test
    public void testsAreEqualIfTheIDsAreEqual() {
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW);
        assertEquals(task2, task1, "Задачи не равны");
    }
}