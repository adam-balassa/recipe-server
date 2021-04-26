Feature: Delete a recipe
  Scenario: After I delete a recipe I can't retrieve it
    Given the following recipes are in the database
      | id        | name          |
      | testId1   | test recipe 1 |
      | testId2   | test recipe 2 |
    When I delete a recipe with the id testId1
    Then it succeeds
    When I request getting all recipes
    Then I receive the following recipes
      | id        | name          |
      | testId2   | test recipe 2 |


  Scenario: After I delete a recipe that does not exist
    Given the following recipes are in the database
      | id        | name          |
      | testId1   | test recipe 1 |
      | testId2   | test recipe 2 |
    When I delete a recipe with the id invalidId
    Then it succeeds
    When I request getting all recipes
    Then I receive the following recipes
      | id        | name          |
      | testId1   | test recipe 1 |
      | testId2   | test recipe 2 |