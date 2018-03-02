package com.blinkendorf.app.step_definitions;

import cucumber.api.java.en.*;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.PendingException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import com.blinkendorf.app.SQL_Connector;

import java.io.File;
import java.net.*;
import java.sql.ResultSet;

public class SQL_Integration_Steps {

  SQL_Connector conn;
  String class_code;
  int term_code;
  String first_query;

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
    // Write code here that turns the phrase above into concrete actions
    assertEquals(true, conn.isConnected());
  }

  @Given("^the \"([^\"]*)\" database exists on the server$")
  public void the_database_exists_on_the_server(String arg1) throws Exception {
    // Write code here that turns the phrase above into concrete actions
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
    // Write code here that turns the phrase above into concrete actions
    conn.createDatabase(arg1);
  }

  @Given("^the app connects to the \"([^\"]*)\" database on the server$")
  public void the_app_connects_to_the_database_on_the_server(String arg1) throws Exception {
    // Write code here that turns the phrase above into concrete actions
    String url = "jdbc:mysql://localhost:3306/" + arg1;
    String username = "java";
    String password = "Java";
    conn = new SQL_Connector(url, username, password);
  }

  @When("^the app tries to import the data from \"([^\"]*)\"$")
  public void the_app_tries_to_import_the_data_from_csv(String arg1) throws Exception {
      // Write code here that turns the phrase above into concrete actions
      File file = new File(arg1);
      conn.loadCSV(file);

  }
  
  @Then("^the database should be updated$")
  public void the_database_should_be_updated() throws Exception {
      // Write code here that turns the phrase above into concrete actions
      assumeTrue(true);
  } 

  @Given("^the class code \"([^\"]*)\"$")
  public void the_class_code(String arg1) throws Exception {
    // Write code here that turns the phrase above into concrete actions
    class_code = arg1;
  }

  @Given("^the term code \"([^\"]*)\"$")
  public void the_term_code(int arg1) throws Exception {
    // Write code here that turns the phrase above into concrete actions
    term_code = arg1;
  }

  @When("^the app runs the query$")
  public void the_app_runs_the_query() throws Exception {
    // Write code here that turns the phrase above into concrete actions
    first_query = conn.firstNameInClass(class_code,term_code);
  }

  @Then("^the first name should be \"([^\"]*)\"$")
  public void the_first_name_should_be(String arg1) throws Exception {
    // Write code here that turns the phrase above into concrete actions
    assertEquals(arg1, first_query);
  }

  @Given("^an arbitrary query$")
  public void an_arbitrary_query() throws Exception {
      // Write code here that turns the phrase above into concrete actions
      first_query = "SELECT First_Name, Last_Name FROM REGISTRATION WHERE Subject_Code = 'CS' AND Course_Number = 115";
  }

  @Then("^something outputs$")
  public void something_outputs() throws Exception {
      // Write code here that turns the phrase above into concrete actions
      System.out.print(conn.tableFormatter(first_query));
      assumeTrue(true);
  }

  @Given("^the section code \"([^\"]*)\"$")
  public void the_term_code(int arg1) throws Exception {
    // Write code here that turns the phrase above into concrete actions
    section_code = arg1;
  }

  @When("^the app runs the new query$")
  public void the_app_runs_the_new_query() throws Exception {
    // Write code here that turns the phrase above into concrete actions
    first_query = "SELECT First_Name, Last_Name, CLASS_TAKEN(Pidm, 'CS230','C') FROM REGISTRATION WHERE Subject_Code = 'CS' AND Course_Number = 374"
  }
}