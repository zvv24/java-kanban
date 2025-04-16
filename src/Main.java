import managers.InMemoryTaskManager;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task(2, "Задача 2", "Описание задачи 2", Status.NEW);
        Epic epic1 = new Epic(3, "Эпик 1", "В нем должно быть 3 подзадачи", Status.NEW, new ArrayList<>());
        Epic epic2 = new Epic(4, "Эпик 2", "В нем должна быть 0 подзадача", Status.DONE, new ArrayList<>());
        Subtask subtask1 = new Subtask(5, "Подзадача 1", "Подзадача эпика 1", Status.NEW, 3);
        Subtask subtask2 = new Subtask(6, "Подзадача 1", "Подзадача эпика 1", Status.NEW, 3);
        Subtask subtask3 = new Subtask(7, "Подзадача 2", "Подзадача эпика 1", Status.IN_PROGRESS, 3);

        manager.newTask(task1);
        manager.newTask(task2);
        manager.newEpic(epic1);
        manager.newEpic(epic2);
        manager.newSubtasks(subtask1);
        manager.newSubtasks(subtask2);
        manager.newSubtasks(subtask3);

        manager.searchTaskByID(1);
        manager.searchTaskByID(2);
        manager.searchEpicByID(3);
        manager.searchSubtaskByID(5);
        manager.searchSubtaskByID(6);
        manager.searchSubtaskByID(7);
        System.out.println(manager.getHistory());
        System.out.println("----------");

        manager.searchTaskByID(1);
        manager.searchTaskByID(1);
        manager.searchTaskByID(1);
        System.out.println(manager.getHistory());
        System.out.println("----------");

        manager.searchSubtaskByID(7);
        System.out.println(manager.getHistory());
        System.out.println("----------");

        manager.deleteTaskByID(2);
        System.out.println(manager.getHistory());
        System.out.println("----------");

        manager.deleteEpicByID(3);
        System.out.println(manager.getHistory());
        System.out.println("----------");
    }
}