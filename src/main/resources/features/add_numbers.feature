Feature: Add two numbers

  @AddNumbersTest
  Scenario: Adding two numbers 5 and 10
    * def num1 = 5
    * def num2 = 10
    * def result = num1 + num2
    * match result == 15
