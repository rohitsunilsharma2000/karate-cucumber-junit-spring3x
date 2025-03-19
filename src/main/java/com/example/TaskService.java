package com.example;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TaskService {

    public void processFailedTasksService(Optional<List<RamsTask>> optionalTaskStatusEntityList) {
        if (optionalTaskStatusEntityList.isPresent()) {
            List<RamsTask> taskStatusEntities = optionalTaskStatusEntityList.get();
            System.out.println("Number of failed transactions: " + taskStatusEntities.size());
            for (RamsTask taskStatusEntity : taskStatusEntities) {
                Optional<TaskDetails> optionalTaskDetails = Optional.ofNullable(taskStatusEntity.getTaskDetails());
                if (optionalTaskDetails.isPresent() && calculateNextRetryTimeAndEnable(taskStatusEntity)) {
                    TaskDetails taskDetails = taskStatusEntity.getTaskDetails();
                    UUID taskUvid = taskStatusEntity.getTaskUvid();
                    Optional<UUID> optionalTaskUUID = Optional.ofNullable(taskUvid);
                    UUID rateAmendmentUvid = taskStatusEntity.getRateAmendmentUvid();
                    if (taskUvid != null && optionalTaskUUID.isPresent()) {
                        validateTaskAndInvoke(taskUvid, rateAmendmentUvid, taskDetails);
                    }
                }
            }
        }
    }

    public boolean calculateNextRetryTimeAndEnable(RamsTask taskStatusEntity) {
        return true;  // Mocking the retry logic
    }

    public void validateTaskAndInvoke(UUID taskUvid, UUID rateAmendmentUvid, TaskDetails taskDetails) {
        System.out.println("Validating and invoking task: " + taskUvid);
    }
}
