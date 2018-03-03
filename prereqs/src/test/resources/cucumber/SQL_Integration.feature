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

  Scenario Outline: Import data from CSV file into mySQL server
  The app shoul be able to import any csv file of the appropriate form into the table
    Given a mySQL server is running on the local machine
    And the app connects to the "RECORDS" database on the server 
    When the app tries to import the data from "<filepath>"
    Then the database should be updated

Examples:
| filepath |
| src/test/resources/data/full/registration_anon.csv |


  Scenario: Test output of people in a class in the term 201410
    Given the class code "CS37401"
    And the term code "201410"
    And the app connects to the "RECORDS" database on the server
    When the app runs the query
    Then the first name should be "Sherry Colquitt"

  Scenario: Test the table formatters output
    Given the app executes an arbitrary query
    And the app connects to the "RECORDS" database on the server
    Then something should be printed

  Scenario: Test the CLASS_TAKEN function
    Given the class code "CS374"
    And the section code "01"
    And the term code "201410"
    And the app connects to the "RECORDS" database on the server
    When the app runs the new query
    Then the list of who have taken the class will be printed
