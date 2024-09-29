import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description);
    }

    public Epic(String taskName, String description, int identificationNumber, Statuses status) {
        super(taskName, description, identificationNumber, status);
    }

    public void addToSubtaskId(int subtaskId) {
        subtasksId.add(subtaskId);
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
