Project Name: Pre-Req Checker 

Team Members: Gideon Luck, John Marsden, John Roper, Matthew Williams

Requirements: A list of students in your class that don't have the required prerequisites

Final Week:
=======
How to Run
========================
To build, enter prereqs directory and execute `mvn package`. This will build the
project into a compiled JAR. 

This may take a while on the first go, because it will first have to download
all of the dependencies. These currently include Cucumber, jUnit, the Cucumber
jUnit Runner, and JSEFA.
Maven will automatically locate these repositiories and install them in your
`$(user.home)/.m2/repositories/` directory. 

Commands that work in our Java app
========================
`who is in <subject> <classnumber>.<sectionnumber>` : where \<subject\> is like 'CS' and \<classnumber\> is like '274' and  \<sectionnumber\> is like '01'. This will give a list of all students enrolled in the section given.

`who is not qualified for <subject> <classnumber>.<sectionnumber>` : This is the primary functionality of our app. It outputs the students that do not meet the prereqs for thw course, and which prereqs they do not meet. 

`what are the prereqs for <subject> <classname>` : This tells the user what the prereqs are for a given course. 

`end`: ends the project and exits.

`help` : will output this list of commands to the terminal.


Directory Structure
-------------------
+ Implementation should be in `prereqs/src/main/java/com/blinkendorf/app/*.java`.
+ Cucumber features should be in `prereqs/src/test/resources/cucumber/*.feature`.
  *These could really be anywhere, see "Running Cucumber tests..." below.*
+ Cucumber Step Definitons should be in `prereqs/src/test/java/com/blinkendorf/app/step_definitions/*.java`.
+ Cucumber jUnit runner should be in `prereqs/src/test/java/com/blinkendorf/app/CucumberTest.java`


Maven basic commands
---------------
`mvn validate`: validate the project is correct and all necessary information is
available

`mvn compile`: compile the source code of the project

`mvn test`: test the compiled source code using a suitable unit testing
framework. These tests should not require the code be packaged or deployed

`mvn package`: take the compiled code and package it in its distributable
format, such as a JAR.

`mvn integration-test`: process and deploy the package if necessary into an
environment where integration tests can be run

`mvn verify`: run any checks to verify the package is valid and meets quality
criteria

`mvn install`: install the package into the local repository, for use as a
dependency in other projects locally

`mvn deploy`: done in an integration or release environment, copies the final
package to the remote repository for sharing with other developers and projects.

`mvn javadoc:javadoc`: done in the prereqs directory will compile an HTML file with our documentation . 

Running Cucumber tests with jUnit
-----------------------------

You just need to include the following code in its own class, nothing else
required, inside `/src/test/java/.../app/`

```
package mypackage;

import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
public class RunCukesTest {
}
```

This will activate cucumber when `mvn test` is run. To specify format options
and where to look for feature files, the following must be added on the line
between `@RunWith...` and `public...`:
```
@CucumberOptions(
    format = { "pretty", "html:target/cucumber" },
    features = "classpath:cucumber/calculator.feature"
)

What will we need: A parser, 
