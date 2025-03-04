package tasks;

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

    public void setEpic(int epic) {
        this.epic = epic;
    }
}