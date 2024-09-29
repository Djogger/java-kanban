import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, ArrayList<Subtask>> subtasks = new HashMap<>();
    private int id = 0;


    public TaskManager() {

    }

    public void createTask(Task task) {
        task.setIdentificationNumber(id);
        task.setStatus(Statuses.NEW);
        tasks.put(id, task);
        id += 1;
    }

    public void createEpic(Epic epic) {
        epic.setIdentificationNumber(id);
        epic.setStatus(Statuses.NEW);
        epics.put(id, epic);
        id += 1;
    }

    public void createAndAddSubtask(Epic epic, Subtask subtask) {
        ArrayList<Subtask> subtasksArray;

        if (subtasks.get(epic.getIdentificationNumber()) == null) {
            subtasksArray = new ArrayList<>();
            subtasksArray.add(subtask);
        } else {
            subtasksArray = subtasks.get(epic.getIdentificationNumber());
            subtasksArray.add(subtask);
        }

        epic.addToSubtaskId(id);

        subtask.setIdentificationNumber(id);

        subtask.setStatus(Statuses.NEW);

        subtask.setEpicId(epic.getIdentificationNumber());

        subtasks.put(epic.getIdentificationNumber(), subtasksArray);

        id += 1;

        checkEpicStatus(epic.getIdentificationNumber());
    }

    public void updateTask(Task task) {
        tasks.put(task.getIdentificationNumber(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getIdentificationNumber(), epic);
    }

    public void updateSubtask(Epic epic, Subtask subtask) {
        ArrayList<Subtask> subtasksArray;

        if (subtasks.get(epic.getIdentificationNumber()) == null) {
            return;
        } else {
            subtasksArray = subtasks.get(epic.getIdentificationNumber());
        }

        int numOfSubtask = 0;

        for (Subtask subtaskFromArray : subtasksArray) {

            if (subtaskFromArray.getIdentificationNumber() == subtask.getIdentificationNumber()) {
                subtask.setIdentificationNumber(subtaskFromArray.getIdentificationNumber());

                subtask.setEpicId(epic.getIdentificationNumber());

                subtasksArray.set(numOfSubtask, subtask);

                subtasks.put(epic.getIdentificationNumber(), subtasksArray);

                checkEpicStatus(epic.getIdentificationNumber());
            }

            numOfSubtask += 1;
        }
    }

    public void checkEpicStatus(int epicId) {
        int newStatuses = 0;
        int inProgressStatuses = 0;
        int doneStatuses = 0;

        Epic epic = epics.get(epicId);

        ArrayList<Subtask> subtaskArrayList = subtasks.get(epicId);

        for (Subtask subtask : subtaskArrayList) {
            if (subtask.getStatus() == Statuses.NEW) {
                newStatuses += 1;
            } else if (subtask.getStatus() == Statuses.IN_PROGRESS) {
                inProgressStatuses += 1;
            } else if (subtask.getStatus() == Statuses.DONE) {
                doneStatuses += 1;
            }
        }

        if (newStatuses == subtaskArrayList.size() || epic.getSubtasksId().isEmpty()) {
            epic.setStatus(Statuses.NEW);
        } else if (doneStatuses == subtaskArrayList.size()) {
            epic.setStatus(Statuses.DONE);
        } else {
            epic.setStatus(Statuses.IN_PROGRESS);
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();

        tasksList.addAll(tasks.values());

        return tasksList;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicsList = new ArrayList<>();

        epicsList.addAll(epics.values());

        return epicsList;
    }

    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        ArrayList<Subtask> subtasksList = subtasks.get(epic.getIdentificationNumber());

        return subtasksList;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtasksList = new ArrayList<>();

        for (ArrayList<Subtask> subtasksArray : subtasks.values()) {
            subtasksList.addAll(subtasksArray);
        }

        return subtasksList;
    }

    public Task getTask(Task task) {
        return tasks.get(task.getIdentificationNumber());
    }

    public Epic getEpic(Epic epic) {
        return epics.get(epic.getIdentificationNumber());
    }

    public Subtask getSubtask(Epic epic, Subtask subtask) {
        ArrayList<Subtask> subtaskList = subtasks.get(epic.getIdentificationNumber());

        if (subtaskList.contains(subtask)) {
            int index = subtaskList.indexOf(subtask);
            return subtaskList.get(index);
        }

        return null;
    }

    public void printAllTasks() {
        ArrayList<Task> tasksList = getTasks();

        for (Task task : tasksList) {
            System.out.println(task);
        }

        ArrayList<Epic> epicsList = getEpics();

        for (Epic epic : epicsList) {
            System.out.println(epic);
        }

        ArrayList<Subtask> subtasksList = getSubtasks();

        for (Subtask subtask : subtasksList) {
            System.out.println(subtask);
        }
    }

    public void deleteTask(Task task) {
        tasks.remove(task.getIdentificationNumber());
    }

    public void deleteEpic(Epic epic) {
        epics.remove(epic.getIdentificationNumber());
        subtasks.remove(epic.getIdentificationNumber());
    }

    public void deleteSubtask(Epic epic, Subtask subtask) {
        int key = epic.getIdentificationNumber();

        ArrayList<Subtask> subtaskArray = subtasks.get(key);
        subtaskArray.remove(subtask);
        subtasks.put(key, subtaskArray);

        checkEpicStatus(epic.getIdentificationNumber());
    }

    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public ArrayList<Integer> getSubtasksId(int epicId) {
        return new ArrayList<>(); // Реализовать возвращение списка id.
    }
}
