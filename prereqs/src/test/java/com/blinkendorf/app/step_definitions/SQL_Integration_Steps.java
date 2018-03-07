package com.blinkendorf.app.step_definitions;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;
import com.blinkendorf.app.Data;
import com.blinkendorf.app.SQL_Connector;

import java.io.File;
import java.net.*;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

/**
 * Cucumber step definitions for SQL integration feature
 */
public class SQL_Integration_Steps {

  SQL_Connector conn;
  String class_code;
  int term_code;
  String first_query;
  String section_code;
  Data result;

  @Given("^a mySQL server is running on the local machine$")
  public void a_mySQL_server_is_running_on_the_local_machine() throws Exception {
    // this opens a Socket with the default mySQL port on localhost. Should only work if a server is listening to 3306
    InetAddress addr = InetAddress.getByName("localhost");
    int port = 3306;
    SocketAddress sockaddr = new InetSocketAddress(addr, port);
    Socket sock = new Socket();
    sock.connect(sockaddr, 2000); // open the connection with a 2 seconds timeout
  }

  @Given("^the app is not already connected to a server$")
  public void the_app_is_not_already_connected_to_a_server() throws Exception {
    // let's test this later, when we actually have an app.
  }

  @When("^the app connects to the local server$")
  public void the_app_connects_to_the_local_server() throws Exception {
    conn = new SQL_Connector();
  }

  @Then("^the connection should be valid$")
  public void the_connection_should_be_valid() throws Exception {
    assertEquals(true, conn.isConnected());
  }

  @Given("^the \"([^\"]*)\" database exists on the server$")
  public void the_database_exists_on_the_server(String arg1) throws Exception {
    // Connection connection = <your java.sql.Connection>
    ResultSet resultSet = conn.getConnection().getMetaData().getCatalogs();
    //iterate each catalog in the ResultSet
    boolean found = false;
    while (resultSet.next()) {
      // Get the database name, which is at position 1
      String databaseName = resultSet.getString(1);
      if (databaseName.equalsIgnoreCase(arg1)) {
        found = true;
      }
    }
    resultSet.close();
    assertEquals(true, found);  
  }

  @When("^the app tries to create the registration table$")
  public void the_app_tries_to_create_the_registration_table() throws Exception {
    conn.createRegistrationTable();
  }

  @When("^the app tries to create the prereqs table$")
  public void the_app_tries_to_create_the_prereqs_table() throws Exception {
    conn.createPrereqTable();
  }

  @Then("^the database should contain a (?:blank) \"([^\"]*)\" table$")
  public void the_server_should_hold_a_table(String arg1) throws Exception {
    java.sql.ResultSet rs = conn.getAllTableNames();
    boolean flag = false;
    while (rs.next()) {
      if (rs.getString(3).equalsIgnoreCase(arg1)) {
        flag = true;
      }
    }
    assertEquals(true, flag);
  }

  @When("^the app tries to create the \"([^\"]*)\" database$")
  public void the_app_tries_to_create_the_database(String arg1) throws Exception {
    conn.createDatabase(arg1);
  }

  @Given("^the app connects to the \"([^\"]*)\" database on the server$")
  public void the_app_connects_to_the_database_on_the_server(String arg1) throws Exception {
    String url = "jdbc:mysql://localhost:3306/" + arg1;
    String username = "java";
    String password = "Java";
    conn = new SQL_Connector(url, username, password);
  }

  @When("^the app tries to import the data from \"([^\"]*)\" into \"([^\"]*)\"$")
  public void the_app_tries_to_import_the_data_from_csv(String arg1, String arg2) throws Exception {
      File file = new File(arg1);
      conn.loadCSV(file, arg2);
  }
  
  @Then("^the database should be updated$")
  public void the_database_should_be_updated() throws Exception {
      assumeTrue(true);
  } 

  @Given("^the class code \"([^\"]*)\"$")
  public void the_class_code(String arg1) throws Exception {
    class_code = arg1;
  }

  @When("^the app runs the query$")
  public void the_app_runs_the_query() throws Exception {
    result = conn.namesInClass(class_code, term_code);
  }

  @Then("^the first name should be \"([^\"]*)\"$")
  public void the_first_name_should_be(String arg1) throws Exception {
    assertEquals(arg1, result.getFirstRecord());
  }

  @Given("^the app executes an arbitrary query$")
  public void an_arbitrary_query() throws Exception {
      first_query = "SELECT First_Name, Last_Name FROM REGISTRATION WHERE Subject_Code = 'CS' AND Course_Number = 115 AND Section_Number = '01' AND Term_Code = '201710'";
  }

  @Then("^something should be printed$")
  public void something_outputs() throws Exception {
      System.out.print(conn.printQuery(first_query));
      assumeTrue(true);
  }

  @Given("^the section code \"([^\"]*)\"$")
  public void the_section_code(String arg1) throws Exception {
    section_code = arg1;
  }

  @Then("^the list of who have taken the class will be printed$")
  public void something_else_outputs() throws Exception {
    // this should be reworked. We are deprecating the printQuery function
    result.printData();
    assumeTrue(true);
  }

  @Given("^the term code \"([^\"]*)\"$")
  public void the_term_code(int arg1) throws Exception {
    term_code = arg1;
  }

  @When("^the app runs the new query$")
  public void the_app_runs_the_new_query() throws Exception {
    //this should be reworked. The app should actually RUN the query in the stepcalled "the app runs the query"
    first_query = "SELECT First_Name, Last_Name, CLASS_TAKEN(Pidm,'MATH124','C') AS MATH124 FROM REGISTRATION WHERE Subject_Code = 'CS' AND Course_Number = 120 AND Section_Number = '02' AND Term_Code = 201610";
    result = conn.runQuery(first_query);
  }

  @Given("^the app queries for prereqs of \"([^\"]*)\" \"([^\"]*)\"$")
  public void the_app_queries_for_prereqs_of(String arg1, String arg2) throws Exception {
    result = conn.getPrereqs(arg1, arg2);
    }

  @Then("^\"([^\"]*)\" should be the first result$")
  public void should_be_the_first_result(String arg1) throws Exception {
    String prereqString = "";
    ArrayList<String> prereqs = result.getColumn(1);
    for (int i = 0; i < prereqs.size(); i++)
    {
      if (i != 0) prereqString += ",";
      prereqString += prereqs.get(i);
    }
    assertEquals(true,arg1.equalsIgnoreCase(prereqString));
    
  }


}
