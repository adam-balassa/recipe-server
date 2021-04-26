Feature: Add recipe
  Scenario: Adding simple recipe
    When I upload a recipe
    Then I receive a response of that recipe

  Scenario: After I upload a recipe I can retrieve it
    Given there is 1 recipe in the database
    When I upload a recipe
    And I request getting all recipes
    Then I receive list of 2 recipes