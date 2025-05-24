package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected void handleGet(HttpExchange h) throws IOException {
        String path = h.getRequestURI().getPath();
        String[] split = path.split("/");

        if (split.length == 2) {
            List<Task> history = taskManager.getHistory();
            sendText(h, gson.toJson(history));
        } else {
            sendNotFound(h, "История не найдена");
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