package manager;

import exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import task.Epic;
import task.Statuses;
import task.Subtask;
import task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManagerTestTwo {
    private static TaskManager<Task> taskManager;

    @BeforeAll
    public static void before() {
        taskManager = Managers.getDefault();

        Task task1 = new Task("Задача 1", "Описание 1 таска.", Statuses.NEW);
        Task task2 = new Task("Задача 2", "Описание второго таска.", Statuses.NEW);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание 1 эпика.");
        Epic epic2 = new Epic("Эпик 2", "Описание второго эпика.");

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Сабтаск 1", "Описание 1 сабтаска.", 3, Statuses.NEW);
        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание второго сабтаска.", 3, Statuses.NEW);
        Subtask subtask3 = new Subtask("Сабтаск 3", "Описание 3 сабтаска.", 4, Statuses.NEW);

        taskManager.createAndAddSubtask(subtask1);
        taskManager.createAndAddSubtask(subtask2);
        taskManager.createAndAddSubtask(subtask3);
    }

    @Test
    public void shouldHaveNoTasksInHistoryAfterAllDeletion() {
        try {
            taskManager.getTask(1);
            taskManager.getTask(2);
            taskManager.getEpic(3);
            taskManager.getEpic(4);
            taskManager.getSubtask(5);
            taskManager.getSubtask(6);
            taskManager.getSubtask(7);

            taskManager.deleteAllTasks();
            taskManager.deleteAllSubtasks();
            taskManager.deleteAllEpics();

            ArrayList<Task> tasksHistory = (ArrayList<Task>) taskManager.getHistory();

            assertEquals(0, tasksHistory.size());
        } catch (NotFoundException ex) {
            System.out.println(ex);
        }
    }
}
