package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler {
    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected void handleGet(HttpExchange h) throws IOException {
        String path = h.getRequestURI().getPath();
        String[] split = path.split("/");

        if (split.length == 2) {
            List<Subtask> subtasks = taskManager.printAllSubtasks();
            sendText(h, gson.toJson(subtasks));
        } else if (split.length == 3) {
            int id = Integer.parseInt(split[2]);
            Subtask subtask = taskManager.searchSubtaskByID(id);
            if (subtask != null) {
                sendText(h, gson.toJson(subtask));
            } else {
                sendNotFound(h, "Подзадача не найдена");
            }
        } else {
            sendNotFound(h, "Подзадача не найдена");
        }
    }

    @Override
    protected void handlePost(HttpExchange h) throws IOException {
        String path = h.getRequestURI().getPath();
        String[] split = path.split("/");
        String body;

        try (InputStream inputStream = h.getRequestBody()) {
            body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        Subtask subtask = gson.fromJson(body, Subtask.class);
        try {
            if (split.length == 2) {
                taskManager.newSubtasks(subtask);
                sendCreate(h, "Подзадача создана");
            } else if (split.length == 3) {
                taskManager.updateSubtask(subtask);
                sendCreate(h, "Подзадача обновлена");
            } else {
                sendNotFound(h, "Подзадача не найдена");
            }
        } catch (IllegalArgumentException e) {
            sendHasInteractions(h);
        }
    }

    @Override
    protected void handleDelete(HttpExchange h) throws IOException {
        String path = h.getRequestURI().getPath();
        String[] split = path.split("/");

        if (split.length == 2) {
            taskManager.deleteAllSubtasks();
            sendText(h, "Все подзадачи удалены");
        } else if (split.length == 3) {
            int id = Integer.parseInt(split[2]);
            Subtask subtask = taskManager.deleteSubtaskByID(id);
            if (subtask != null) {
                sendText(h, "Подзадача удалена");
            } else {
                sendNotFound(h, "Подзадача не найдена");
            }
        } else {
            sendNotFound(h, "Подзадача не найдена");
        }
    }
}