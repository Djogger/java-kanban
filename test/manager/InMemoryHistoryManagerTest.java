package manager;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.*;
import task.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private static TaskManager<Task> taskManager;
    private static Task task1;
    private static Epic epic1;
    private static Subtask subtask1;

    @BeforeAll
    public static void before() {
        taskManager = Managers.getDefault();

        task1 = new Task("Задача 1", "Описание первого таска.", Statuses.NEW);

        taskManager.createTask(task1);

        epic1 = new Epic("Эпик 1", "Описание первого эпика.");

        taskManager.createEpic(epic1);

        subtask1 = new Subtask("Сабтаск 1", "Описание первого сабтаска.", 2, Statuses.NEW);

        taskManager.createAndAddSubtask(subtask1);
    }

    @Test
    public void shouldReturnHistoryListWithSameTasksAndOrder() {
        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubtask(3);

        ArrayList<Task> tasksList = new ArrayList<>();
        tasksList.add(task1);
        tasksList.add(epic1);
        tasksList.add(subtask1);

        assertEquals(tasksList, taskManager.getHistory());
    }

}