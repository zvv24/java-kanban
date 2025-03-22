package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    //Создание нового объекта
    Task newTask(Task task);

    Epic newEpic(Epic epic);

    Subtask newSubtasks(Subtask subtask);

    //Получение списка.
    List<Task> printAllTasks();

    List<Epic> printAllEpics();

    List<Subtask> printAllSubtasks();

    //удаление всех объектов
    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    //Поиск по ID.
    Task searchTaskByID(int id);

    Epic searchEpicByID(int id);

    Subtask searchSubtaskByID(int id);

    //Обновление.
    Task updateTask(Task task);

    Epic updateEpic(Epic epic);

    Subtask updateSubtask(Subtask subtask);

    //Удаление по ID.
    Task deleteTaskByID(int id);

    Epic deleteEpicByID(int id);

    Subtask deleteSubtaskByID(int id);

    //Получение списка всех подзадач определённого эпика.
    ArrayList<Subtask> printSubtasksСertainEpic(int id);

    List<Task> getHistory();
}
