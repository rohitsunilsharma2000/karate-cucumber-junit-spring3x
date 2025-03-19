Feature: Check if the time exceeds the LRA (Long Running Action) required time

  Background:
    * def isTimeExceedThanLraRequired =
        """
          function(currentTimeStamp, timeStamp, delayTime) {
              if (timeStamp === null) {
                  return false;
              }
              var currentTime = new Date(currentTimeStamp);
              var time = new Date(timeStamp);
              return currentTime > new Date(time.getTime() + (delayTime * 60 * 60000)); // Convert hours to milliseconds
          }
        """

  # Scenario 1: When eligibility task is pending for more than 96 hours, mark as exhausted and publish LRA as system abandoned
  @EligibilityPendingMoreThan96Hours
  Scenario: Eligibility task pending more than 96 hours, mark as exhausted
    * def currentTimeStamp = '2025-03-10T09:00:01'
    * def timeStamp = '2025-03-05T09:00:00'
      # 5 days ago
    * def delayTime = 96
      # 96 hours delay
    * def result = isTimeExceedThanLraRequired(currentTimeStamp, timeStamp, delayTime)
    * match result == true
         # matching  Actual result with Expected One
    * def status = 'EXHAUSTED'
    * def lraStatus = 'SYSTEM_ABANDONED'

    * match status == 'EXHAUSTED'
    * print 'Eligibility task pending for more than 96 hours, marking as exhausted'

    * match lraStatus == 'SYSTEM_ABANDONED'
    * print 'Eligibility task pending for more than 96 hours, publishing LRA as system abandoned'


  # Scenario 2: When eligibility task is pending for less than 96 hours, keep it active
  @EligibilityPendingLessThan96Hours
  Scenario: Eligibility task pending less than 96 hours, keep active
    * def currentTimeStamp = '2025-03-10T10:00:00'
    * def timeStamp = '2025-03-09T12:00:00'
      # 22 hours ago
    * def delayTime = 96
      # 96 hours delay
    * def result = isTimeExceedThanLraRequired(currentTimeStamp, timeStamp, delayTime)
     # Actual result
    * match result == false
      # Expected result
    * print 'Eligibility task pending for less than 96 hours, keeping active'
    * def status = 'ACTIVE'
    * match status == 'ACTIVE'
     # matching  Actual result with Expected One
