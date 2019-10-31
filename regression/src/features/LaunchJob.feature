Feature: As an Admin I expect that I can start a batch job with parameters

  Scenario: Login with admin credentials
    Given I open the url "login"
    And   I clear the inputfield "#username"
    When  I add "user" to the inputfield "#username"
    And   I clear the inputfield "#password"
    And   I add "password" to the inputfield "#password"
    And   I click on the button "input[type='submit']"
    Then  I wait on element ".launch-batch-job" for 1000ms to be displayed
    Then  I select the 1st option for element "#launchJob\.jobType"
    And   I set "43" to the inputfield "#launchJob\.jobparameters"
