package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

class TaskTest {

    @Test
    public void testsAreEqualIfTheIDsAreEqual() {
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = task1;
        assertEquals(task2, task1, "Задачи не равны");
    }

}