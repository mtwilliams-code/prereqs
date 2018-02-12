package com.blinkendorf.app;


import java.lang.String.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQL_Connector {

  String url = "jdbc:mysql://localhost:3306/records";
  String username = "java";
  String password = "Java";
  Connection conn = null;

  /**
  * Creates and opens a connection to a mySQL server located at the 
  * given URL with the given username and password.
  *
  * Defaults to database "records" on localhost with username java and password Java.
  *
  * @param  url the URL of the database, of the form "jdbc:mysql://host:port/databasename"
  * @param  username the username to connect to the mySQL server with
  * @param  password the password to connect to the mySQL server with
  * @return      the image at the specified URL
  */
  public SQL_Connector() throws Exception {
    System.out.println("Connecting database...");
    try {
      conn = DriverManager.getConnection(url, username, password);
      // Do something with the Connection

    } catch (SQLException ex) {
      // handle any errors
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
      throw new Exception(ex.getMessage());
    }
  }

  /**
  * Creates and opens a connection to a mySQL server located at the 
  * given URL with the given username and password.
  *
  * Defaults to database "records" on localhost with username java and password Java.
  *
  * @param  url the URL of the database, of the form "jdbc:mysql://host:port/databasename"
  * @param  username the username to connect to the mySQL server with
  * @param  password the password to connect to the mySQL server with
  * @return      the image at the specified URL
  */
  public SQL_Connector(String url, String username, String password) throws Exception {
    System.out.println("Connecting database...");
    this.url = url;
    this.username = username;
    this.password = password;
    try {
      conn = DriverManager.getConnection(url, username, password);
      // Do something with the Connection

    } catch (SQLException ex) {
      // handle any errors
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
      throw new Exception(ex.getMessage());
    }
  }

  public void closeConnection() throws Exception {
    try{
      conn.close();   
    }
    catch(Exception e)
    {
      throw e;
    }
  }

}