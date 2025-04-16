package managers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {
    @Test
    public void managersReturnInitializedInstances() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager, "taskManager не готов к работе ");

        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "historyManager не готов к работе ");
    }
}