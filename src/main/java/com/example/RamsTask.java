package com.example;

import java.util.UUID;

public class RamsTask {
    private UUID taskUvid;
    private TaskDetails taskDetails;
    private UUID rateAmendmentUvid;

    // Constructor
    public RamsTask(UUID taskUvid, TaskDetails taskDetails, UUID rateAmendmentUvid) {
        this.taskUvid = taskUvid;
        this.taskDetails = taskDetails;
        this.rateAmendmentUvid = rateAmendmentUvid;
    }

    // Getters
    public UUID getTaskUvid() {
        return taskUvid;
    }

    public TaskDetails getTaskDetails() {
        return taskDetails;
    }

    public UUID getRateAmendmentUvid() {
        return rateAmendmentUvid;
    }
}
