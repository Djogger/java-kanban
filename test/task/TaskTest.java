package task;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private static Task task1;
    private static Task task2;
    private static Task task3;

    @BeforeAll
    public static void before() {
        task1 = new Task("Таск 1", "Описание первого таска.", Statuses.NEW);
        task2 = new Task("Таск 2", "Описание второго таска.", Statuses.NEW);
        task3 = new Task("Таск 3", "Описание третьего таска.", 1, Statuses.NEW);
    }

    @Test
    public void shouldBeTrueIfTasksHaveSameId() {
        assertEquals(task1, task2);
    }

    @Test
    public void shouldBeTrueIfTasksHaveNotSameId() {
        assertNotEquals(task1, task3);
    }
}