package manager;

import exceptions.NotFoundException;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;
import java.util.TreeSet;

public interface TaskManager<T extends Task> {

    void createTask(Task task);

    void createEpic(Epic epic);

    void createAndAddSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getEpicSubtasks(Epic epic);

    List<Subtask> getSubtasks();

    Task getTask(int taskId) throws NotFoundException;

    Epic getEpic(int epicId) throws NotFoundException;

    Subtask getSubtask(int subtaskId) throws NotFoundException;

    List<Task> getHistory();

    TreeSet<Task> getPrioritizedTasks();

    void printAllTasks();

    void deleteTask(int taskId);

    void deleteEpic(int epicId);

    void deleteSubtask(Integer subtaskId);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();
}
