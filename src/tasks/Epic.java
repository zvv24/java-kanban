package tasks;

import java.util.ArrayList;

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

    public void setSubtask(int subtask) {
        this.subtask.add(subtask);
    }
}