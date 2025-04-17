package managers;

import tasks.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static File file = new File("file.csv");

    @Override
    public Task newTask(Task task) {
        Task buf = super.newTask(task);
        save();
        return buf;
    }

    @Override
    public Epic newEpic(Epic epic) {
        Epic buf = super.newEpic(epic);
        save();
        return buf;
    }

    @Override
    public Subtask newSubtasks(Subtask subtask) {
        Subtask buf = super.newSubtasks(subtask);
        save();
        return buf;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task updateTask(Task task) {
        Task buf = super.updateTask(task);
        save();
        return buf;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic buf = super.updateEpic(epic);
        save();
        return buf;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Subtask buf = super.updateSubtask(subtask);
        save();
        return buf;
    }

    @Override
    public Task deleteTaskByID(int id) {
        Task buf = super.deleteTaskByID(id);
        save();
        return buf;
    }

    @Override
    public Epic deleteEpicByID(int id) {
        Epic buf = super.deleteEpicByID(id);
        save();
        return buf;
    }

    @Override
    public Subtask deleteSubtaskByID(int id) {
        Subtask buf = super.deleteSubtaskByID(id);
        save();
        return buf;
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write("id,type,name,status,description,epic");
            for (Task task : getAllTasks()) {
                writer.write(toString(task));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения", e);
        }
    }

    private List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(printAllTasks());
        allTasks.addAll(printAllEpics());
        allTasks.addAll(printAllSubtasks());
        return allTasks;
    }

    private static String toString(Task task) {
        if (task.getClass() == Subtask.class) {
            return String.format("%d, %s, %s, %s, %s, %d",
                    task.getId(),
                    TaskType.SUBTASK,
                    task.getName(),
                    task.getStatus(),
                    task.getDescription(),
                    ((Subtask) task).getEpic());
        } else if (task.getClass() == Epic.class) {
            return String.format("%d, %s, %s, %s, %s",
                    task.getId(),
                    TaskType.EPIC,
                    task.getName(),
                    task.getStatus(),
                    task.getDescription());
        } else {
            return String.format("%d, %s, %s, %s, %s",
                    task.getId(),
                    TaskType.TASK,
                    task.getName(),
                    task.getStatus(),
                    task.getDescription());
        }
    }

    private static Task fromString(String value) {
        String[] buf = value.split(","); // id,type,name,status,description,epic
        int id = Integer.parseInt(buf[0]);
        TaskType taskType = TaskType.valueOf(buf[1]);
        String name = buf[2];
        Status status = Status.valueOf(buf[3]);
        String description = buf[4];

        if (taskType.equals(TaskType.SUBTASK)) {
            return new Subtask(id, name, description, status, Integer.parseInt(buf[5]));
        } else if (taskType.equals(taskType.EPIC)) {
            return new Epic(id, name, description, status, new ArrayList<>());
        } else {
            return new Task(id, name, description, status);
        }
    }

    class ManagerSaveException extends RuntimeException {
        public ManagerSaveException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }

}
