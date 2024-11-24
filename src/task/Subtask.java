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
        return String.format("%s,%s,%s,%s,%s,%s", getIdentificationNumber(), getClass(), getTaskName(), getStatus(), getDescription(), getEpicId());
    }

}
