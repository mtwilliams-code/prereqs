Feature: Integrate with remote AWS mySQL database
  The java app should be able to connect to a mySQL database 
  running on AWS, add records, and make queries.

  Scenario: Make a connection to mySQL on AWS
  The app should be able to establish a connection to a remote SQL server on AWS
    Given a mySQL server is running on AWS
    And the app is not already connected to a server
    When the app connects to the remote AWS server
    Then the connection should be valid

  Scenario: Query for people in a class
    Given the class code "CS12001"
    And the term code "201420"
    And the app connects to the "RECORDS" database on the AWS server
    When the app runs the query
    Then the first name should be "Danielle Brown"

  Scenario: Test the table formatters output
    Given the app connects to the "RECORDS" database on the AWS server
    And the app executes an arbitrary query
    Then something should be printed

  Scenario: CLASS_TAKEN function returns if people have taken a certain course
    Given the class code "CS374"
    And the section code "01"
    And the term code "201410"
    And the app connects to the "RECORDS" database on the AWS server
    When the app runs the new query
    Then the list of who have taken the class will be printed

  Scenario Outline: Query for prereqs of a course
    Given the app connects to the "RECORDS" database on the AWS server
    And the app queries for prereqs of "<subjectCode>" "<subjectNum>"
    Then "<prereq>" should be the first result

  Examples:
  | subjectCode | subjectNum | prereq             |
  | CS          | 374        | CS230              |
  | ACCT        | 499        | ACCT302,ACCT310    |
  | ANSC        | 345        | ANSC343,ANSC363orANSC483|

  Scenario Outline: Test PrereqCheck
  Given the app connects to the "RECORDS" database on the AWS server
  And the subject code "<subjectCode>"
  And the section code "<sectionCode>"
  And the term code "<termCode>"
  And the course number "<courseNumber>"
  When the app runs the PrereqCheck query
  Then the list of who don't have the prereqs will be printed

  Examples:
  | subjectCode | courseNumber | sectionCode | termCode |
  | ANSC        | 345          | 01          | 201810   |