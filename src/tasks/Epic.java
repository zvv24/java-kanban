package tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtask = new ArrayList<>();

    public Epic(int id, String name, String description, Status status, ArrayList<Integer> subtask) {
        super(id, name, description, status);
        this.subtask = subtask;
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

    public ArrayList<Integer> getSubtask() {
        return subtask;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    public void setSubtask(int subtask) {
        this.subtask.add(subtask);
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