package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    protected final TaskManager taskManager;
    protected final Gson gson;

    public BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        try {
            switch (method) {
                case "GET":
                    handleGet(h);
                    break;
                case "POST":
                    handlePost(h);
                    break;
                case "DELETE":
                    handleDelete(h);
                    break;
                default:
                    sendNotFound(h, "Метод не поддерживается");
            }
        } catch (Exception e) {
            sendServerError(h);
        }
    }

    protected abstract void handleGet(HttpExchange h) throws IOException;

    protected abstract void handlePost(HttpExchange h) throws IOException;

    protected abstract void handleDelete(HttpExchange h) throws IOException;

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

    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
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