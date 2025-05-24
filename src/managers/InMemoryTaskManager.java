package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();

    private int newId() {
        return id++;
    }

    //Создание нового объекта
    @Override
    public Task newTask(Task task) {
        if (taskOverlap(task)) {
            throw new IllegalArgumentException("Задачи пересекаются");
        }
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
        if (taskOverlap(subtask)) {
            throw new IllegalArgumentException("Задачи пересекаются");
        }
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
        printAllTasks().stream()
                .map(Task::getId)
                .forEach(historyManager::removeHistory);
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        printAllEpics().forEach(epic -> {
            epic.getSubtask().forEach(historyManager::removeHistory);
            historyManager.removeHistory(epic.getId());
        });
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        printAllSubtasks().stream()
                .map(Subtask::getId)
                .forEach(historyManager::removeHistory);
        subtasks.clear();
        epics.keySet().forEach(idEpic -> {
            epics.get(idEpic).getSubtask().clear();
            checkStatus(idEpic);
        });
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
        if (taskOverlap(task)) {
            throw new IllegalArgumentException("Задачи пересекаются");
        }
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
        if (taskOverlap(subtask)) {
            throw new IllegalArgumentException("Задачи пересекаются");
        }
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
        calculateTimeEpic(epic);
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

    private void calculateTimeEpic(Epic epic) {
        if (epic.getSubtask().isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
            return;
        }

        LocalDateTime start = LocalDateTime.MAX;
        LocalDateTime end = LocalDateTime.MIN;
        Duration duration = Duration.ZERO;

        for (int subtaskId : epic.getSubtask()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask == null || subtask.getStartTime() == null) continue;

            LocalDateTime startSubtask = subtask.getStartTime();
            LocalDateTime endSubtask = subtask.getEndTime();
            Duration durationSubtask = subtask.getDuration();

            if (start == null || startSubtask.isBefore(start)) {
                start = startSubtask;
            }
            if (end == null || endSubtask.isAfter(end)) {
                end = endSubtask;
            }
            duration = duration.plus(durationSubtask);
        }

        epic.setStartTime(start);
        epic.setDuration(duration);
        epic.setEndTime(end);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    //Получение списка всех подзадач определённого эпика.
    @Override
    public ArrayList<Subtask> printSubtasksCertainEpic(int id) {
        return epics.get(id).getSubtask().stream()
                .map(subtasks::get)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
                Comparator.nullsLast(Comparator.naturalOrder())));
        prioritizedTasks.addAll(tasks.values());
        prioritizedTasks.addAll(subtasks.values());
        return prioritizedTasks;
    }

    private boolean taskOverlap(Task task) {
        return getPrioritizedTasks().stream()
                .filter(t -> t.getStartTime() != null)
                .anyMatch(t -> !t.equals(task) && t.isOverlap(task));
    }
}