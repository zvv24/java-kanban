package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private int id = 1;
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int newId() {
        return id++;
    }

    //Создание нового объекта
    public Task newTask(Task task) {
        return tasks.put(newId(), task);
    }

    public Epic newEpic(Epic epic) {
        epics.put(newId(), epic);
        checkStatus(id - 1);
        return epic;
    }

    public Subtask newSubtasks(Subtask subtask) {
        int idSubtask = subtask.getId();
        int idEpic = subtask.getEpic();
        epics.get(idEpic).setSubtask(idSubtask);
        subtasks.put(newId(), subtask);
        checkStatus(idEpic);
        return subtask;
    }

    //Получение списка.
    public List<Task> printAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> printAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> printAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    //удаление всех объектов
    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        deleteAllSubtasks();
        epics.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (int idEpic : epics.keySet()) {
            epics.get(idEpic).getSubtask().clear();
            checkStatus(idEpic);
        }
    }

    //Поиск по ID.
    public Task searchTaskByID(int id) {
        return tasks.get(id);
    }

    public Epic searchEpicByID(int id) {
        return epics.get(id);
    }

    public Subtask searchSubtaskByID(int id) {
        return subtasks.get(id);
    }

    //Обновление.
    public Task updateTask(Task task) {
        return tasks.replace(task.getId(), task);
    }

    public Epic updateEpic(Epic epic) {
        epics.replace(epic.getId(), epic);
        checkStatus(epic.getId());
        return epic;
    }

    public Subtask updateSubtask(Subtask subtask) {
        subtasks.replace(subtask.getId(), subtask);
        checkStatus(subtask.getEpic());
        return subtask;
    }

    //Удаление по ID.
    public Task deleteTaskByID(int id) {
        return tasks.remove(id);
    }

    public Epic deleteEpicByID(int id) {
        ArrayList<Integer> idSubtask = epics.get(id).getSubtask();
        for (int ids : idSubtask) {
            subtasks.remove(ids);
        }
        return epics.remove(id);
    }

    public Subtask deleteSubtaskByID(int id) {
        int idEpic = subtasks.get(id).getEpic();
        epics.get(idEpic).getSubtask().remove(Integer.valueOf(id));
        checkStatus(idEpic);
        return subtasks.remove(id);
    }

    //Проверка статуса для эпиков
    private void checkStatus(int id) {
        Epic epic = epics.get(id);
        if (epic.getSubtask().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            ArrayList<Integer> listSubtask = epic.getSubtask();
            int done = 0;
            int nnew = 0;
            for (int i : listSubtask) {
                Subtask subtask = subtasks.get(i);
                if (subtask.getStatus().equals(Status.DONE)) {
                    done++;
                } else if (subtask.getStatus().equals(Status.NEW)) {
                    nnew++;
                }
            }
            if (done == listSubtask.size()) {
                epic.setStatus(Status.DONE);
            } else if (nnew == listSubtask.size()) {
                epic.setStatus(Status.NEW);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    //Получение списка всех подзадач определённого эпика.
    public ArrayList<Integer> printListSubtasks(int id) {
        Epic listSubtaks = epics.get(id);
        return listSubtaks.getSubtask();
    }

    //Получение списка всех подзадач определённого эпика.
    public ArrayList<Subtask> printSubtasksСertainEpic(int id) {
        ArrayList<Integer> listSubtaks = epics.get(id).getSubtask();
        ArrayList<Subtask> subtasks1 = new ArrayList<>();
        for (int idSubtask : listSubtaks) {
            subtasks1.add(subtasks.get(idSubtask));
        }
        return subtasks1;
    }
}