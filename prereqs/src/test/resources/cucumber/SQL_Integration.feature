Feature: Integrate with local mySQL database
  The java app should be able to connect to a mySQL database 
  running locally, add records, and make queries.

  Scenario: Make a connection to mySQL on the local machine
  The app should be able to establish a connection to a local SQL server
    Given a mySQL server is running on the local machine
    And the app is not already connected to a server
    When the app connects to the local server
    Then the connection should be valid

  Scenario: Create a database in mySQL on the local machine
  The app should be able to create a database
    Given a mySQL server is running on the local machine
    And the app connects to the local server
    When the app tries to create the "RECORDS" database
    Then the "RECORDS" database exists on the server

  Scenario: Create a table in mySQL on the local machine
  The app should be able to create a table in a database
    Given a mySQL server is running on the local machine
    And the app connects to the "RECORDS" database on the server 
    When the app tries to create the registration table
    Then the database should contain a blank "registration" table

  