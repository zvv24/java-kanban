import java.util.ArrayList;

public class Task {
    static int id = 1;
    String name;
    String description;
    Status status;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

}

class Epic extends Task {
    ArrayList<Integer> SubTask = new ArrayList<>();
    public Epic(String name, String description, Status status, ArrayList<Integer> SubTask) {
        super(name, description, status);
        this.SubTask = SubTask;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subTask=" + SubTask +
                '}';
    }
}

class SubTask extends Task {
    ArrayList<Integer> Epic = new ArrayList<>();
    public SubTask(String name, String description, Status status, ArrayList<Integer> Epic) {
        super(name, description, status);
        this.Epic = Epic;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", Epic=" + Epic +
                '}';
    }
}
