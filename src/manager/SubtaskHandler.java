package manager;

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
        String path = httpExchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");

        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Subtask subtask = super.gson.fromJson(body, Subtask.class);

        try {
            if (splitPath.length >= 3) {
                taskManager.updateSubtask(subtask);

                return "Задача обновлена";
            }

            taskManager.createAndAddSubtask(subtask);

            return "Задача создана";
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

        List<? extends Task> listOfTasks = taskManager.getEpics();

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
