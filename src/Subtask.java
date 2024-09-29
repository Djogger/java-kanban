public class Subtask extends Task{
    private int epicId;

    public Subtask(String taskName, String description) {
        super(taskName, description);
    }

    public Subtask(String taskName, String description, int identificationNumber, Statuses status) {
        super(taskName, description, identificationNumber, status);
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "taskName='" + super.getTaskName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", identificationNumber=" + super.getIdentificationNumber() +
                ", epicId=" + epicId +
                ", status=" + super.getStatus() +
                '}';
    }
}
