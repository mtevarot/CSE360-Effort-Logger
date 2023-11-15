package application;

import java.time.LocalDateTime;

public class EffortLog {
    private int id;
    private String projectName;
    private String lifeCycleStep;
    private String effortCategory;
    private String projectType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public EffortLog(int id, String projectName, String lifeCycleStep, String effortCategory, String projectType, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.projectName = projectName;
        this.lifeCycleStep = lifeCycleStep;
        this.effortCategory = effortCategory;
        this.projectType = projectType;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getLifeCycleStep() {
        return lifeCycleStep;
    }

    public String getEffortCategory() {
        return effortCategory;
    }

    public String getProjectType() {
        return projectType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}