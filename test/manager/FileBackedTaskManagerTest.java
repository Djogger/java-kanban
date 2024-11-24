package manager;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Statuses;
import task.Subtask;
import task.Task;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static manager.FileBackedTaskManager.loadFromFile;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    private static Path filePath;
    private static final FileBackedTaskManager<Task> taskManager = new FileBackedTaskManager<>("C:/Users/User/Desktop/Java Курс/Sprint-4/java-kanban/src/file.txt");
    private static FileBackedTaskManager<Task> taskManager2;

    @BeforeAll
    public static void before() {
        filePath = Paths.get("C:/Users/User/Desktop/Java Курс/Sprint-4/java-kanban/src/file.txt");

        Task task1 = new Task("Задача 1", "Описание первого таска.", Statuses.NEW);
        Task task2 = new Task("Задача 2", "Описание второго таска.", Statuses.DONE);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание первого эпика.");
        Epic epic2 = new Epic("Эпик 2", "Описание второго эпика.");

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Сабтаск 1", "Описание первого сабтаска.", 3, Statuses.NEW);
        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание второго сабтаска.", 3, Statuses.IN_PROGRESS);
        Subtask subtask3 = new Subtask("Сабтаск 3", "Описание третьего сабтаска.", 4, Statuses.NEW);

        taskManager.createAndAddSubtask(subtask1);
        taskManager.createAndAddSubtask(subtask2);
        taskManager.createAndAddSubtask(subtask3);
    }

    @Test
    public void shouldReturn7Insertions() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            int numOfInsertions = 0;

            reader.readLine();

            while (reader.ready()) {
                numOfInsertions++;
                reader.readLine();
            }

            assertEquals(7, numOfInsertions);
        } catch (IOException ex) {
            try {
                throw new ManagerSaveException("Ошибка работы с файлом");
            } catch (ManagerSaveException exc) {
                System.out.println(exc.getMessage());;
            }
        }
    }

    @Test
    public void shouldReturnSameQuantityOfTasksFromFile() {
        taskManager2 = loadFromFile(filePath.toFile());

        int numOfInsertions = 0;

        List<Task> tasksList = taskManager2.getTasks();

        for (Task task : tasksList) {
            numOfInsertions++;
        }

        List<Epic> epicsList = taskManager2.getEpics();

        for (Epic epic : epicsList) {
            numOfInsertions++;
        }

        List<Subtask> subtasksList = taskManager2.getSubtasks();

        for (Subtask subtask : subtasksList) {
            numOfInsertions++;
        }

        assertEquals(7, numOfInsertions);
    }
}
