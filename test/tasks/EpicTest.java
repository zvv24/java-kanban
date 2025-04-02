package tasks;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    @Test
    public void epicsAreEqualIfTheIDsAreEqual() {
        Epic epic1 = new Epic(1, "Эпик 1", "Описание эпика 1", Status.NEW, new ArrayList<>());
        Epic epic2 = new Epic(1, "Эпик 1", "Описание эпика 1", Status.NEW, new ArrayList<>());
        assertEquals(epic2, epic1, "Эпики не равны");
    }
}