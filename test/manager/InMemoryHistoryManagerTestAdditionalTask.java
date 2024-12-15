package manager;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.NotFoundException;
import org.junit.jupiter.api.*;
import task.Epic;
import task.Statuses;
import task.Subtask;
import task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManagerTestAdditionalTask {
    private static TaskManager<Task> taskManager;

    @BeforeAll
    public static void before() {
        // 1) Создать ряд задач, эпиков и сабтасков по первому пункту:
        taskManager = Managers.getDefault();

        Task task1 = new Task("Задача 1", "Описание первого таска.", Statuses.NEW);
        Task task2 = new Task("Задача 2", "Описание второго таска.", Statuses.NEW);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание первого эпика.");

        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Сабтаск 1", "Описание первого сабтаска.", 3, Statuses.NEW);
        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание второго сабтаска.", 3, Statuses.NEW);
        Subtask subtask3 = new Subtask("Сабтаск 3", "Описание второго сабтаска.", 3, Statuses.NEW);

        taskManager.createAndAddSubtask(subtask1);
        taskManager.createAndAddSubtask(subtask2);
        taskManager.createAndAddSubtask(subtask3);

        Epic epic2 = new Epic("Эпик 2", "Описание второго эпика.");

        taskManager.createEpic(epic2);
    }

    @Test
    public void shouldHaveNoDuplicatesInHistory() {
        try {
            taskManager.getTask(1);
            taskManager.getTask(2);
            taskManager.getTask(1);

            taskManager.getSubtask(5);
            taskManager.getSubtask(4);
            taskManager.getSubtask(5);

            taskManager.getEpic(3);

            int duplicates = 0;

            ArrayList<Task> tasksHistory = (ArrayList<Task>) taskManager.getHistory();

            int iteration = 1;

            for (Task task : tasksHistory) {
                int taskId = task.getIdentificationNumber();

                for (int i = iteration; i < tasksHistory.size(); i++) {
                    if (taskId == tasksHistory.get(i).getIdentificationNumber()) {
                        duplicates++;
                    }
                }

                iteration += 1;
            }

            assertEquals(0, duplicates);
        } catch (NotFoundException ex) {
            System.out.println(ex);
        }
    }

    @Test
    public void shouldHaveNoIdOfDeletedTaskInHistory() {
        taskManager.deleteTask(1);
        taskManager.deleteEpic(3);

        ArrayList<Task> tasksHistory = (ArrayList<Task>) taskManager.getHistory();

        if (tasksHistory.size() == 1) {
            int taskId = tasksHistory.get(0).getIdentificationNumber();

            assertNotEquals(1, taskId);
            assertNotEquals(3, taskId);
            assertNotEquals(4, taskId);
            assertNotEquals(5, taskId);

            assertEquals(2, taskId);
        }
    }
}
