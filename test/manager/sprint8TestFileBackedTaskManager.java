package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Statuses;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class sprint8TestFileBackedTaskManager extends TaskManagerTest<FileBackedTaskManager<Task>> {
    @Override
    protected FileBackedTaskManager<Task> createTaskManager() {
        return new FileBackedTaskManager<>("file.txt");
    }

    @Test
    public void checkOverlappingForIntervalsInTasks() {
        FileBackedTaskManager<Task> taskManager = new FileBackedTaskManager<>("file.txt");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd:MM:yy");

        Task task1 = new Task("Задача 1", "Описание первого таска.", Statuses.NEW, LocalDateTime.parse("04:23 04:12:24", dateTimeFormatter), Duration.ofMinutes(65));
        Task task2 = new Task("Задача 2", "Описание второго таска.", Statuses.DONE, LocalDateTime.parse("18:45 04:12:24", dateTimeFormatter), Duration.ofMinutes(21));

        assertDoesNotThrow(() -> {
            taskManager.createTask(task1);
            taskManager.createTask(task2);
        });

        Task task3 = new Task("Задача 1", "Описание первого таска.", Statuses.NEW, LocalDateTime.parse("04:23 04:12:24", dateTimeFormatter), Duration.ofMinutes(65));
        Task task4 = new Task("Задача 2", "Описание второго таска.", Statuses.DONE, LocalDateTime.parse("04:45 04:12:24", dateTimeFormatter), Duration.ofMinutes(21));

        assertThrows(IllegalArgumentException.class, () -> {
            taskManager.createTask(task3);
            taskManager.createTask(task4);
        });
    }
}
