import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> history = new ArrayList<>();

    public void add(Task task) {
        if (history.size() == 10) {
            history.remove(0);
        }

        history.add(task);
    }

    public ArrayList<Task> getHistory() {
        return history;
    }
}
