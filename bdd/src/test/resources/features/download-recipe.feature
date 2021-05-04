Feature: Download Street Kitchen recipe

  Scenario Outline: Downloading a Street Kitchen recipe will save it in the database
    Given there are 0 recipes in the database
    When I download a Street Kitchen recipe from <URL>
    And I request getting all recipes
    Then I receive list of 1 recipe

    Examples:
      | URL                                                               |

      | https://streetkitchen.hu/green-kitchen/vegan/vegan-gulyasleves/   |

  Scenario: Downloading a Street Kitchen recipe will create a matching recipe
    When I download a Street Kitchen recipe from https://streetkitchen.hu/green-kitchen/vegan/vegan-gulyasleves/
    Then I receive the following recipe
      | name              | Vegán gulyásleves |
      | ingredientSize    | 1                 |
      | instructionSize   | 3                 |
      | quantity          | 5                 |
      | quantity2         | 6                 |

  Scenario: A StreetKitchen recipe's images are stored on Amazon
    When I download a Street Kitchen recipe from https://streetkitchen.hu/green-kitchen/vegan/vegan-gulyasleves/
    Then the result recipe's image is stored on amazon

  Scenario: Invalid URL is handled
    When I download a Street Kitchen recipe from anInvalidURL
    Then I receive a bad request error