import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class EpicManager {
    Scanner scanner = new Scanner(System.in);
    static HashMap<Integer, Epic> epics = new HashMap<>();

    public void newEpic() {
        System.out.println("Введите имя, описание и статус эпика.");
        String name = scanner.nextLine();
        String description = scanner.nextLine();
        Status status = Status.valueOf(scanner.nextLine());
        ArrayList<Integer> subTaskID = new ArrayList<>();
        Epic epic = new Epic(name, description, status, subTaskID);
        epics.put(Task.id++, epic);
        checkStatus(Task.id - 1);
    }

    public void deleteAllEpics() {
        Set<Integer> list = epics.keySet();
        for (int id : list) {
            ArrayList<Integer> id2 = epics.get(id).SubTask;
            for (int i : id2){
                SubTask subTask = SubTaskManager.subTasks.get(i);
                subTask.Epic = new ArrayList<>();
            }

        }
        epics.clear();
        System.out.println("Список эпиков очищен.");
    }

    public void searchEpicByID() {
        while (true) {
            System.out.print("Введите Id эпика ");
            int id = scanner.nextInt();
            if (epics.containsKey(id)) {
                System.out.println(epics.get(id));
                break;
            } else {
                System.out.println("Эпика с таким ID нет, попробуйте ещё раз.");
            }
        }
    }

    public void printAllEpics() {
        System.out.println(epics);
    }

    public void updateEpic() {
        while (true) {
            System.out.print("Введите Id эпика ");
            int id = scanner.nextInt();
            if (epics.containsKey(id)) {
                scanner.nextLine(); //очистка буфера
                System.out.println("Введите имя, описание и статус эпика.");
                String name = scanner.nextLine();
                String description = scanner.nextLine();
                Status status = Status.valueOf(scanner.nextLine());
                ArrayList<Integer> subTaskID = epics.get(id).SubTask;
                Epic epic = new Epic(name, description, status, subTaskID);
                epics.put(id, epic);
                System.out.println("Эпик с Id " + id + " изменен.");
                checkStatus(id);
                break;
            } else {
                System.out.println("Эпика с таким ID нет, попробуйте ещё раз.");
            }
        }
    }

    public void deleteEpicByID() {
        while (true) {
            int id = scanner.nextInt();
            if (epics.containsKey(id)) {
                epics.remove(id);
                System.out.println("Эпик с Id " + id + " удалена.");
                break;
            } else {
                System.out.println("Эпика с таким ID нет, попробуйте ещё раз.");
            }
        }
    }

    public void printListSubTasks() {
        while (true) {
            System.out.print("Введите Id эпика ");
            int id = scanner.nextInt();
            if (epics.containsKey(id)) {
                Epic list = epics.get(id);
                System.out.println(list.SubTask);
                break;
            } else {
                System.out.println("Эпика с таким ID нет, попробуйте ещё раз.");
            }
        }
    }

    public static void checkStatus(int id){
        Epic list = epics.get(id);
        if (list.SubTask.isEmpty()) {
            list.status = Status.NEW;
        } else {
            ArrayList<Integer> listSubTask = list.SubTask;
            int d = 0;
            int n = 0;
            for (int i : listSubTask) {
                SubTask list2 = SubTaskManager.subTasks.get(i);
                if (list2.status.equals(Status.DONE)) {
                    d++;
                } else if (list2.status.equals(Status.NEW)) {
                    n++;
                }
            }
            if (d == listSubTask.size()) {
                list.status = Status.DONE;
            } else if (n == listSubTask.size()){
                list.status = Status.NEW;
            } else {
                list.status = Status.IN_PROGRESS;
            }
        }
    }
}
