package manager;

import task.*;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTaskManager<T extends Task> extends InMemoryTaskManager<T> {
    private Path filePath;

    public static void main(String[] args) {
//        FileBackedTaskManager<Task> taskManager = new FileBackedTaskManager<>("C:/Users/User/Desktop/Java Курс/Sprint-4/java-kanban/src/file.txt");
//
//        Task task1 = new Task("Задача 1", "Описание первого таска.", Statuses.NEW);
//        Task task2 = new Task("Задача 2", "Описание второго таска.", Statuses.DONE);
//
//        taskManager.createTask(task1);
//        taskManager.createTask(task2);
//
//        Epic epic1 = new Epic("Эпик 1", "Описание первого эпика.");
//        Epic epic2 = new Epic("Эпик 2", "Описание второго эпика.");
//
//        taskManager.createEpic(epic1);
//        taskManager.createEpic(epic2);
//
//        Subtask subtask1 = new Subtask("Сабтаск 1", "Описание первого сабтаска.", 3, Statuses.NEW);
//        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание второго сабтаска.", 3, Statuses.IN_PROGRESS);
//        Subtask subtask3 = new Subtask("Сабтаск 3", "Описание третьего сабтаска.", 4, Statuses.NEW);
//
//        taskManager.createAndAddSubtask(subtask1);
//        taskManager.createAndAddSubtask(subtask2);
//        taskManager.createAndAddSubtask(subtask3);

        FileBackedTaskManager<Task> taskManager = loadFromFile(new File("src/file.txt"));

        Task task3 = new Task("Задача 3", "Описание первого таска.", Statuses.NEW);

        taskManager.createTask(task3);

        Subtask subtask1 = new Subtask("Сабтаск 4", "Описание четвёртого сабтаска.", 4, Statuses.DONE);

        taskManager.createAndAddSubtask(subtask1);

        List<Task> tasksList = taskManager.getTasks();

        for (Task task : tasksList) {
            System.out.println(task);
        }

        List<Epic> epicsList = taskManager.getEpics();

        for (Epic epic : epicsList) {
            System.out.println(epic);
        }

        List<Subtask> subtasksList = taskManager.getSubtasks();

        for (Subtask subtask : subtasksList) {
            System.out.println(subtask);
        }
    }

    public FileBackedTaskManager(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    private void save() {
        try (Writer fileWriter = new FileWriter(filePath.toFile())) {
            Files.createFile(filePath);
            writeInFile(fileWriter);
        } catch (FileAlreadyExistsException e) {
            try (Writer fileWriter = new FileWriter(filePath.toFile())) {
                writeInFile(fileWriter);
            } catch (IOException ex) {
                try {
                    throw new ManagerSaveException("Ошибка записи в файл");
                } catch (ManagerSaveException exc) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException ex) {
            try {
                throw new ManagerSaveException("Ошибка работы с файлом");
            } catch (ManagerSaveException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void writeInFile(Writer fileWriter) throws IOException {
        fileWriter.write("id,type,name,status,description,epic\n");

        for (Task task : super.getTasks()) {
            fileWriter.write(task.toString() + "\n");
        }

        for (Epic epic : super.getEpics()) {
            fileWriter.write(epic.toString() + "\n");
        }

        for (Subtask subtask : super.getSubtasks()) {
            fileWriter.write(subtask.toString() + "\n");
        }
    }

    public static FileBackedTaskManager<Task> loadFromFile(File file) {
        FileBackedTaskManager<Task> taskManager = new FileBackedTaskManager<>(file.getAbsolutePath());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();

            String line;

            int biggestId = 0;

            while ((line = reader.readLine()) != null) {
                Task task = taskManager.fromString(line);

                if (task.getIdentificationNumber() > biggestId) {
                    biggestId = task.getIdentificationNumber();
                }

                if (task instanceof Subtask) {
                    taskManager.restoreSubtask((Subtask) task, biggestId);
                } else if (task instanceof Epic) {
                    taskManager.restoreEpic((Epic) task, biggestId);
                } else if (task instanceof Task) {
                    taskManager.restoreTask(task, biggestId);
                }
            }
        } catch (IOException ex) {
            try {
                throw new ManagerSaveException("Ошибка работы с файлом");
            } catch (ManagerSaveException e) {
                throw new RuntimeException(e);
            }
        }

        return taskManager;
    }

    private Task fromString(String value) {
        String[] split = value.split(",");

        // Получаем тип задачи
        String taskType = split[1];

        // Сравниваем с типами задач
        if (taskType.equals("class task.Task")) {
            return new Task(split[2], split[4], Integer.parseInt(split[0]), Statuses.valueOf(split[3]));
        } else if (taskType.equals("class task.Subtask")) {
            return new Subtask(split[2], split[4], Integer.parseInt(split[0]), Integer.parseInt(split[5]), Statuses.valueOf(split[3]));
        } else if (taskType.equals("class task.Epic")) {
            return new Epic(split[2], split[4], Integer.parseInt(split[0]), Statuses.valueOf(split[3]));
        } else {
            throw new RuntimeException("Ошибка восстановления задачи.");
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createAndAddSubtask(Subtask subtask) {
        super.createAndAddSubtask(subtask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public void deleteSubtask(Integer subtaskId) {
        super.deleteSubtask(subtaskId);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }
}
