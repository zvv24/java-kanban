package Managers;

import Tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TaskManager {
    int id = 1;
    Scanner scanner = new Scanner(System.in);
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public int newId() {
        return id++;
    }

    //Создание нового объекта
    public Task newTask(Task task) {
        return tasks.put(newId(), task);
    }

    public Epic newEpic(Epic epic) {
        return epics.put(newId(), epic);
    } // после нужен checkStatus

    public SubTask newSubTasks(SubTask subTask) {
        return subTasks.put(newId(), subTask);
    } // после нужен checkStatus

    //Получение списка.
    public HashMap<Integer, Task> printAllTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> printAllEpics() {
        return epics;
    }

    public HashMap<Integer, SubTask> printAllSubTasks() {
        return subTasks;
    }

    //удаление всех объектов
    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
    } // после нужен checkStatus

    //Поиск по ID.
    public Task searchTaskByID(int id) {
        return tasks.get(id);
    }

    public Epic searchEpicByID(int id) {
        return epics.get(id);
    }

    public SubTask searchSubTaskByID(int id) {
        return subTasks.get(id);
    }

    //Обновление.
    public Task updateTask(int id, Task task) {
        return tasks.replace(id, task);
    }

    public Epic updateEpic(int id, Epic epic) {
        return epics.replace(id, epic);
    } // после нужен checkStatus

    public SubTask updateSubTask(int id, SubTask subTask) {
        return subTasks.replace(id, subTask);
    } // после нужен checkStatus

    //Удаление по ID.
    public Task deleteTaskByID(int id) {
        return tasks.remove(id);
    }

    public Epic deleteEpicByID(int id) {
        return epics.remove(id);
    }

    public SubTask deleteSubTaskByID(int id) {
        return subTasks.remove(id);
    } // после нужен checkStatus

    //Получение списка всех подзадач определённого эпика.
    public ArrayList<Integer> printListSubTasks(int id) {
        Epic list = epics.get(id);
        return list.getSubTask();
    }

    //Присвоение подзадачи эпику
    public void addSubTask(int idSubTask, int idEpic) {
        epics.get(idEpic).setSubTask(idSubTask);
        subTasks.get(idSubTask).setEpic(idEpic);
    } // после нужен checkStatus

    //Проверка статуса для эпиков
    public void checkStatus(int id) {
        Epic list = epics.get(id);
        if (list.getSubTask().isEmpty()) {
            list.setStatus(Status.NEW);
        } else {
            ArrayList<Integer> listSubTask = list.getSubTask();
            int d = 0;
            int n = 0;
            for (int i : listSubTask) {
                SubTask list2 = subTasks.get(i);
                if (list2.getStatus().equals(Status.DONE)) {
                    d++;
                } else if (list2.getStatus().equals(Status.NEW)) {
                    n++;
                }
            }
            if (d == listSubTask.size()) {
                list.setStatus(Status.DONE);
            } else if (n == listSubTask.size()) {
                list.setStatus(Status.NEW);
            } else {
                list.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    //Удаление
    public void clearEpicList(int id) {
        subTasks.get(id).getEpic().clear();
    }

    public void clearSubTaskList(int id) {
        epics.get(id).getSubTask().clear();
    }
}