package manager;

import task.Epic;
import task.Statuses;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager<T extends Task> implements TaskManager<T> {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private TreeSet<Task> prioritizedTasks = new TreeSet<>();
    private HistoryManager historyManager;
    private int id = 1;


    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public void createTask(Task task) {
        boolean hasOverlap = prioritizedTasks.stream()
                .anyMatch(taskFromList -> isOverlapping(taskFromList, task));

        if (hasOverlap) {
            throw new IllegalArgumentException("Задача пересекается с существующей задачей.");
        }

        task.setIdentificationNumber(id);
        tasks.put(id, task);

        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }

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

        boolean hasOverlap = prioritizedTasks.stream()
                .anyMatch(subtaskFromList -> isOverlapping(subtaskFromList, subtask));

        if (hasOverlap) {
            throw new IllegalArgumentException("Подзадача пересекается с существующей задачей.");
        }

        Epic epic = epics.get(subtask.getEpicId());

        epic.addToSubtaskId(id);

        subtask.setIdentificationNumber(id);

        subtasks.put(subtask.getIdentificationNumber(), subtask);

        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }

        id += 1;

        checkEpicStatus(epic.getIdentificationNumber());
        calculateEpicDurationAndStartTimeAndEndTime(epic.getIdentificationNumber());
    }

    protected void restoreTask(Task task, int lastId) {
        tasks.put(task.getIdentificationNumber(), task);

        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }

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

        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }

        id = lastId + 1;

        checkEpicStatus(epic.getIdentificationNumber());
        calculateEpicDurationAndStartTimeAndEndTime(epic.getIdentificationNumber());
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getIdentificationNumber())) {
            boolean hasOverlap = prioritizedTasks.stream()
                    .anyMatch(taskFromList -> isOverlapping(taskFromList, task));

            if (hasOverlap) {
                throw new IllegalArgumentException("Задача пересекается с существующей задачей.");
            }

            tasks.put(task.getIdentificationNumber(), task);
            prioritizedTasks = prioritizedTasks.stream()
                    .filter(taskFromPrioritizedList -> taskFromPrioritizedList.getIdentificationNumber() != task.getIdentificationNumber())
                    .collect(Collectors.toCollection(TreeSet::new));

            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
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
                boolean hasOverlap = prioritizedTasks.stream()
                        .anyMatch(subtaskFromListToCheck -> isOverlapping(subtaskFromListToCheck, subtask));

                if (hasOverlap) {
                    throw new IllegalArgumentException("Подзадача пересекается с существующей задачей.");
                }

                subtasks.put(key, subtask);
                prioritizedTasks = prioritizedTasks.stream()
                        .filter(subtaskFromPrioritizedList -> subtaskFromPrioritizedList.getIdentificationNumber() != subtask.getIdentificationNumber())
                        .collect(Collectors.toCollection(TreeSet::new));

                if (subtask.getStartTime() != null) {
                    prioritizedTasks.add(subtask);
                }

                checkEpicStatus(subtask.getEpicId());
                calculateEpicDurationAndStartTimeAndEndTime(subtask.getEpicId());
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

    private void calculateEpicDurationAndStartTimeAndEndTime(int epicId) {
        Epic epic = epics.get(epicId);

        if (epic.getSubtasksId().isEmpty()) {
            epic.setStartTime(LocalDateTime.now());
            epic.setDuration(Duration.ofMinutes(0));
            return;
        }

        Optional<LocalDateTime> startTime = epic.getSubtasksId().stream()
                .map(subtaskId -> subtasks.get(subtaskId).getStartTime())
                .min(LocalDateTime::compareTo);

        int duration = epic.getSubtasksId().stream()
                .map(subtaskId -> subtasks.get(subtaskId))
                .mapToInt(subtask -> (int) Duration.between(subtask.getStartTime(), subtask.getEndTime()).toMinutes())
                .sum();

        Optional<LocalDateTime> endTime = epic.getSubtasksId().stream()
                .map(subtaskId -> subtasks.get(subtaskId).getEndTime())
                .max(LocalDateTime::compareTo);

        startTime.ifPresent(epic::setStartTime);
        epic.setDuration(Duration.ofMinutes(duration));
        endTime.ifPresent(epic::setEndTime);
    }

    private boolean isOverlapping(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();

        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        return (start1.isBefore(end2) && end1.isAfter(start2));
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
    public List<Subtask> getEpicSubtasks(Epic epic) {
        List<Subtask> subtasksList = epic.getSubtasksId().stream()
                .map(subtaskId -> subtasks.get(subtaskId))
                .collect(Collectors.toList());

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

    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public void printAllTasks() {
        getTasks().stream()
                .peek(System.out::println)
                .forEach(task -> {
                });

        getEpics().stream()
                .peek(System.out::println)
                .forEach(task -> {
                });

        getSubtasks().stream()
                .peek(System.out::println)
                .forEach(task -> {
                });
    }

    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
        historyManager.remove(taskId);
        prioritizedTasks = prioritizedTasks.stream()
                .filter(taskFromPrioritizedList -> taskFromPrioritizedList.getIdentificationNumber() != taskId)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public void deleteEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);

            ArrayList<Integer> subtasksIdList = epic.getSubtasksId();

            subtasksIdList.forEach(subtaskId -> {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            });

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
            calculateEpicDurationAndStartTimeAndEndTime(epic.getIdentificationNumber());
            historyManager.remove(subtaskId);
            prioritizedTasks = prioritizedTasks.stream()
                    .filter(subtaskFromPrioritizedList -> subtaskFromPrioritizedList.getIdentificationNumber() != subtaskId)
                    .collect(Collectors.toCollection(TreeSet::new));
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.values().forEach(task -> {
            historyManager.remove(task.getIdentificationNumber());
        });

        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        subtasks.values().forEach(subtask -> {
            historyManager.remove(subtask.getIdentificationNumber());
        });

        epics.values().forEach(epic -> {
            historyManager.remove(epic.getIdentificationNumber());
        });

        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.values().forEach(subtask -> {
            historyManager.remove(subtask.getIdentificationNumber());
        });

        subtasks.clear();

        epics.values().forEach(epic -> {
            epic.deleteAllSubtasksId();
            checkEpicStatus(epic.getIdentificationNumber());
            calculateEpicDurationAndStartTimeAndEndTime(epic.getIdentificationNumber());
        });
    }
}
