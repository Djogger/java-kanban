package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public Subtask(String taskName, String description, int epicId, Statuses status, LocalDateTime startTime, Duration duration) {
        super(taskName, description, status, startTime, duration);

        this.epicId = epicId;
    }

    public Subtask(String taskName, String description, int identificationNumber, int epicId, Statuses status, LocalDateTime startTime, Duration duration) {
        super(taskName, description, identificationNumber, status, startTime, duration);

        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", getIdentificationNumber(), getClass(), getTaskName(), getStatus(), getDescription(), getEpicId(), getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")), getDuration().toMinutes(), getEndTime().format(DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")));
    }

}
