package manager;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends TaskHandler {
    public SubtaskHandler(TaskManager<Task> taskManager) {
        super(taskManager);
    }

    @Override
    protected String handlePostRequest(HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Subtask subtask = super.gson.fromJson(body, Subtask.class);

        JsonObject jsonObject = super.gson.fromJson(body, JsonObject.class);

        try {
            int identificationNumber = jsonObject.has("identificationNumber") ?
                    jsonObject.get("identificationNumber").getAsInt() : 0;

            if (identificationNumber != 0) {
                taskManager.updateSubtask(subtask);
                return "Сабтаск обновлён";
            }

            taskManager.createAndAddSubtask(subtask);
            return "Сабтаск создан с id: " + taskManager.getIdOfLastCreatedTask();
        } catch (IllegalArgumentException ex) {
            return "error";
        }
    }

    @Override
    protected String handleGetRequest(HttpExchange httpExchange) {
        String path = httpExchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");

        if (splitPath.length > 2) {
            try {
                String id = splitPath[2];

                Subtask subtask = taskManager.getSubtask(Integer.parseInt(id));

                return super.gson.toJson(subtask);
            } catch (NotFoundException ex) {
                return "null";
            }
        }

        List<? extends Task> listOfTasks = taskManager.getSubtasks();

        return gson.toJson(listOfTasks);
    }

    @Override
    protected String handleDeleteRequest(HttpExchange httpExchange) {
        String path = httpExchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");

        String id = splitPath[2];

        taskManager.deleteSubtask(Integer.parseInt(id));

        return "Задача удалена";
    }
}
