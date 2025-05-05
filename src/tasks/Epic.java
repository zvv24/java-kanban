package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtask = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(int id, String name, String description, Status status, ArrayList<Integer> subtask) {
        super(id, name, description, status, null, null);
        this.subtask = subtask;
    }

    public ArrayList<Integer> getSubtask() {
        return subtask;
    }

    public void setSubtask(int subtask) {
        this.subtask.add(subtask);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subTask=" + subtask +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtask, epic.subtask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtask);
    }
}