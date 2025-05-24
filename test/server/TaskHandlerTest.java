package server;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TaskHandlerTest extends HttpTaskServerTest {

    @Test
    public void creatingTask() throws Exception {
        String task = gson.toJson(new Task(1, "Задача 1", "Описание задачи 1", Status.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(10)));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Task> tasks = taskManager.printAllTasks();
        assertEquals(1, tasks.size());
        assertEquals("Задача 1", tasks.getFirst().getName());
    }

    @Test
    public void gettingTaskByID() throws Exception {
        Task task = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(10));
        taskManager.newTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + task.getId()))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Task task1 = gson.fromJson(response.body(), Task.class);
        assertEquals(task.getId(), task1.getId());
    }

    @Test
    public void gettingAllTasks() throws Exception {
        Task task = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(10));
        Task task1 = new Task(1, "Задача 2", "Описание задачи 2", Status.NEW,
                LocalDateTime.now().plusHours(1),
                Duration.ofMinutes(10));
        taskManager.newTask(task);
        taskManager.newTask(task1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());

        assertEquals(200, response.statusCode());
        assertEquals(2, tasks.size());
    }

    @Test
    public void updatingTask() throws Exception {
        Task task = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(10));
        taskManager.newTask(task);
        task.setDescription("Новое описание задачи 1");
        String taskJson = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + task.getId()))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        Task task1 = taskManager.searchTaskByID(task.getId());
        assertEquals("Новое описание задачи 1", task1.getDescription());
    }

    @Test
    public void deletingTask() throws Exception {
        Task task = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(10));
        taskManager.newTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + task.getId()))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(taskManager.searchTaskByID(task.getId()));
    }

    @Test
    public void deletingAllTasks() throws Exception {
        Task task = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(10));
        Task task1 = new Task(2, "Задача 2", "Описание задачи 2", Status.NEW,
                LocalDateTime.now().plusHours(1),
                Duration.ofMinutes(10));
        taskManager.newTask(task);
        taskManager.newTask(task1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.printAllTasks().size());
    }
}
