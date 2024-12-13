package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static manager.BaseHttpHandler.sendNoSuchMethod;
import static manager.BaseHttpHandler.sendText;

public class HistoryHandler implements HttpHandler {
    protected static TaskManager<Task> taskManager;
    protected Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public HistoryHandler(TaskManager<Task> taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response;

        String method = httpExchange.getRequestMethod();

        switch (method) {
            case "GET":
                response = handleGetRequest(httpExchange);
                break;
            default:
                response = "Некорректный метод";
        }

        if (response.equals("Некорректный метод")) {
            sendNoSuchMethod(httpExchange, response);
        } else {
            sendText(httpExchange, response);
        }
    }

    protected String handleGetRequest(HttpExchange httpExchange) {
        List<? extends Task> listOfTasks = taskManager.getHistory();

        return gson.toJson(listOfTasks);
    }
}
