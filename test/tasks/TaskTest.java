package tasks;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    @Test
    public void testsAreEqualIfTheIDsAreEqual() {
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(10));
        Task task2 = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(10));
        assertEquals(task2, task1, "Задачи не равны");
    }
}