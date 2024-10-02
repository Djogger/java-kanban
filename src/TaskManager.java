import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int id = 1;


    public TaskManager() {

    }

    public void createTask(Task task) {
        task.setIdentificationNumber(id);
        tasks.put(id, task);
        id += 1;
    }

    public void createEpic(Epic epic) {
        epic.setIdentificationNumber(id);
        epic.setStatus(Statuses.NEW);
        epics.put(id, epic);
        id += 1;
    }

    public void createAndAddSubtask(Subtask subtask) {
        if (epics.get(subtask.getEpicId()) == null) {
            return;
        }

        Epic epic = epics.get(subtask.getEpicId());

        epic.addToSubtaskId(id);

        subtask.setIdentificationNumber(id);

        subtasks.put(subtask.getIdentificationNumber(), subtask);

        id += 1;

        checkEpicStatus(epic.getIdentificationNumber());
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getIdentificationNumber())) {
            tasks.put(task.getIdentificationNumber(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getIdentificationNumber())) {
            Epic epicFromList = epics.get(epic.getIdentificationNumber());
            epicFromList.setTaskName(epic.getTaskName());
            epicFromList.setDescription(epic.getDescription());
        }
    }

    public void updateSubtask(Subtask subtask) {
        int key = subtask.getIdentificationNumber();

        if (subtasks.containsKey(key)) {
            Subtask subtaskFromList = subtasks.get(key);

            if (subtaskFromList.getEpicId() == subtask.getEpicId()) {
                subtasks.put(key, subtask);
                checkEpicStatus(subtask.getEpicId());
            }
        }
    }

    private void checkEpicStatus(int epicId) {
        int newStatuses = 0;
        int doneStatuses = 0;

        Epic epic = epics.get(epicId);

        ArrayList<Integer> subtasksIdList = epic.getSubtasksId();

        for (Integer subtaskId : subtasksIdList) {
            Subtask subtask = subtasks.get(subtaskId);

            if (subtask.getStatus() == Statuses.NEW) {
                newStatuses += 1;
            } else if (subtask.getStatus() == Statuses.DONE) {
                doneStatuses += 1;
            }
        }

        if (newStatuses == subtasksIdList.size() || epic.getSubtasksId().isEmpty()) {
            epic.setStatus(Statuses.NEW);
        } else if (doneStatuses == subtasksIdList.size()) {
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
        ArrayList<Epic> epicsList = new ArrayList<>(epics.values());

        return epicsList;
    }

    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        ArrayList<Subtask> subtasksList = new ArrayList<>();

        for (int subtaskId : epic.getSubtasksId()) {
            Subtask subtask = subtasks.get(subtaskId);
            subtasksList.add(subtask);
        }

        return subtasksList;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtasksList = new ArrayList<>(subtasks.values());

        return subtasksList;
    }

    public Task getTask(int taskId) {
        return tasks.get(taskId);
    }

    public Epic getEpic(int epicId) {
        return epics.get(epicId);
    }

    public Subtask getSubtask(int subtaskId) {
        return subtasks.get(subtaskId);
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

    public void deleteTask(int taskId) {
        tasks.remove(taskId);
    }

    public void deleteEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);

            ArrayList<Integer> subtasksIdList = epic.getSubtasksId();

            for (Integer subtaskId : subtasksIdList) {
                subtasks.remove(subtaskId);
            }

            epics.remove(epicId);
        }
    }

    public void deleteSubtask(Integer subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);

        if (subtask != null) {
            subtasks.remove(subtaskId);

            Epic epic = epics.get(subtask.getEpicId());

            epic.deleteSubtaskId(subtaskId);

            checkEpicStatus(epic.getIdentificationNumber());
        }
    }

    public  void deleteAllTasks() {
        tasks.clear();;
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.deleteAllSubtasksId();
            checkEpicStatus(epic.getIdentificationNumber());
        }
    }
}
