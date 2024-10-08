package task;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task.Statuses;
import task.Subtask;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    private static Subtask subtask1;
    private static Subtask subtask2;
    private static Subtask subtask3;

    @BeforeAll
    public static void before() {
        subtask1 = new Subtask("Сабтаск 1", "Описание первого сабтаска.", 0, Statuses.NEW);
        subtask2 = new Subtask("Сабтаск 2", "Описание второго сабтаска.", 0, 4, Statuses.NEW);
        subtask3 = new Subtask("Сабтаск 3", "Описание третьего сабтаска.", 1, 5, Statuses.NEW);
    }

    @Test
    public void shouldBeTrueIfSubtasksHaveSameId() {
        assertEquals(subtask1, subtask2);
    }

    @Test
    public void shouldBeTrueIfSubtasksHaveNotSameId() {
        assertNotEquals(subtask1, subtask3);
    }
}