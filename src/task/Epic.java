package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();
    private LocalDateTime endTime;


    public Epic(String taskName, String description) {
        super(taskName, description, Statuses.NEW);
        this.endTime = LocalDateTime.now();
    }

    public Epic(String taskName, String description, int identificationNumber) {
        super(taskName, description, identificationNumber, Statuses.NEW);
        this.endTime = LocalDateTime.now();
    }

    public Epic(String taskName, String description, int identificationNumber, Statuses status) {
        super(taskName, description, identificationNumber, status);
        this.endTime = LocalDateTime.now();
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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", getIdentificationNumber(), getClass(), getTaskName(), getStatus(), getDescription(), getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")), getDuration().toMinutes(), getEndTime().format(DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")));
    }

}
