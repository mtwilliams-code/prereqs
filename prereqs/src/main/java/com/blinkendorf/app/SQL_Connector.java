package com.blinkendorf.app;


import java.lang.String.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQL_Connector {

  String url = "jdbc:mysql://localhost:3306/registration";
  String username = "java";
  String password = "Java";

  void main() {
    System.out.println("Connecting database...");
    Connection conn = null;
    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost/registration" + "user=java&password=Java");

      // Do something with the Connection

    } catch (SQLException ex) {
      // handle any errors
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
    }
  }

}