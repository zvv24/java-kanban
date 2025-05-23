package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {
    public EpicHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected void handleGet(HttpExchange h) throws IOException {
        String path = h.getRequestURI().getPath();
        String[] split = path.split("/");

        if (split.length == 2) {
            List<Epic> epics = taskManager.printAllEpics();
            sendText(h, gson.toJson(epics));
        } else if (split.length == 3) {
            int id = Integer.parseInt(split[2]);
            Epic epic = taskManager.searchEpicByID(id);
            if (epic != null) {
                sendText(h, gson.toJson(epic));
            } else {
                sendNotFound(h, "Эпик не найден");
            }
        } else if (split.length == 4 && split[3].equals("subtasks")) {
            int id = Integer.parseInt(split[2]);
            List<Subtask> subtasks = taskManager.printSubtasksCertainEpic(id);
            if (subtasks != null) {
                sendText(h, gson.toJson(subtasks));
            } else {
                sendNotFound(h, "Эпик не найден");
            }
        } else {
            sendNotFound(h, "Эпик не найден");
        }
    }

    @Override
    protected void handlePost(HttpExchange h) throws IOException {
        String path = h.getRequestURI().getPath();
        String[] split = path.split("/");

        if (split.length == 2) {
            String body;
            try (InputStream inputStream = h.getRequestBody()) {
                body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }

            Epic epic = gson.fromJson(body, Epic.class);
            try {
                taskManager.newEpic(epic);
                sendCreate(h, "Эпик создан");
            } catch (IllegalArgumentException e) {
                sendHasInteractions(h);
            }
        } else {
            sendNotFound(h, "Эпик не найден");
        }
    }

    @Override
    protected void handleDelete(HttpExchange h) throws IOException {
        String path = h.getRequestURI().getPath();
        String[] split = path.split("/");

        if (split.length == 2) {
            taskManager.deleteAllEpics();
            sendText(h, "Все эпики удалены");
        } else if (split.length == 3) {
            int id = Integer.parseInt(split[2]);
            Epic epic = taskManager.deleteEpicByID(id);
            if (epic != null) {
                sendText(h, "Эпик удален");
            } else {
                sendNotFound(h, "Эпик не найден");
            }
        } else {
            sendNotFound(h, "Эпик не найден");
        }
    }
}