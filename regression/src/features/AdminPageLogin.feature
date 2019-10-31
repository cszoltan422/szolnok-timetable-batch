Feature: As an Admin I expect that I can log into the application with my credentials

    Scenario: Login with admin credentials
        Given I open the url "login"
        And   I clear the inputfield "#username"
        When  I add "wrongusername" to the inputfield "#username"
        And   I clear the inputfield "#password"
        And   I add "wrongpassword" to the inputfield "#password"
        And   I click on the button "input[type='submit']"
        Then  I expect that element ".alert.alert-danger" is displayed
        And   I clear the inputfield "#username"
        When  I add "user" to the inputfield "#username"
        And   I clear the inputfield "#password"
        And   I add "password" to the inputfield "#password"
        And   I click on the button "input[type='submit']"
        Then  I expect that cookie "JSESSIONID" exists


    Scenario: As an Admin I would like to be able to logout
        Given I open the url "login"
        And   I clear the inputfield "#username"
        When  I add "user" to the inputfield "#username"
        And   I clear the inputfield "#password"
        And   I add "password" to the inputfield "#password"
        And   I click on the button "input[type='submit']"
        Then  I expect that element ".launch-batch-job" is displayed
        Then  I click on the button "a.nav-link.navbar-right.logout"
        Then  I expect that cookie "JSESSIONID" not exists
        And   I expect that element "#username" is displayed
