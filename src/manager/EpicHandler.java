package manager;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import task.Epic;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends TaskHandler {
    public EpicHandler(TaskManager<Task> taskManager) {
        super(taskManager);
    }

    @Override
    protected String handlePostRequest(HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Epic epic = super.gson.fromJson(body, Epic.class);

        JsonObject jsonObject = super.gson.fromJson(body, JsonObject.class);

        try {
            int identificationNumber = jsonObject.has("identificationNumber") ?
                    jsonObject.get("identificationNumber").getAsInt() : 0;

            if (identificationNumber != 0) {
                taskManager.updateEpic(epic);
                return "Эпик обновлён";
            }

            taskManager.createEpic(epic);
            return "Эпик создан c id: " + taskManager.getIdOfLastCreatedTask();
        } catch (IllegalArgumentException ex) {
            return "error";
        }
    }

    @Override
    protected String handleGetRequest(HttpExchange httpExchange) {
        String path = httpExchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");

        String id = splitPath[2];

        try {
            Epic epic = taskManager.getEpic(Integer.parseInt(id));

            if (splitPath.length == 4) {
                return super.gson.toJson(taskManager.getEpicSubtasks(epic));
            } else if (splitPath.length == 3) {
                return super.gson.toJson(epic);
            }
        } catch (NotFoundException ex) {
            return "null";
        }

        List<? extends Task> listOfTasks = taskManager.getEpics();

        return gson.toJson(listOfTasks);
    }

    @Override
    protected String handleDeleteRequest(HttpExchange httpExchange) {
        String path = httpExchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");

        String id = splitPath[2];

        taskManager.deleteEpic(Integer.parseInt(id));

        return "Задача удалена";
    }
}
