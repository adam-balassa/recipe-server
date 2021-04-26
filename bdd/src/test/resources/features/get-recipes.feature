Feature: Get all recipes
  Scenario: Getting one recipe
    Given there is 1 recipe in the database
    When I request getting all recipes
    Then I receive list of 1 recipe
    And the recipes ids match

  Scenario: Getting multiple recipes
    Given there are 3 recipes in the database
    When I request getting all recipes
    Then I receive list of 3 recipes
    And the recipes ids match

  Scenario: Getting no recipes
    Given there are 0 recipes in the database
    When I request getting all recipes
    Then I receive list of 0 recipe