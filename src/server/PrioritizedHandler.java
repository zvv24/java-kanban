package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {
    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected void handleGet(HttpExchange h) throws IOException {
        String path = h.getRequestURI().getPath();
        String[] split = path.split("/");

        if (split.length == 2) {
            List<Task> prioritized = taskManager.getPrioritizedTasks().stream().toList();
            sendText(h, gson.toJson(prioritized));
        } else {
            sendNotFound(h, "Приоритетные задачи не найдены");
        }
    }

    @Override
    protected void handlePost(HttpExchange h) throws IOException {
        sendNotFound(h, "Метод не поддерживает POST");
    }

    @Override
    protected void handleDelete(HttpExchange h) throws IOException {
        sendNotFound(h, "Метод не поддерживает DELETE");
    }
}