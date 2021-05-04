Feature: Get all recipes
  Scenario: Getting one recipe
    Given the following recipes are in the database
      | id        | name          |
      | testId1   | test recipe 1 |
    When I request getting all recipes
    Then I receive the following recipes
      | id        | name          |
      | testId1   | test recipe 1 |

  Scenario: Getting multiple recipes
    Given the following recipes are in the database
      | id        | name          |
      | testId1   | test recipe 1 |
      | testId2   | test recipe 2 |
      | testId3   | test recipe 3 |
    When I request getting all recipes
    Then I receive the following recipes
      | id        | name          |
      | testId1   | test recipe 1 |
      | testId2   | test recipe 2 |
      | testId3   | test recipe 3 |

  Scenario: Getting no recipes
    Given there are 0 recipes in the database
    When I request getting all recipes
    Then I receive list of 0 recipe