import java.util.ArrayList;
import java.util.Arrays;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description, Statuses.NEW);
    }

    public Epic(String taskName, String description, int identificationNumber) {
        super(taskName, description, identificationNumber, Statuses.NEW);
    }

    public void addToSubtaskId(int subtaskId) {
        subtasksId.add(subtaskId);
    }

    public void deleteSubtaskId(int id) {
        int index = subtasksId.indexOf(id);
        subtasksId.remove(index);
    }

    public void deleteAllSubtasksId() {
        subtasksId.clear();
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskName='" + super.getTaskName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", identificationNumber=" + super.getIdentificationNumber() +
                ", subtasksId=" + subtasksId.toString() +
                ", status=" + super.getStatus() +
                '}';
    }
}
