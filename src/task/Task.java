package task;

import java.util.Objects;

public class Task {
    private String taskName;
    private String description;
    private int identificationNumber;
    private Statuses status;


    public Task(String taskName, String description, Statuses status) {
        this.taskName = taskName;
        this.description = description;
        this.identificationNumber = 0;
        this.status = status;
    }

    public Task(String taskName, String description, int identificationNumber, Statuses status) {
        this.taskName = taskName;
        this.description = description;
        this.identificationNumber = identificationNumber;
        this.status = status;
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
        return String.format("%s,%s,%s,%s,%s,", getIdentificationNumber(), getClass(), getTaskName(), getStatus(), getDescription());
    }

}
