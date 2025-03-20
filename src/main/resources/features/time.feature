Feature: Java Interop for TimeUtils

  Background:
    * def TimeUtils = Java.type('com.example.TimeUtils')
    * def toInstant =
      """
      function(timestamp) {
        return timestamp ? java.time.Instant.parse(timestamp) : null;
      }
      """
    * def inputData =
      """
      [
        { "currentTimeStamp": "2025-03-10T10:00:00Z", "timeStamp": null, "delayTime": 10, "expectedResult": false },
        { "currentTimeStamp": "2025-03-10T10:00:00Z", "timeStamp": "2025-03-01T10:00:00Z", "delayTime": 96, "expectedResult": true },
        { "currentTimeStamp": "2025-03-10T10:00:00Z", "timeStamp": "2025-03-10T09:00:00Z", "delayTime": 120, "expectedResult": false },
        { "currentTimeStamp": "2025-03-10T10:00:00Z", "timeStamp": "2025-03-05T09:00:00Z", "delayTime": 96, "expectedResult": true },
        { "currentTimeStamp": "2025-03-10T10:00:00Z", "timeStamp": "2025-03-09T12:00:00Z", "delayTime": 96, "expectedResult": false }
      ]
      """

  Scenario: Run multiple test cases dynamically
    * def results = []
    * eval
      """
      for (var i = 0; i < inputData.length; i++) {
          var testCase = inputData[i];
          var currentTime = toInstant(testCase.currentTimeStamp);
          var pastTime = toInstant(testCase.timeStamp);
          var delay = testCase.delayTime;
          var result = TimeUtils.isTimeExceedThanLraRequired(currentTime, pastTime, delay);
          results.push({ "input": testCase, "result": result, "expected": testCase.expectedResult, "match": result === testCase.expectedResult });
      }
      """
    * print results
