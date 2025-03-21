package tests;

import managers.HistoryManager;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ManagersTest {
    @Test
    public void managersReturnInitializedInstances(){
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager, "taskManager не готов к работе ");

        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "historyManager не готов к работе ");
    }
  
}