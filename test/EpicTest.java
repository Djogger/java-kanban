import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private static Epic epic1;
    private static Epic epic2;
    private static Epic epic3;

    @BeforeAll
    public static void before() {
        epic1 = new Epic("Эпик 1", "Описание первого эпика.");
        epic2 = new Epic("Эпик 2", "Описание второго эпика.");
        epic3 = new Epic("Эпик 2", "Описание третьего эпика.", 1);
    }

    @Test
    public void shouldBeTrueIfEpicsHaveSameId() {
        assertEquals(epic1, epic2);
    }

    @Test
    public void shouldBeTrueIfEpicsHaveNotSameId() {
        assertNotEquals(epic1, epic3);
    }
}