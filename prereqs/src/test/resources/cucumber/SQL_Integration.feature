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

  Scenario: Create "REGISTRATION" table in mySQL on the local machine
  The app should be able to create a table in a database
    Given a mySQL server is running on the local machine
    And the app connects to the "RECORDS" database on the server 
    When the app tries to create the registration table
    Then the database should contain a blank "registration" table

  Scenario: Create "PREREQS" table in mySQL on the local machine
  The app should be able to create a table in a database
    Given a mySQL server is running on the local machine
    And the app connects to the "RECORDS" database on the server 
    When the app tries to create the prereqs table
    Then the database should contain a blank "prereqs" table

  Scenario Outline: Import data from CSV file into mySQL server
  The app shoul be able to import any csv file of the appropriate form into the table
    Given a mySQL server is running on the local machine
    And the app connects to the "RECORDS" database on the server 
    When the app tries to import the data from "<filepath>" into "<table>"
    Then the database should be updated

Examples:
| filepath                                           | table        | 
| src/test/resources/data/full/anon1810.csv | registration |
| src/test/resources/data/scrapedPrereqs.csv         | prereqs      |

  Scenario: Query for people in a class
    Given the class code "CS12001"
    And the term code "201420"
    And the app connects to the "RECORDS" database on the server
    When the app runs the query
    Then the first name should be "Anne Luther"

  Scenario: Test the table formatters output
    Given the app connects to the "RECORDS" database on the server
    And the app executes an arbitrary query
    Then something should be printed

  Scenario: CLASS_TAKEN function returns if people have taken a certain course
    Given the class code "CS374"
    And the section code "01"
    And the term code "201410"
    And the app connects to the "RECORDS" database on the server
    When the app runs the new query
    Then the list of who have taken the class will be printed

  Scenario Outline: Query for prereqs of a course
    Given the app connects to the "RECORDS" database on the server
    And the app queries for prereqs of "<subjectCode>" "<subjectNum>"
    Then "<prereq>" should be the first result

  Examples:
  | subjectCode | subjectNum | prereq             |
  | CS          | 374        | CS230              |
  | ACCT        | 499        | ACCT302,ACCT310    |
  | ANSC        | 345        | ANSC343,ANSC363orANSC483|
