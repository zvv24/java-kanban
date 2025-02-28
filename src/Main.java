import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();
        EpicManager epicManager = new EpicManager();
        SubTaskManager subTaskManager = new SubTaskManager();
        System.out.println("Поехали!");

        while (true) {
            printMenu();
            int command = scanner.nextInt();

            switch (command) {
                case 1 -> {
                    System.out.println("Какой список вы хотите получить? \n 1 - Задач \n 2 - Эпиков \n 3 - Подзадач");
                    command = scanner.nextInt();
                    if (command == 1) {
                        taskManager.printAllTasks();
                    } else if (command == 2) {
                        epicManager.printAllEpics();
                    } else if (command == 3) {
                        subTaskManager.printAllSubTasks();
                    } else {
                        System.out.println("Такой команды нет");
                    }
                }
                case 2 -> {
                    System.out.println("Какой список вы хотите удалить? \n 1 - Задач \n 2 - Эпиков \n 3 - Подзадач");
                    command = scanner.nextInt();
                    if (command == 1) {
                        taskManager.deleteAllTasks();
                    } else if (command == 2) {
                        epicManager.deleteAllEpics();
                    } else if (command == 3) {
                        subTaskManager.deleteAllSubTasks();
                    } else {
                        System.out.println("Такой команды нет");
                    }
                }
                case 3 -> {
                    System.out.println("Что вы хотите получить? \n 1 - Задачу \n 2 - Эпик \n 3 - Подзадачу");
                    command = scanner.nextInt();
                    if (command == 1) {
                        taskManager.searchTaskByID();
                    } else if (command == 2) {
                        epicManager.searchEpicByID();
                    } else if (command == 3) {
                        subTaskManager.searchSubTaskByID();
                    } else {
                        System.out.println("Такой команды нет");
                    }
                }
                case 4 -> {
                    System.out.println("Что вы хотите создать? \n 1 - Задачу \n 2 - Эпик \n 3 - Подзадачу");
                    command = scanner.nextInt();
                    if (command == 1) {
                        taskManager.newTask();
                    } else if (command == 2) {
                        epicManager.newEpic();
                    } else if (command == 3) {
                        subTaskManager.newSubTasks();
                    } else {
                        System.out.println("Такой команды нет");
                    }
                }
                case 5 -> {
                    System.out.println("Что вы хотите обновить? \n 1 - Задачу \n 2 - Эпик \n 3 - Подзадачу");
                    command = scanner.nextInt();
                    if (command == 1) {
                        taskManager.updateTask();
                    } else if (command == 2) {
                        epicManager.updateEpic();
                    } else if (command == 3) {
                        subTaskManager.updateSubTask();
                    } else {
                        System.out.println("Такой команды нет");
                    }
                }
                case 6 -> {
                    System.out.println("Что вы хотите удалить? \n 1 - Задачу \n 2 - Эпик \n 3 - Подзадачу");
                    command = scanner.nextInt();
                    if (command == 1) {
                        taskManager.deleteTaskByID();
                    } else if (command == 2) {
                        epicManager.deleteEpicByID();
                    } else if (command == 3) {
                        subTaskManager.deleteSubTaskByID();
                    } else {
                        System.out.println("Такой команды нет");
                    }
                }
                case 7 -> epicManager.printListSubTasks();
                case 0 -> {
                    System.out.println(0);
                    return;
                }
            }

        }
    }
    public static void printMenu(){
        System.out.println("1 - Получить список");
        System.out.println("2 - Удаление");
        System.out.println("3 - Получение по идентификатору");
        System.out.println("4 - Создание");
        System.out.println("5 - Обновление");
        System.out.println("6 - Удаление по идентификатору");
        System.out.println("7 - Получение списка всех подзадач определённого эпика.");
        System.out.println("0 - Выход");
    }
}

