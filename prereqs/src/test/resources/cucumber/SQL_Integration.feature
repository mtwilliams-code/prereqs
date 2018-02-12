Feature: Intergrate with mySQL database
  The java app should be able to connect to a mySQL database 
  running locally, add records, and make queries.

  Scenario: Make a connection to mySQL
    Given a mySQL server is running on the local machine
    And the app is not already connected to a server
    When the app tries to connect to the local server
    Then the connection should be valid