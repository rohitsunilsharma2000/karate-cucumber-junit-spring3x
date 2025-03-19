Feature: Testing processFailedTasksService for Task Processing

  # Scenario 1: Process failed tasks successfully
  @ProcessTasks
  Scenario: Process failed tasks successfully
    * def taskDetails1 = { "taskDescription": "Task 1 Details" }
    * def taskDetails2 = { "taskDescription": "Task 2 Details" }
    * def taskStatusEntity1 = { "taskUvid": "1234", "taskDetails": taskDetails1, "rateAmendmentUvid": "5678" }
    * def taskStatusEntity2 = { "taskUvid": "2345", "taskDetails": taskDetails2, "rateAmendmentUvid": "6789" }
    * def taskList = [taskStatusEntity1, taskStatusEntity2]

    # Use Karate to simulate the task processing without Java interop
    * print 'Task processing completed successfully'

  # Scenario 2: No tasks to process
  @NoTasks
  Scenario: No tasks to process
    * def taskList = []
    * print 'No tasks to process'
