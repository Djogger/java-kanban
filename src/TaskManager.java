import java.util.ArrayList;

public interface TaskManager<T extends Task> {

     void createTask(Task task);

     void createEpic(Epic epic);

     void createAndAddSubtask(Subtask subtask);

     void updateTask(Task task);

     void updateEpic(Epic epic);

     void updateSubtask(Subtask subtask);

     ArrayList<Task> getTasks();

     ArrayList<Epic> getEpics();

     ArrayList<Subtask> getEpicSubtasks(Epic epic);

     ArrayList<Subtask> getSubtasks();

     Task getTask(int taskId);

     Epic getEpic(int epicId);

     Subtask getSubtask(int subtaskId);

      ArrayList<Task> getHistory();

     void printAllTasks();

     void deleteTask(int taskId);

     void deleteEpic(int epicId);

     void deleteSubtask(Integer subtaskId);

     void deleteAllTasks();

     void deleteAllEpics();

     void deleteAllSubtasks();
}
