package manager;

import task.Task;

public class Managers {
    public static TaskManager<Task> getDefault() {
        return new InMemoryTaskManager<>();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
