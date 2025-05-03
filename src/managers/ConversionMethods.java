package managers;

import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ConversionMethods {
    public static String toString(Task task) {
        TaskType taskType = task.getType();
        switch (taskType) {
            case SUBTASK -> {
                return String.format("%d,%s,%s,%s,%s,%d,%s,%s",
                        task.getId(),
                        taskType,
                        task.getName(),
                        task.getStatus(),
                        task.getDescription(),
                        ((Subtask) task).getEpic(),
                        task.getStartTime().toString(),
                        task.getDuration().toMinutes());
            }
            case EPIC, TASK -> {
                return String.format("%d,%s,%s,%s,%s,%s,%s",
                        task.getId(),
                        taskType,
                        task.getName(),
                        task.getStatus(),
                        task.getDescription(),
                        task.getStartTime().toString(),
                        task.getDuration().toMinutes());


            }
            default -> throw new IllegalArgumentException("Неверный тип задачи");
        }
    }

    public static Task fromString(String value) {
        String[] buf = value.split(","); // id,type,name,status,description,epic,startTime,duration
        int id = Integer.parseInt(buf[0]);
        TaskType taskType = TaskType.valueOf(buf[1]);
        String name = buf[2];
        Status status = Status.valueOf(buf[3]);
        String description = buf[4];
        LocalDateTime startTime = LocalDateTime.parse(buf[buf.length - 2]);
        Duration duration = Duration.ofMinutes(Integer.parseInt(buf[buf.length - 1]));


        switch (taskType) {
            case SUBTASK -> {
                return new Subtask(id, name, description, status, Integer.parseInt(buf[5]), startTime, duration);
            }
            case EPIC -> {
                return new Epic(id, name, description, status, new ArrayList<>());
            }
            case TASK -> {
                return new Task(id, name, description, status, startTime, duration);
            }
            default -> throw new IllegalArgumentException("Неверный тип задачи");
        }
    }
}
