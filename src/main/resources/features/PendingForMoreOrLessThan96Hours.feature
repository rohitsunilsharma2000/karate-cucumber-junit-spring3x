Feature: System abandonment for eligibility tasks pending for more than 96 hours

  # Scenario 1: Task is pending for more than 96 hours and should be marked as exhausted and abandoned
  @PendingForMoreThan96Hours
  Scenario: Task is pending for more than 96 hours and should be marked as exhausted and abandoned
    * def taskUvid = '1234'
    * def status = 'pending'
    * def createdTime = '2025-03-01T10:00:00'
     # Task created 97 hours ago
    * def currentTime = '2025-03-10T09:00:00'
    # Current time is 97 hours after the task creation time
    * def maxPendingTime = 96
    # Threshold for abandonment in hours

    # Calculate the time difference in hours between createdTime and currentTime
    * def timeDifference = (new Date(currentTime) - new Date(createdTime)) / (1000 * 3600)
    # Convert milliseconds to hours

    # Debug: Print the calculated time difference to verify
    * print 'Calculated Time Difference (hours): ' + timeDifference

    # Conditional check to determine if the task should be exhausted and abandoned
    * def status = timeDifference > maxPendingTime ? 'exhausted and LRA system abandoned' : 'pending'

    # Check the result
    * match status == 'exhausted and LRA system abandoned'
    * print 'Task status after 96 hours: ' + status


  # Scenario 2: Task is still within the 96 hours limit and is not yet marked as exhausted
  @PendingForLessThan96Hours
  Scenario: Task is still within 96 hours and is not yet marked as exhausted
    * def taskUvid = '2345'
    * def status = 'pending'
    * def createdTime = '2025-03-03T10:00:00'
    * def currentTime = '2025-03-05T10:00:00'
    * def maxPendingTime = 96
       # Threshold for abandonment in hours

    # Calculate the time difference in hours between createdTime and currentTime
    * def timeDifference = (new Date(currentTime) - new Date(createdTime)) / (1000 * 3600)
       # Convert milliseconds to hours

    # Debugging: Print the calculated time difference to verify it's less than 96 hours
    * print 'Calculated Time Difference (hours): ' + timeDifference

    # Conditional check to determine if the task is still pending
    * def status = timeDifference > maxPendingTime ? 'exhausted and LRA system abandoned' : 'pending'

    # Check if the task is still pending
    * match status == 'pending'
       # The task should still be pending as it hasn't crossed 96 hours yet
    * print 'Task status within 96 hours: ' + status
