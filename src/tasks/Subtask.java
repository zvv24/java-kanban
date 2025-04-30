package tasks;

import java.util.Objects;

public class Subtask extends Task {
    private int epic;

    public Subtask(int id, String name, String description, Status status, int epic) {
        super(id, name, description, status);
        this.epic = epic;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epic=" + epic +
                '}';
    }

    public int getEpic() {
        return epic;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    public void setEpic(int epic) {
        this.epic = epic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epic == subtask.epic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epic);
    }
}