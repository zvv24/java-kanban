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

public class PrioritizedHandlerTest extends HttpTaskServerTest {

    @Test
    public void gettingPrioritized() throws Exception {
        taskManager.newTask(new Task(1, "Задача 1", "Описание задачи 1", Status.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(10)));
        taskManager.newTask(new Task(2, "Задача 2", "Описание задачи 2", Status.NEW,
                LocalDateTime.now().plusHours(2),
                Duration.ofMinutes(10)));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> prioritized = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());
        assertEquals(2, prioritized.size());
        assertEquals("Задача 1", prioritized.getFirst().getName());
    }
}
