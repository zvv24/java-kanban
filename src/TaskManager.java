import java.util.HashMap;
import java.util.Scanner;

public class TaskManager {
    Scanner scanner = new Scanner(System.in);
    HashMap<Integer, Task> tasks = new HashMap<>();

    public void newTask() {
        System.out.println("Введите имя, описание и статус задачи.");
        String name = scanner.nextLine();
        String description = scanner.nextLine();
        Status status = Status.valueOf(scanner.nextLine());
        Task task = new Task(name, description, status);
        tasks.put(Task.id++, task);
    }

    public void deleteAllTasks() {
        tasks.clear();
        System.out.println("Список задач очищен.");
    }

    public void searchTaskByID() {
        while (true) {
            System.out.print("Введите Id задачи ");
            int id = scanner.nextInt();
            if (tasks.containsKey(id)) {
                System.out.println(tasks.get(id));
                break;
            } else {
                System.out.println("Задачи с таким ID нет, попробуйте ещё раз.");
            }
        }
    }

    public void printAllTasks() {
        System.out.println(tasks);
    }

    public void updateTask() {
        while (true) {
            System.out.print("Введите Id задачи ");
            int id = scanner.nextInt();
            if (tasks.containsKey(id)) {
                scanner.nextLine(); //очистка буфера
                System.out.println("Введите имя, описание и статус задачи.");
                String name = scanner.nextLine();
                String description = scanner.nextLine();
                Status status = Status.valueOf(scanner.nextLine());
                Task task = new Task(name, description, status);
                tasks.put(id, task);
                System.out.println("Задача с Id " + id + " изменена.");
                break;
            } else {
                System.out.println("Задачи с таким ID нет, попробуйте ещё раз.");
            }
        }
    }

    public void deleteTaskByID() {
        while (true) {
            int id = scanner.nextInt();
            if (tasks.containsKey(id)) {
                tasks.remove(id);
                System.out.println("Задача с Id " + id + " удалена.");
                break;
            } else {
                System.out.println("Задачи с таким ID нет, попробуйте ещё раз.");
            }
        }
    }
}
