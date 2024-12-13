package manager;

import exceptions.NotFoundException;
import manager.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private static TaskManager<Task> taskManager;
    private static Task task1;
    private static Task task2;
    private static Epic epic1;
    private static Epic epic2;
    private static Subtask subtask1;
    private static Subtask subtask2;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();

        task1 = new Task("Задача 1", "Описание первого таска.", Statuses.NEW);
        task2 = new Task("Задача 2", "Описание второго таска.", Statuses.NEW);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        epic1 = new Epic("Эпик 1", "Описание первого эпика.");
        epic2 = new Epic("Эпик 2", "Описание второго эпика.");

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        subtask1 = new Subtask("Сабтаск 1", "Описание первого сабтаска.", 3, Statuses.NEW);
        subtask2 = new Subtask("Сабтаск 2", "Описание второго сабтаска.", 3, Statuses.NEW);

        taskManager.createAndAddSubtask(subtask1);
        taskManager.createAndAddSubtask(subtask2);
    }

    @AfterAll
    public static void afterShouldReturn0BecauseAllListsMustBeEmpty() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubtasks();
        assertEquals(0, taskManager.getTasks().size());
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    public void shouldReturnNotNullFromTasksSubtasksAndEpicsLists() {
        try {
            assertNotNull(taskManager.getTask(1));
            assertNotNull(taskManager.getEpic(3));
            assertNotNull(taskManager.getSubtask(5));
        } catch (NotFoundException ex) {
            System.out.println(ex);
        }
    }

    @Test
    public void shouldBeTrueIfAfterCreateTasksEpicsAndSubtasksNotWithSameId() {
        assertNotEquals(task1, task2);
        assertNotEquals(epic1, epic2);
        assertNotEquals(subtask1, subtask2);
    }

    @Test
    public void shouldReturn2FromTasksEpicsAndSubtasksListsSize() {
        assertEquals(2, taskManager.getTasks().size());
        assertEquals(2, taskManager.getEpics().size());
        assertEquals(2, taskManager.getSubtasks().size());
    }

    @Test
    public void taskEpicAndSubtaskFromListsShouldBeSameAsPreviousBeforeAddingToLists() {
        try {
            assertEquals(task1.getTaskName(), taskManager.getTask(1).getTaskName());
            assertEquals(task1.getDescription(), taskManager.getTask(1).getDescription());
            assertEquals(task1.getIdentificationNumber(), taskManager.getTask(1).getIdentificationNumber());
            assertEquals(task1.getStatus(), taskManager.getTask(1).getStatus());

            assertEquals(epic1.getTaskName(), taskManager.getEpic(3).getTaskName());
            assertEquals(epic1.getDescription(), taskManager.getEpic(3).getDescription());
            assertEquals(epic1.getIdentificationNumber(), taskManager.getEpic(3).getIdentificationNumber());
            assertEquals(epic1.getStatus(), taskManager.getEpic(3).getStatus());

            assertEquals(subtask1.getTaskName(), taskManager.getSubtask(5).getTaskName());
            assertEquals(subtask1.getDescription(), taskManager.getSubtask(5).getDescription());
            assertEquals(subtask1.getIdentificationNumber(), taskManager.getSubtask(5).getIdentificationNumber());
            assertEquals(subtask1.getStatus(), taskManager.getSubtask(5).getStatus());
        } catch (NotFoundException ex) {
            System.out.println(ex);
        }
    }

    @Test
    public void taskEpicAndSubtaskShouldBeNotInTheListAfterDelete() {
        try {
            taskManager.deleteTask(1);
            assertNull(taskManager.getTask(1));

            taskManager.deleteSubtask(5);
            assertNull(taskManager.getSubtask(5));
            assertFalse(taskManager.getEpic(3).getSubtasksId().contains(5));

            taskManager.deleteEpic(3);
            assertNull(taskManager.getEpic(3));
            assertNull(taskManager.getSubtask(5));
            assertNull(taskManager.getSubtask(6));
        } catch (NotFoundException ex) {
            System.out.println(ex);
        }
    }

}