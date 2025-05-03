package tasks;

import managers.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtask = new ArrayList<>();

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
    public LocalDateTime getStartTime() {
        return subtask.stream()
                .map(InMemoryTaskManager::getSubtask)
                .map(Subtask::getStartTime)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.MAX);
    }

    @Override
    public Duration getDuration() {
        return subtask.stream()
                .map(InMemoryTaskManager::getSubtask)
                .map(Subtask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public LocalDateTime getEndTime() {
        return subtask.stream()
                .map(InMemoryTaskManager::getSubtask)
                .map(Subtask::getStartTime)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.MIN);
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