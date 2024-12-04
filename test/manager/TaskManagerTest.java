package manager;

import org.junit.jupiter.api.BeforeAll;
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

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @BeforeEach
    public void beforeEachTest() {
        taskManager = createTaskManager();
    }

    protected abstract T createTaskManager();

    @Test
    public void epicStatusShouldBeNew() {
        Epic epic1 = new Epic("Эпик 1", "Описание первого эпика.");
        Subtask subtask1 = new Subtask("Сабтаск 1", "Описание первого сабтаска.", 1, Statuses.NEW, LocalDateTime.parse("05:21 06:12:24", DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")), Duration.ofMinutes(21));
        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание второго сабтаска.", 1, Statuses.NEW, LocalDateTime.parse("14:05 05:12:24", DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")), Duration.ofMinutes(721));
        Subtask subtask3 = new Subtask("Сабтаск 3", "Описание третьего сабтаска.", 1, Statuses.NEW, LocalDateTime.parse("05:05 09:12:24", DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")), Duration.ofMinutes(21));

        taskManager.createEpic(epic1);
        taskManager.createAndAddSubtask(subtask1);
        taskManager.createAndAddSubtask(subtask2);
        taskManager.createAndAddSubtask(subtask3);

        assertEquals(epic1.getStatus(), Statuses.NEW);
    }

    @Test
    public void epicStatusShouldBeDone() {
        Epic epic1 = new Epic("Эпик 1", "Описание первого эпика.");
        Subtask subtask1 = new Subtask("Сабтаск 1", "Описание первого сабтаска.", 1, Statuses.DONE, LocalDateTime.parse("05:21 06:12:24", DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")), Duration.ofMinutes(21));
        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание второго сабтаска.", 1, Statuses.DONE, LocalDateTime.parse("14:05 05:12:24", DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")), Duration.ofMinutes(721));
        Subtask subtask3 = new Subtask("Сабтаск 3", "Описание третьего сабтаска.", 1, Statuses.DONE, LocalDateTime.parse("05:05 09:12:24", DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")), Duration.ofMinutes(21));

        taskManager.createEpic(epic1);
        taskManager.createAndAddSubtask(subtask1);
        taskManager.createAndAddSubtask(subtask2);
        taskManager.createAndAddSubtask(subtask3);

        assertEquals(epic1.getStatus(), Statuses.DONE);
    }

    @Test
    public void epicStatusShouldBeInProgressTest1() {
        Epic epic1 = new Epic("Эпик 1", "Описание первого эпика.");
        Subtask subtask1 = new Subtask("Сабтаск 1", "Описание первого сабтаска.", 1, Statuses.NEW, LocalDateTime.parse("05:21 06:12:24", DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")), Duration.ofMinutes(21));
        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание второго сабтаска.", 1, Statuses.DONE, LocalDateTime.parse("14:05 05:12:24", DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")), Duration.ofMinutes(721));

        taskManager.createEpic(epic1);
        taskManager.createAndAddSubtask(subtask1);
        taskManager.createAndAddSubtask(subtask2);

        assertEquals(epic1.getStatus(), Statuses.IN_PROGRESS);
    }

    @Test
    public void epicStatusShouldBeInProgressTest2() {
        Epic epic1 = new Epic("Эпик 1", "Описание первого эпика.");
        Subtask subtask1 = new Subtask("Сабтаск 1", "Описание первого сабтаска.", 1, Statuses.IN_PROGRESS, LocalDateTime.parse("05:21 06:12:24", DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")), Duration.ofMinutes(21));
        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание второго сабтаска.", 1, Statuses.IN_PROGRESS, LocalDateTime.parse("14:05 05:12:24", DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")), Duration.ofMinutes(721));
        Subtask subtask3 = new Subtask("Сабтаск 3", "Описание третьего сабтаска.", 1, Statuses.IN_PROGRESS, LocalDateTime.parse("05:05 09:12:24", DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")), Duration.ofMinutes(21));

        taskManager.createEpic(epic1);
        taskManager.createAndAddSubtask(subtask1);
        taskManager.createAndAddSubtask(subtask2);
        taskManager.createAndAddSubtask(subtask3);

        assertEquals(epic1.getStatus(), Statuses.IN_PROGRESS);
    }

    @Test
    public void subtaskShouldReturnEpicId() {
        Epic epic1 = new Epic("Эпик 1", "Описание первого эпика.");
        Epic epic2 = new Epic("Эпик 2", "Описание второго эпика.");
        Subtask subtask1 = new Subtask("Сабтаск 1", "Описание первого сабтаска.", 1, Statuses.IN_PROGRESS, LocalDateTime.parse("05:21 06:12:24", DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")), Duration.ofMinutes(21));
        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание второго сабтаска.", 1, Statuses.IN_PROGRESS, LocalDateTime.parse("14:05 05:12:24", DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")), Duration.ofMinutes(721));
        Subtask subtask3 = new Subtask("Сабтаск 3", "Описание третьего сабтаска.", 2, Statuses.IN_PROGRESS, LocalDateTime.parse("05:05 09:12:24", DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")), Duration.ofMinutes(21));

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createAndAddSubtask(subtask1);
        taskManager.createAndAddSubtask(subtask2);
        taskManager.createAndAddSubtask(subtask3);

        assertEquals(subtask1.getEpicId(), epic1.getIdentificationNumber());
        assertEquals(subtask2.getEpicId(), epic1.getIdentificationNumber());
        assertEquals(subtask3.getEpicId(), epic2.getIdentificationNumber());
    }
}
