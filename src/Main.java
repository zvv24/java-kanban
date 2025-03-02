import Managers.TaskManager;
import Tasks.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW);
        Epic epic1 = new Epic("Эпик 1", "В нем должно быть 2 подзадачи", Status.NEW, new ArrayList<>());
        Epic epic2 = new Epic("Эпик 2", "В нем должна быть 1 подзадача", Status.NEW, new ArrayList<>());
        SubTask subTask1 = new SubTask("Подзадача 1", "Подзадача эпика 1", Status.NEW, new ArrayList<>());
        SubTask subTask2 = new SubTask("Подзадача 2", "Подзадача эпика 1", Status.IN_PROGRESS, new ArrayList<>());
        SubTask subTask3 = new SubTask("Подзадача 3", "Подзадача эпика 2", Status.NEW, new ArrayList<>());

        manager.newTask(task1); // 1
        manager.newTask(task2); // 2
        manager.newEpic(epic1); // 3
        manager.newEpic(epic2); // 4
        manager.newSubTasks(subTask1); // 5
        manager.newSubTasks(subTask2); // 6
        manager.newSubTasks(subTask3); // 7
        manager.addSubTask(5, 3);
        manager.addSubTask(6, 3);
        manager.addSubTask(7, 4);
        manager.checkStatus(3);
        manager.checkStatus(4);

        System.out.println(manager.tasks);
        System.out.println();
        System.out.println(manager.epics);
        System.out.println();
        System.out.println(manager.subTasks);
        System.out.println("--------------------------------------------------");

        task2.setStatus(Status.IN_PROGRESS);
        manager.updateTask(2, task2);
        subTask3.setStatus(Status.DONE);
        manager.updateSubTask(7, subTask3);
        manager.checkStatus(4);

        System.out.println(manager.tasks);
        System.out.println();
        System.out.println(manager.epics);
        System.out.println();
        System.out.println(manager.subTasks);
        System.out.println("--------------------------------------------------");

        manager.deleteTaskByID(1);
        manager.deleteEpicByID(3);
        manager.clearEpicList(5);
        manager.clearEpicList(6);

        System.out.println(manager.tasks);
        System.out.println();
        System.out.println(manager.epics);
        System.out.println();
        System.out.println(manager.subTasks);
        System.out.println("--------------------------------------------------");
    }
}

