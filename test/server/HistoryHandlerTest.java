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

public class HistoryHandlerTest extends HttpTaskServerTest {

    @Test
    public void gettingHistory() throws Exception {
        taskManager.newTask(new Task(1, "Задача 1", "Описание задачи 1", Status.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(10)));
        taskManager.searchTaskByID(1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        List<Task> history = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());
        assertEquals(1, history.size());
    }
}
