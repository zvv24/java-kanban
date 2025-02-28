import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class SubTaskManager {
    Scanner scanner = new Scanner(System.in);
    static HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public void newSubTasks() {

        System.out.println("Введите имя, описание и статус подзадачи.");
        String name = scanner.nextLine();
        String description = scanner.nextLine();
        Status status = Status.valueOf(scanner.nextLine());
        ArrayList<Integer> EpicID = new ArrayList<>();
        SubTask subTask = new SubTask(name, description, status, EpicID);
        subTasks.put(Task.id++, subTask);
        subTasksToEpic();
    }

    public void subTasksToEpic(){
        System.out.println("К какому эпику принадлежит подзадача?");
        while (true) {
            System.out.print("Введите Id эпика ");
            int id = scanner.nextInt();
            if (EpicManager.epics.containsKey(id)) {
                ArrayList<Integer> EpicID = new ArrayList<>();
                EpicID.add(id);
                Epic list = EpicManager.epics.get(id);
                list.SubTask.add(Task.id - 1);
                SubTask list1 = subTasks.get(Task.id - 1);
                list1.Epic.add(id);
                EpicManager.checkStatus(id);
                scanner.nextLine(); //очистка буфера
                break;
            } else {
                System.out.println("Эпика с таким ID нет, попробуйте ещё раз.");
            }
        }
    }

    public void deleteAllSubTasks() {
        Set<Integer> list = subTasks.keySet();
        for (int id : list) {
            ArrayList<Integer> id2 = subTasks.get(id).Epic;
            Epic epic = EpicManager.epics.get(id2.get(0));
            epic.SubTask = new ArrayList<>();
            epic.status = Status.NEW;
        }
        subTasks.clear();
        System.out.println("Список подзадач очищен.");

    }

    public void searchSubTaskByID() {
        while (true) {
            System.out.print("Введите Id подзадачи ");
            int id = scanner.nextInt();
            if (subTasks.containsKey(id)) {
                System.out.println(subTasks.get(id));
                break;
            } else {
                System.out.println("Подзадачи с таким ID нет, попробуйте ещё раз.");
            }
        }
    }

    public void printAllSubTasks() {
        System.out.println(subTasks);
    }

    public void updateSubTask() {
        while (true) {
            System.out.print("Введите Id подзадачи ");
            int id = scanner.nextInt();
            if (subTasks.containsKey(id)) {
                scanner.nextLine(); //очистка буфера
                String name = scanner.nextLine();
                String description = scanner.nextLine();
                Status status = Status.valueOf(scanner.nextLine());
                ArrayList<Integer> EpicID = subTasks.get(id).Epic;
                SubTask subTask = new SubTask(name, description, status, EpicID);
                subTasks.put(id, subTask);
                System.out.println("Подзадача с Id " + id + " изменена.");
                if (EpicID.isEmpty()) {
                    id = EpicID.get(0);
                    EpicManager.checkStatus(id);
                }
                break;
            } else {
                System.out.println("Подзадачи с таким ID нет, попробуйте ещё раз.");
            }
        }
    }

    public void deleteSubTaskByID() {
        while (true) {
            int id = scanner.nextInt();
            if (subTasks.containsKey(id)) {
                SubTask list = subTasks.get(id);
                int id1 = list.Epic.get(0);
                subTasks.remove(id);
                System.out.println("Подзадача с Id " + id + " удалена.");
                EpicManager.checkStatus(id1);
                break;
            } else {
                System.out.println("Подзадачи с таким ID нет, попробуйте ещё раз.");
            }
        }
    }

}
