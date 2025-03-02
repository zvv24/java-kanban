package Tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> SubTask = new ArrayList<>();
    public Epic(String name, String description, Status status, ArrayList<Integer> SubTask) {
        super(name, description, status);
        this.SubTask = SubTask;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subTask=" + SubTask +
                '}';
    }

    public ArrayList<Integer> getSubTask() {
        return SubTask;
    }

    public void setSubTask(int subTask) {
        SubTask.add(subTask);
    }
}