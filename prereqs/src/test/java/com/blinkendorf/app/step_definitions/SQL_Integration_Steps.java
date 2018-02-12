package com.blinkendorf.app.step_definitions;


import cucumber.api.java.en.*;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.PendingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.blinkendorf.app.SQL_Connector;
import java.nio.file.Files;
import java.nio.file.*;
import java.net.*;

public class SQL_Integration_Steps {

  @Given("^a mySQL server is running on the local machine$")
  public void a_mySQL_server_is_running_on_the_local_machine() throws Exception {
    // Write code here that turns the phrase above into concrete actions
    // checks whethere ip address is reachable - does not check if server is running
    InetAddress addr = InetAddress.getByName("localhost");
    // addr.isReachable(2000); //tries for two seconds to connect to localhost. Should be plenty of time.
    int port = 3306;
    SocketAddress sockaddr = new InetSocketAddress(addr, port);
    Socket sock = new Socket();
    sock.connect(sockaddr, 2000); // open the connection with a 2 seconds timeout
  }

  @Given("^the app is not already connected to a server$")
  public void the_app_is_not_already_connected_to_a_server() throws Exception {
    // Write code here that turns the phrase above into concrete actions
    throw new PendingException();
  }

  @When("^the app tries to connect to the local server$")
  public void the_app_tries_to_connect_to_the_local_server() throws Exception {
    // Write code here that turns the phrase above into concrete actions
    throw new PendingException();
  }

  @Then("^the connection should be valid$")
  public void the_connection_should_be_valid() throws Exception {
    // Write code here that turns the phrase above into concrete actions
    throw new PendingException();
  }
}