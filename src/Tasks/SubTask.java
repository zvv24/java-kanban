package Tasks;

import java.util.ArrayList;

public class SubTask extends Task {
    private ArrayList<Integer> Epic = new ArrayList<>();
    public SubTask(String name, String description, Status status, ArrayList<Integer> Epic) {
        super(name, description, status);
        this.Epic = Epic;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epic=" + Epic +
                '}';
    }

    public ArrayList<Integer> getEpic() {
        return Epic;
    }

    public void setEpic(int epic) {
        Epic.add(epic);
    }


}