package server;

import adapter.DurationAdapter;
import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServerTest {
    protected TaskManager taskManager;
    protected HttpTaskServer httpTaskServer;
    protected Gson gson;
    protected HttpClient httpClient;

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        httpClient = HttpClient.newHttpClient();

        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubtasks();
    }

    @AfterEach
    public void tearDown() {
        httpTaskServer.stop();
    }
}