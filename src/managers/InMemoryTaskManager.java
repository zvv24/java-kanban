package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();

    private int newId() {
        return id++;
    }

    //Создание нового объекта
    @Override
    public Task newTask(Task task) {
        return tasks.put(newId(), task);
    }

    @Override
    public Epic newEpic(Epic epic) {
        epics.put(newId(), epic);
        checkStatus(id - 1);
        return epic;
    }

    @Override
    public Subtask newSubtasks(Subtask subtask) {
        int idSubtask = subtask.getId();
        int idEpic = subtask.getEpic();
        epics.get(idEpic).setSubtask(idSubtask);
        subtasks.put(newId(), subtask);
        checkStatus(idEpic);
        return subtask;
    }

    //Получение списка.
    @Override
    public List<Task> printAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> printAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> printAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    //удаление всех объектов
    @Override
    public void deleteAllTasks() {
        for (Task task : printAllTasks()) {
            historyManager.removeHistory(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : printAllEpics()) {
            for (int subtask : epic.getSubtask()) {
                historyManager.removeHistory(subtask);
            }
            historyManager.removeHistory(epic.getId());
        }
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Subtask subtask : printAllSubtasks()) {
            historyManager.removeHistory(subtask.getId());
        }
        subtasks.clear();
        for (int idEpic : epics.keySet()) {
            epics.get(idEpic).getSubtask().clear();
            checkStatus(idEpic);
        }
    }

    //Поиск по ID.
    @Override
    public Task searchTaskByID(int id) {
        Task task = tasks.get(id);
        historyManager.addHistory(task);
        return task;
    }

    @Override
    public Epic searchEpicByID(int id) {
        Epic epic = epics.get(id);
        historyManager.addHistory(epic);
        return epic;
    }

    @Override
    public Subtask searchSubtaskByID(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.addHistory(subtask);
        return subtask;
    }

    //Обновление.
    @Override
    public Task updateTask(Task task) {
        return tasks.replace(task.getId(), task);
    }

    @Override
    public Epic updateEpic(Epic epic) {
        epics.replace(epic.getId(), epic);
        checkStatus(epic.getId());
        return epic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        subtasks.replace(subtask.getId(), subtask);
        checkStatus(subtask.getEpic());
        return subtask;
    }

    //Удаление по ID.
    @Override
    public Task deleteTaskByID(int id) {
        historyManager.removeHistory(id);
        return tasks.remove(id);
    }

    @Override
    public Epic deleteEpicByID(int id) {
        ArrayList<Integer> idSubtask = epics.get(id).getSubtask();
        for (int ids : idSubtask) {
            historyManager.removeHistory(ids);
            subtasks.remove(ids);
        }
        historyManager.removeHistory(id);
        return epics.remove(id);
    }

    @Override
    public Subtask deleteSubtaskByID(int id) {
        int idEpic = subtasks.get(id).getEpic();
        epics.get(idEpic).getSubtask().remove(Integer.valueOf(id));
        checkStatus(idEpic);
        historyManager.removeHistory(id);
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
    @Override
    public ArrayList<Subtask> printSubtasksСertainEpic(int id) {
        ArrayList<Integer> listSubtaks = epics.get(id).getSubtask();
        ArrayList<Subtask> subtasks1 = new ArrayList<>();
        for (int idSubtask : listSubtaks) {
            subtasks1.add(subtasks.get(idSubtask));
        }
        return subtasks1;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}