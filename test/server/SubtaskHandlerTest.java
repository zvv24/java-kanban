package server;

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

public class SubtaskHandlerTest extends HttpTaskServerTest {

    @Test
    public void creatingSubtask() throws Exception {
        Epic epic = taskManager.newEpic(new Epic(1, "Эпик", "Описание эпика", Status.NEW,
                new ArrayList<>()));
        String subtask = gson.toJson(new Subtask(2, "подзадача 1", "Описание подзадачи 1", Status.NEW, 1,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(20)));

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
}
