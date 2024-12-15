package manager;

import exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Statuses;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class sprint8TestHistoryManager {
    private static TaskManager<Task> taskManager;


    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void shouldReturnEmptyHistoryList() {
        taskManager.getHistory();

        assertEquals(new ArrayList<>(), taskManager.getHistory());
    }

    @Test
    public void shouldHaveNoDuplication() {
        try {
            Task task = new Task("Задача 1", "Описание первого таска.", Statuses.NEW);

            taskManager.createTask(task);

            taskManager.getTask(task.getIdentificationNumber());
            taskManager.getTask(task.getIdentificationNumber());

            assertEquals(taskManager.getHistory().get(0), task);
            assertEquals(taskManager.getHistory().size(), 1);
        } catch (NotFoundException ex) {
            System.out.println(ex);
        }
    }

    @Test
    public void shouldDeleteFromHistoryListWithoutExceptions() {
        Task task1 = new Task("Задача 1", "Описание первого таска.", Statuses.NEW);
        Task task2 = new Task("Задача 1", "Описание первого таска.", Statuses.NEW);
        Task task3 = new Task("Задача 1", "Описание первого таска.", Statuses.NEW);
        Task task4 = new Task("Задача 1", "Описание первого таска.", Statuses.NEW);
        Task task5 = new Task("Задача 1", "Описание первого таска.", Statuses.NEW);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);

        try {
            taskManager.getTask(task1.getIdentificationNumber());
            taskManager.getTask(task2.getIdentificationNumber());
            taskManager.getTask(task3.getIdentificationNumber());
            taskManager.getTask(task4.getIdentificationNumber());
            taskManager.getTask(task5.getIdentificationNumber());
        } catch (NotFoundException ex) {
            System.out.println(ex);
        }

        taskManager.deleteTask(task1.getIdentificationNumber());
        taskManager.deleteTask(task5.getIdentificationNumber());
        taskManager.deleteTask(task3.getIdentificationNumber());

        assertEquals(taskManager.getHistory().get(0), task2);
        assertEquals(taskManager.getHistory().get(1), task4);
        assertEquals(taskManager.getHistory().size(), 2);
    }
}
