package manager;

import task.Epic;
import task.Statuses;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTaskManager<T extends Task> implements TaskManager<T> {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private HistoryManager historyManager;
    private int id = 1;


    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public void createTask(Task task) {
        task.setIdentificationNumber(id);
        tasks.put(id, task);
        id += 1;
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setIdentificationNumber(id);
        epic.setStatus(Statuses.NEW);
        epics.put(id, epic);
        id += 1;
    }

    @Override
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

    protected void restoreTask(Task task, int lastId) {
        tasks.put(task.getIdentificationNumber(), task);
        id = lastId + 1;
    }

    protected void restoreEpic(Epic epic, int lastId) {
        epics.put(epic.getIdentificationNumber(), epic);
        id = lastId + 1;
    }

    protected void restoreSubtask(Subtask subtask, int lastId) {
        if (epics.get(subtask.getEpicId()) == null) {
            return;
        }

        Epic epic = epics.get(subtask.getEpicId());

        epic.addToSubtaskId(subtask.getIdentificationNumber());

        subtasks.put(subtask.getIdentificationNumber(), subtask);

        id = lastId + 1;

        checkEpicStatus(epic.getIdentificationNumber());
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getIdentificationNumber())) {
            tasks.put(task.getIdentificationNumber(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getIdentificationNumber())) {
            Epic epicFromList = epics.get(epic.getIdentificationNumber());
            epicFromList.setTaskName(epic.getTaskName());
            epicFromList.setDescription(epic.getDescription());
        }
    }

    @Override
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

        if (!subtasksIdList.isEmpty()) {
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
        } else {
            epic.setStatus(Statuses.NEW);
        }
    }

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();

        tasksList.addAll(tasks.values());

        return tasksList;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicsList = new ArrayList<>(epics.values());

        return epicsList;
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        ArrayList<Subtask> subtasksList = new ArrayList<>();

        for (int subtaskId : epic.getSubtasksId()) {
            Subtask subtask = subtasks.get(subtaskId);
            subtasksList.add(subtask);
        }

        return subtasksList;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtasksList = new ArrayList<>(subtasks.values());

        return subtasksList;
    }

    @Override
    public Task getTask(int taskId) {
        Task task = tasks.get(taskId);

        if (task != null) {
            historyManager.add(task);
        }

        return task;
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = epics.get(epicId);

        if (epic != null) {
            historyManager.add(epic);
        }

        return epic;
    }

    @Override
    public Subtask getSubtask(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);

        if (subtask != null) {
            historyManager.add(subtask);
        }

        return subtask;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return (ArrayList<Task>) historyManager.getHistory();
    }

    @Override
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

    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);

            ArrayList<Integer> subtasksIdList = epic.getSubtasksId();

            for (Integer subtaskId : subtasksIdList) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }

            epics.remove(epicId);
            historyManager.remove(epicId);
        }
    }

    @Override
    public void deleteSubtask(Integer subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);

        if (subtask != null) {
            subtasks.remove(subtaskId);

            Epic epic = epics.get(subtask.getEpicId());

            epic.deleteSubtaskId(subtaskId);

            checkEpicStatus(epic.getIdentificationNumber());
            historyManager.remove(subtaskId);
        }
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getIdentificationNumber());
        }

        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getIdentificationNumber());
        }
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getIdentificationNumber());
        }

        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getIdentificationNumber());
        }

        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.deleteAllSubtasksId();
            checkEpicStatus(epic.getIdentificationNumber());
        }
    }
}
