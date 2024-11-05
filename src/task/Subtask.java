package task;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String taskName, String description, int epicId, Statuses status) {
        super(taskName, description, status);

        this.epicId = epicId;
    }

    public Subtask(String taskName, String description, int identificationNumber, int epicId, Statuses status) {
        super(taskName, description, identificationNumber, status);

        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "task.Subtask{" +
                "taskName='" + super.getTaskName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", identificationNumber=" + super.getIdentificationNumber() +
                ", epicId=" + epicId +
                ", status=" + super.getStatus() +
                '}';
    }
}
