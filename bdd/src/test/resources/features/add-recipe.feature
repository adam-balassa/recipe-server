Feature: Add recipe
  Scenario: Adding simple recipe
    When I upload the following recipe
      | name              | test recipe                   |
      | category          | MAIN                          |
      | imageUrl          | https://amazonaws.com         |
      | quantity          | 4                             |
      | quantity2         | 5                             |
      | instructions      | instruction 1, instruction 2  |
      | isVegetarian      | true                          |
    Then I receive a response of the following recipe
      | name              | test recipe                   |
      | category          | MAIN                          |
      | imageUrl          | https://amazonaws.com         |
      | quantity          | 4                             |
      | quantity2         | 5                             |
      | instructions      | instruction 1, instruction 2  |
      | isVegetarian      | true                          |

  Scenario: After I upload a recipe I can retrieve it
    Given there is 1 recipe in the database
    When I upload the following recipe
      | name              | test recipe                   |
      | category          | MAIN                          |
      | imageUrl          | https://amazonaws.com         |
      | quantity          | 4                             |
      | quantity2         | 5                             |
      | instructions      | instruction 1, instruction 2  |
      | isVegetarian      | true                          |
    And I request getting all recipes
    Then I receive list of 2 recipes

  Scenario: Uploading a recipe with non-amazon-stored image
    When I upload the following recipe
      | name              | test recipe                   |
      | category          | MAIN                          |
      | imageUrl          | https://example.com           |
      | quantity          | 4                             |
      | quantity2         | 5                             |
      | instructions      | instruction 1, instruction 2  |
      | isVegetarian      | true                          |
    Then the result recipe's image is stored on amazon

  Scenario: Invalid request is handled
    When I try to upload an invalid recipe
    Then I receive a bad request error