package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Comparable<Task> {
    private String taskName;
    private String description;
    private int identificationNumber;
    private Statuses status;
    private LocalDateTime startTime;
    private Duration duration;


    public Task(String taskName, String description, Statuses status) {
        this.taskName = taskName;
        this.description = description;
        this.identificationNumber = 0;
        this.status = status;
        this.startTime = null;
        this.duration = null;
    }

    public Task(String taskName, String description, int identificationNumber, Statuses status) {
        this.taskName = taskName;
        this.description = description;
        this.identificationNumber = identificationNumber;
        this.status = status;
        this.startTime = null;
        this.duration = null;
    }

    public Task(String taskName, String description, Statuses status, LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.description = description;
        this.identificationNumber = 0;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String taskName, String description, int identificationNumber, Statuses status, LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.description = description;
        this.identificationNumber = identificationNumber;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public void setIdentificationNumber(int identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Statuses status) {
        this.status = status;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    public int getIdentificationNumber() {
        return identificationNumber;
    }

    public Statuses getStatus() {
        return status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plusMinutes(duration.toMinutes());
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return identificationNumber == task.identificationNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificationNumber);
    }

    @Override
    public String toString() {
        if (getStartTime() != null && getDuration() != null && getEndTime() != null) {
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s", getIdentificationNumber(), getClass(), getTaskName(), getStatus(), getDescription(), getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")), getDuration().toMinutes(), getEndTime().format(DateTimeFormatter.ofPattern("HH:mm dd:MM:yy")));
        } else {
            return String.format("%s,%s,%s,%s,%s", getIdentificationNumber(), getClass(), getTaskName(), getStatus(), getDescription());
        }
    }

    @Override
    public int compareTo(Task other) {
        return this.startTime.compareTo(other.startTime);
    }

}
