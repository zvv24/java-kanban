package server;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SubtaskHandlerTest extends HttpTaskServerTest {

    @BeforeEach
    public void creatEpic1() {
        Epic epic = taskManager.newEpic(new Epic(1, "Эпик", "Описание эпика", Status.NEW,
                new ArrayList<>()));
    }

    @AfterEach
    public void deleteEpic1() {
        taskManager.deleteAllEpics();
    }

    @Test
    public void creatingSubtask() throws Exception {
        String subtask = gson.toJson(new Subtask(2, "Подзадача 1", "Описание подзадачи 1", Status.NEW, 1,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(10)));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subtask))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Subtask> subtasks = taskManager.printAllSubtasks();
        assertEquals(1, subtasks.size());
    }

    @Test
    public void gettingSubtaskByID() throws Exception {
        Subtask subtask = new Subtask(2, "Подзадача 1", "Описание подзадачи 1", Status.NEW, 1,
                LocalDateTime.now(), Duration.ofMinutes(10));
        taskManager.newSubtasks(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/" + subtask.getId()))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Subtask subtask1 = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtask.getId(), subtask1.getId());
    }

    @Test
    public void gettingAllTasks() throws Exception {
        Subtask subtask = new Subtask(2, "Подзадача 1", "Описание подзадачи 1", Status.NEW, 1,
                LocalDateTime.now(), Duration.ofMinutes(10));
        Subtask subtask1 = new Subtask(3, "Подзадача 2", "Описание подзадачи 2", Status.NEW, 1,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(10));
        taskManager.newSubtasks(subtask);
        taskManager.newSubtasks(subtask1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> subtasks = gson.fromJson(response.body(), new TypeToken<List<Subtask>>() {
        }.getType());

        assertEquals(200, response.statusCode());
        assertEquals(2, subtasks.size());
    }

    @Test
    public void updatingTask() throws Exception {
        Subtask subtask = new Subtask(2, "подзадача 1", "Описание подзадачи 1", Status.NEW, 1,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(10));
        taskManager.newSubtasks(subtask);
        subtask.setDescription("Новое описание подзадачи 1");
        String subtaskJson = gson.toJson(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/" + subtask.getId()))
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        Subtask subtask1 = taskManager.searchSubtaskByID(subtask.getId());
        assertEquals("Новое описание подзадачи 1", subtask1.getDescription());
    }

    @Test
    public void deletingTask() throws Exception {
        Subtask subtask = new Subtask(2, "подзадача 1", "Описание подзадачи 1", Status.NEW, 1,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(10));
        taskManager.newSubtasks(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/" + subtask.getId()))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(taskManager.searchSubtaskByID(subtask.getId()));
    }

    @Test
    public void deletingAllTasks() throws Exception {
        Subtask subtask = new Subtask(2, "Подзадача 1", "Описание подзадачи 1", Status.NEW, 1,
                LocalDateTime.now(), Duration.ofMinutes(10));
        Subtask subtask1 = new Subtask(3, "Подзадача 2", "Описание подзадачи 2", Status.NEW, 1,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(10));
        taskManager.newSubtasks(subtask);
        taskManager.newSubtasks(subtask1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.printAllSubtasks().size());
    }
}
