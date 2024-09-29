import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // Тестировка:
        // 1)
        System.out.println("1) Первый этап тестировки:");

        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Задача 1", "Описание первого таска.");
        Task task2 = new Task("Задача 2", "Описание второго таска.");

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание первого эпика.");
        Epic epic2 = new Epic("Эпик 2", "Описание второго эпика.");

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Сабтаск 1", "Описание первого сабтаска.");
        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание второго сабтаска.");
        Subtask subtask3 = new Subtask("Сабтаск 3", "Описание второго сабтаска.");

        taskManager.createAndAddSubtask(epic1, subtask1);
        taskManager.createAndAddSubtask(epic1, subtask2);
        taskManager.createAndAddSubtask(epic2, subtask3);

        ArrayList<Task> tasksList = taskManager.getTasks();

        for (Task task : tasksList) {
            System.out.println(task);
        }

        ArrayList<Epic> epicsList = taskManager.getEpics();

        for (Epic epic : epicsList) {
            System.out.println(epic);
        }

        ArrayList<Subtask> subtasksList = taskManager.getSubtasks();

        for (Subtask subtask : subtasksList) {
            System.out.println(subtask);
        }

        System.out.println("\n");

        // 2)
        System.out.println("2) Второй этап тестировки:");

        Task task3 = new Task("Задача 1", "Описание первого таска.", 0, Statuses.IN_PROGRESS);
        Task task4 = new Task("Задача 2", "Описание второго таска.", 1, Statuses.DONE);

        taskManager.updateTask(task3);
        taskManager.updateTask(task4);

        Subtask subtask4 = new Subtask("Сабтаск 1", "Описание первого сабтаска.", 4, Statuses.NEW);
        Subtask subtask5 = new Subtask("Сабтаск 2", "Описание второго сабтаска.", 5, Statuses.DONE);
        Subtask subtask6 = new Subtask("Сабтаск 3", "Описание второго сабтаска.", 6, Statuses.DONE);

        taskManager.updateSubtask(epic1, subtask4);
        taskManager.updateSubtask(epic1, subtask5);
        taskManager.updateSubtask(epic2, subtask6);

        ArrayList<Task> tasksList2 = taskManager.getTasks();

        for (Task task : tasksList2) {
            System.out.println(task);
        }

        ArrayList<Epic> epicsList2 = taskManager.getEpics();

        for (Epic epic : epicsList2) {
            System.out.println(epic);
        }

        ArrayList<Subtask> subtasksList2 = taskManager.getSubtasks();

        for (Subtask subtask : subtasksList2) {
            System.out.println(subtask);
        }

        System.out.println("\n");

        // 3)
        System.out.println("3) Третий этап тестировки:");

        taskManager.deleteTask(task3);
        taskManager.deleteEpic(epic2);

        taskManager.printAllTasks();
    }

}
