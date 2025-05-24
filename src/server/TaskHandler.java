package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected void handleGet(HttpExchange h) throws IOException {
        String path = h.getRequestURI().getPath();
        String[] split = path.split("/");
        if (split.length == 2) {
            List<Task> tasks = taskManager.printAllTasks();
            sendText(h, gson.toJson(tasks));
        } else if (split.length == 3) {
            int id = Integer.parseInt(split[2]);
            Task task = taskManager.searchTaskByID(id);
            if (task != null) {
                sendText(h, gson.toJson(task));
            } else {
                sendNotFound(h, "Задача не найдена");
            }
        } else {
            sendNotFound(h, "Задача не найдена");
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

        Task task = gson.fromJson(body, Task.class);
        try {
            if (split.length == 2) {
                taskManager.newTask(task);
                sendCreate(h, "Задача создана");
            } else if (split.length == 3) {
                taskManager.updateTask(task);
                sendCreate(h, "Задача обновлена");
            } else {
                sendNotFound(h, "Задача не найдена");
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
            taskManager.deleteAllTasks();
            sendText(h, "Все задачи удалены");
        } else if (split.length == 3) {
            int id = Integer.parseInt(split[2]);
            Task task = taskManager.deleteTaskByID(id);
            if (task != null) {
                sendText(h, "Задача удалена");
            } else {
                sendNotFound(h, "Задача не найдена");
            }
        } else {
            sendNotFound(h, "Задача не найдена");
        }
    }
}