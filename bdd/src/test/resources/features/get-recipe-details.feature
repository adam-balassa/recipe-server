Feature: Get a recipe
  Scenario: Getting a recipe's detail
    Given the following recipe is in the database
      | id                | testId                        |
      | name              | test recipe                   |
      | category          | MAIN                          |
      | imageUrl          | https://example.com           |
      | quantity          | 4                             |
      | quantity2         | 5                             |
      | instructions      | instruction 1, instruction 2  |
      | isVegetarian      | true                          |
    When I retrieve recipe with id testId
    Then I receive the following recipe details
      | id                | testId                        |
      | name              | test recipe                   |
      | category          | MAIN                          |
      | imageUrl          | https://example.com           |
      | quantity          | 4                             |
      | quantity2         | 5                             |
      | instructions      | instruction 1, instruction 2  |
      | isVegetarian      | true                          |

  Scenario: Getting recipe with invalid id
    Given there are 3 recipes in the database
    When I retrieve recipe with id invalidId
    Then I receive a recipe not found error