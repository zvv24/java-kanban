package managers;

import tasks.*;

import java.util.ArrayList;

public class ConversionMethods {
    public static String toString(Task task) {
        TaskType taskType = task.getType();
        switch (taskType) {
            case SUBTASK -> {
                return String.format("%d,%s,%s,%s,%s,%d",
                        task.getId(),
                        taskType,
                        task.getName(),
                        task.getStatus(),
                        task.getDescription(),
                        ((Subtask) task).getEpic());
            }
            case EPIC, TASK -> {
                return String.format("%d,%s,%s,%s,%s",
                        task.getId(),
                        taskType,
                        task.getName(),
                        task.getStatus(),
                        task.getDescription());
            }
            default -> throw new IllegalArgumentException("Неверный тип задачи");
        }
    }

    public static Task fromString(String value) {
        String[] buf = value.split(","); // id,type,name,status,description,epic
        int id = Integer.parseInt(buf[0]);
        TaskType taskType = TaskType.valueOf(buf[1]);
        String name = buf[2];
        Status status = Status.valueOf(buf[3]);
        String description = buf[4];

        switch (taskType) {
            case SUBTASK -> {
                return new Subtask(id, name, description, status, Integer.parseInt(buf[5]));
            }
            case EPIC -> {
                return new Epic(id, name, description, status, new ArrayList<>());
            }
            case TASK -> {
                return new Task(id, name, description, status);
            }
            default -> throw new IllegalArgumentException("Неверный тип задачи");
        }
    }
}
