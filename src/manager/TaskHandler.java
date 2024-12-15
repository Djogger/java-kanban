package manager;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    public TaskHandler(TaskManager<Task> taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response;

        String method = httpExchange.getRequestMethod();

        switch (method) {
            case "POST":
                response = handlePostRequest(httpExchange);
                break;
            case "GET":
                response = handleGetRequest(httpExchange);
                break;
            case "DELETE":
                response = handleDeleteRequest(httpExchange);
                break;
            default:
                response = "Некорректный метод";
        }

        if (response.equals("error")) {
            sendHasInteractions(httpExchange, "Задача пересекается с существующей");
        } else if (response.equals("null")) {
            sendNotFound(httpExchange, "Искомая задача не была найдена");
        } else if (response.equals("Некорректный метод")) {
            sendNoSuchMethod(httpExchange, response);
        } else {
            sendText(httpExchange, response);
        }
    }

    protected String handlePostRequest(HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Task task = gson.fromJson(body, Task.class);

        JsonObject jsonObject = super.gson.fromJson(body, JsonObject.class);

        try {
            int identificationNumber = jsonObject.has("identificationNumber") ?
                    jsonObject.get("identificationNumber").getAsInt() : 0;

            if (identificationNumber != 0) {
                taskManager.updateTask(task);
                return "Задача обновлена";
            }

            taskManager.createTask(task);
            return "Задача создана с id: " + taskManager.getIdOfLastCreatedTask();
        } catch (IllegalArgumentException ex) {
            return "error";
        }
    }

    protected String handleGetRequest(HttpExchange httpExchange) {
        String path = httpExchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");

        if (splitPath.length > 2) {
            try {
                String id = splitPath[2];

                Task task = taskManager.getTask(Integer.parseInt(id));

                return gson.toJson(task);
            } catch (NotFoundException ex) {
                return "null";
            }
        }

        List<? extends Task> listOfTasks = taskManager.getTasks();

        return gson.toJson(listOfTasks);
    }

    protected String handleDeleteRequest(HttpExchange httpExchange) {
        String path = httpExchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");

        String id = splitPath[2];

        taskManager.deleteTask(Integer.parseInt(id));

        return "Задача удалена";
    }
}
