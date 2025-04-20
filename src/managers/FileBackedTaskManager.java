package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

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
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTasks()) {
                writer.write(ConversionMethods.toString(task));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения", e);
        }
    }

    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(printAllTasks());
        allTasks.addAll(printAllEpics());
        allTasks.addAll(printAllSubtasks());
        return allTasks;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        int id = 0;

        try {
            List<String> list = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for (int i = 1; i < list.size(); i++) {
                String buf = list.get(i);
                Task task = ConversionMethods.fromString(buf);

                if (task.getId() > id) {
                    id = task.getId();
                }

                switch (task.getType()) {
                    case SUBTASK -> fileBackedTaskManager.subtasks.put(task.getId(), (Subtask) task);
                    case EPIC -> fileBackedTaskManager.epics.put(task.getId(), (Epic) task);
                    case TASK -> fileBackedTaskManager.tasks.put(task.getId(), task);
                }
            }

            for (Subtask subtask : fileBackedTaskManager.subtasks.values()) {
                Epic epic = fileBackedTaskManager.epics.get(subtask.getEpic());
                epic.getSubtask().add(subtask.getId());
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки", e);
        }
        return fileBackedTaskManager;
    }

    static class ManagerSaveException extends RuntimeException {
        public ManagerSaveException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}