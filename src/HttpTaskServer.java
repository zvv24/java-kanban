import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServer {
    private static final int PORT = 8080;
    HttpServer httpServer;
    TaskManager taskManager;
    Gson gson;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.gson = new Gson();
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.createContext("/epics", new EpicHandler());
        httpServer.createContext("/subtasks", new SubtaskHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
    }

    public void start() {
        httpServer.start();
        System.out.println("Сервер запущен");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Сервер остановлен");
    }

    class BaseHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange h) throws IOException {
            String method = h.getRequestMethod();
            String path = h.getRequestURI().getPath();
            try {
                switch (method) {
                    case "GET" -> handleGet(h, path);
                    case "POST" -> handlePost(h, path);
                    case "DELETE" -> handleDelete(h, path);
                }
            } catch (Exception e) {
                sendServerError(h);
            }
        }

        protected void handleGet(HttpExchange h, String path) throws IOException {
        }

        protected void handlePost(HttpExchange h, String path) throws IOException {
        }

        protected void handleDelete(HttpExchange h, String path) throws IOException {
        }

        protected void sendText(HttpExchange h, String text) throws IOException {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(200, resp.length);
            h.getResponseBody().write(resp);
            h.close();
        }

        protected void sendCreate(HttpExchange h, String text) throws IOException {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(201, resp.length);
            h.getResponseBody().write(resp);
            h.close();
        }

        protected void sendNotFound(HttpExchange h) throws IOException {
            String resp = "Задача не найдена";
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(404, resp.getBytes().length);
            h.getResponseBody().write(resp.getBytes());
            h.close();
        }

        protected void sendHasInteractions(HttpExchange h) throws IOException {
            String resp = "Задачи пересекаются";
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(406, resp.getBytes().length);
            h.getResponseBody().write(resp.getBytes());
            h.close();
        }

        protected void sendServerError(HttpExchange h) throws IOException {
            String resp = "Ошибка сервера";
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(500, resp.getBytes().length);
            h.getResponseBody().write(resp.getBytes());
            h.close();
        }
    }

    class TaskHandler extends BaseHttpHandler {
        @Override
        protected void handleGet(HttpExchange h, String path) throws IOException {
            String[] split = path.split("/");
            if (split.length == 2 && split[1].equals("tasks")) {
                List<Task> tasks = taskManager.printAllTasks();
                sendText(h, gson.toJson(tasks));
            } else if (split.length == 3 && split[1].equals("tasks")) {
                int id = Integer.parseInt(split[2]);
                Task task = taskManager.searchTaskByID(id);
                if (task != null) {
                    sendText(h, gson.toJson(task));
                } else {
                    sendNotFound(h);
                }
            } else {
                sendNotFound(h);
            }
        }

        @Override
        protected void handlePost(HttpExchange h, String path) throws IOException {
            String[] split = path.split("/");
            if (split.length == 2 && split[1].equals("tasks")) {
                String body;
                try (InputStream inputStream = h.getRequestBody()) {
                    body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    sendServerError(h);
                    return;
                }
                Task task = gson.fromJson(body, Task.class);
                try {
                    Task task1 = taskManager.newTask(task);
                    sendCreate(h, gson.toJson(task1));
                } catch (IllegalArgumentException e) {
                    sendHasInteractions(h);
                }
            } else if (split.length == 3 && split[1].equals("tasks")) {
                String body;
                try (InputStream inputStream = h.getRequestBody()) {
                    body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    sendServerError(h);
                    return;
                }
                Task task = gson.fromJson(body, Task.class);
                try {
                    Task task1 = taskManager.updateTask(task);
                    sendCreate(h, gson.toJson(task1));
                } catch (IllegalArgumentException e) {
                    sendHasInteractions(h);
                }
            } else {
                sendNotFound(h);
            }
        }

        @Override
        protected void handleDelete(HttpExchange h, String path) throws IOException {
            String[] split = path.split("/");
            if (split.length == 2 && split[1].equals("tasks")) {
                taskManager.deleteAllTasks();
                sendText(h, "Все задачи удалены");
            } else if (split.length == 3 && split[1].equals("tasks")) {
                int id = Integer.parseInt(split[2]);
                Task task = taskManager.deleteTaskByID(id);
                if (task != null) {
                    sendText(h, "Задача удалена");
                } else {
                    sendNotFound(h);
                }
            } else {
                sendNotFound(h);
            }
        }
    }

    class EpicHandler extends BaseHttpHandler {
        @Override
        protected void handleGet(HttpExchange h, String path) throws IOException {
            String[] split = path.split("/");
            if (split.length == 2 && split[1].equals("epics")) {
                List<Epic> epics = taskManager.printAllEpics();
                sendText(h, gson.toJson(epics));
            } else if (split.length == 3 && split[1].equals("epics")) {
                int id = Integer.parseInt(split[2]);
                Epic epic = taskManager.searchEpicByID(id);
                if (epic != null) {
                    sendText(h, gson.toJson(epic));
                } else {
                    sendNotFound(h);
                }
            } else if (split.length == 4 && split[1].equals("epics")) {
                int id = Integer.parseInt(split[2]);
                List<Subtask> subtasks = taskManager.printSubtasksСertainEpic(id);
                if (subtasks != null) {
                    sendText(h, gson.toJson(subtasks));
                } else {
                    sendNotFound(h);
                }
            } else {
                sendNotFound(h);
            }
        }

        @Override
        protected void handlePost(HttpExchange h, String path) throws IOException {
            String[] split = path.split("/");
            if (split.length == 2 && split[1].equals("epics")) {
                String body;
                try (InputStream inputStream = h.getRequestBody()) {
                    body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    sendServerError(h);
                    return;
                }
                Epic epic = gson.fromJson(body, Epic.class);
                try {
                    Epic epic1 = taskManager.newEpic(epic);
                    sendCreate(h, gson.toJson(epic1));
                } catch (IllegalArgumentException e) {
                    sendHasInteractions(h);
                }
            } else {
                sendNotFound(h);
            }
        }

        @Override
        protected void handleDelete(HttpExchange h, String path) throws IOException {
            String[] split = path.split("/");
            if (split.length == 2 && split[1].equals("epics")) {
                taskManager.deleteAllEpics();
                sendText(h, "Все эпики удалены");
            } else if (split.length == 3 && split[1].equals("epics")) {
                int id = Integer.parseInt(split[2]);
                Epic epic = taskManager.deleteEpicByID(id);
                if (epic != null) {
                    sendText(h, "Эпик удален");
                } else {
                    sendNotFound(h);
                }
            } else {
                sendNotFound(h);
            }
        }
    }

    class SubtaskHandler extends BaseHttpHandler {
        @Override
        protected void handleGet(HttpExchange h, String path) throws IOException {
            String[] split = path.split("/");
            if (split.length == 2 && split[1].equals("subtask")) {
                List<Subtask> subtasks = taskManager.printAllSubtasks();
                sendText(h, gson.toJson(subtasks));
            } else if (split.length == 3 && split[1].equals("subtask")) {
                int id = Integer.parseInt(split[2]);
                Subtask subtask = taskManager.searchSubtaskByID(id);
                if (subtask != null) {
                    sendText(h, gson.toJson(subtask));
                } else {
                    sendNotFound(h);
                }
            } else {
                sendNotFound(h);
            }
        }

        @Override
        protected void handlePost(HttpExchange h, String path) throws IOException {
            String[] split = path.split("/");
            if (split.length == 2 && split[1].equals("subtasks")) {
                String body;
                try (InputStream inputStream = h.getRequestBody()) {
                    body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    sendServerError(h);
                    return;
                }
                Subtask subtask = gson.fromJson(body, Subtask.class);
                try {
                    Subtask subtask1 = taskManager.newSubtasks(subtask);
                    sendCreate(h, gson.toJson(subtask1));
                } catch (IllegalArgumentException e) {
                    sendHasInteractions(h);
                }
            } else if (split.length == 3 && split[1].equals("subtasks")) {
                String body;
                try (InputStream inputStream = h.getRequestBody()) {
                    body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    sendServerError(h);
                    return;
                }
                Subtask subtask = gson.fromJson(body, Subtask.class);
                try {
                    Subtask subtask1 = taskManager.updateSubtask(subtask);
                    sendCreate(h, gson.toJson(subtask1));
                } catch (IllegalArgumentException e) {
                    sendHasInteractions(h);
                }
            } else {
                sendNotFound(h);
            }
        }

        @Override
        protected void handleDelete(HttpExchange h, String path) throws IOException {
            String[] split = path.split("/");
            if (split.length == 2 && split[1].equals("subtasks")) {
                taskManager.deleteAllSubtasks();
                sendText(h, "Все подзадачи удалены");
            } else if (split.length == 3 && split[1].equals("subtasks")) {
                int id = Integer.parseInt(split[2]);
                Subtask subtask = taskManager.deleteSubtaskByID(id);
                if (subtask != null) {
                    sendText(h, "Подзадача удалена");
                } else {
                    sendNotFound(h);
                }
            } else {
                sendNotFound(h);
            }
        }
    }

    class HistoryHandler extends BaseHttpHandler {
        @Override
        protected void handleGet(HttpExchange h, String path) throws IOException {
            String[] split = path.split("/");
            if (split.length == 2 && split[1].equals("history")) {
                List<Task> history = taskManager.getHistory();
                sendText(h, gson.toJson(history));
            } else {
                sendNotFound(h);
            }
        }
    }

    class PrioritizedHandler extends BaseHttpHandler {
        @Override
        protected void handleGet(HttpExchange h, String path) throws IOException {
            String[] split = path.split("/");
            if (split.length == 2 && split[1].equals("prioritized")) {
                List<Task> prioritized = new ArrayList<>(taskManager.getPrioritizedTasks());
                sendText(h, gson.toJson(prioritized));
            } else {
                sendNotFound(h);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager1 = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(taskManager1);
        server.start();
    }
}
