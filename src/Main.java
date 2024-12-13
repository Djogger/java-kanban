import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.NotFoundException;
import manager.Managers;
import task.Epic;
import task.Statuses;
import task.Subtask;

import task.*;
import manager.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Тестировка:
        // 1)
        try {
            System.out.println("1) Первый этап тестировки:");

            TaskManager<Task> taskManager = Managers.getDefault();

            Task task1 = new Task("Задача 1", "Описание первого таска.", Statuses.NEW);
            Task task2 = new Task("Задача 2", "Описание второго таска.", Statuses.NEW);

            taskManager.createTask(task1);
            taskManager.createTask(task2);

            Epic epic1 = new Epic("Эпик 1", "Описание первого эпика.");
            Epic epic2 = new Epic("Эпик 2", "Описание второго эпика.");

            taskManager.createEpic(epic1);
            taskManager.createEpic(epic2);

            Subtask subtask1 = new Subtask("Сабтаск 1", "Описание первого сабтаска.", 3, Statuses.NEW);
            Subtask subtask2 = new Subtask("Сабтаск 2", "Описание второго сабтаска.", 3, Statuses.NEW);
            Subtask subtask3 = new Subtask("Сабтаск 3", "Описание второго сабтаска.", 4, Statuses.NEW);

            taskManager.createAndAddSubtask(subtask1);
            taskManager.createAndAddSubtask(subtask2);
            taskManager.createAndAddSubtask(subtask3);

            List<Task> tasksList = taskManager.getTasks();

            for (Task task : tasksList) {
                System.out.println(task);
            }

            List<Epic> epicsList = taskManager.getEpics();

            for (Epic epic : epicsList) {
                System.out.println(epic);
            }

            List<Subtask> subtasksList = taskManager.getSubtasks();

            for (Subtask subtask : subtasksList) {
                System.out.println(subtask);
            }

            System.out.println("\n");

            // 2)
            System.out.println("2) Второй этап тестировки:");

            Task task3 = new Task("Задача 1", "Описание первого таска.", 0, Statuses.IN_PROGRESS);
            Task task4 = new Task("Задача 2", "Описание второго таска.", 1, Statuses.DONE);

            taskManager.updateTask(task3);
            taskManager.updateTask(task4);

            Subtask subtask4 = new Subtask("Сабтаск 1", "Описание первого сабтаска.", 5, 3, Statuses.NEW);
            Subtask subtask5 = new Subtask("Сабтаск 2", "Описание второго сабтаска.", 6, 3, Statuses.DONE);
            Subtask subtask6 = new Subtask("Сабтаск 3", "Описание второго сабтаска.", 7, 4, Statuses.DONE);

            taskManager.updateSubtask(subtask4);
            taskManager.updateSubtask(subtask5);
            taskManager.updateSubtask(subtask6);

            List<Task> tasksList2 = taskManager.getTasks();

            for (Task task : tasksList2) {
                System.out.println(task);
            }

            List<Epic> epicsList2 = taskManager.getEpics();

            for (Epic epic : epicsList2) {
                System.out.println(epic);
            }

            List<Subtask> subtasksList2 = taskManager.getSubtasks();

            for (Subtask subtask : subtasksList2) {
                System.out.println(subtask);
            }

            System.out.println("\n");

            // 3)
            System.out.println("3) Третий этап тестировки:");

            taskManager.deleteTask(1);
            taskManager.deleteEpic(3);

            taskManager.printAllTasks();

            System.out.println("\n");

            // 4)
            System.out.println("4) Четвёртый этап тестировки:");

            taskManager.getTask(2);
            taskManager.getEpic(4);
            taskManager.getSubtask(7);
            taskManager.getEpic(500);
            List<? extends Task> history = taskManager.getHistory();
            for (Task element : history) {
                System.out.println(element);
            }


            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .create();

            System.out.println(gson.toJson(task1));
        } catch (NotFoundException ex) {
            System.out.println(ex);
        }
    }

}
