package com.blinkendorf.app;

import java.lang.String.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;

public class SQL_Connector {

  String url = "jdbc:mysql://localhost:3306";
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  String username = "java";
  String password = "Java";
  Connection conn = null;

  /** 
   * returns java.sql.Connection object
   * 
   * 
   */
  public Connection getConnection() {
    return conn;
  }

  public ResultSet loadCSV(File file) throws SQLException{
    Statement stmt = null;
    stmt = conn.createStatement();
    String query;
    ResultSet rslt = null;
    try {
      query = "load data local infile '" + file.toPath() + "' into table registration columns terminated by ',' "
      + "enclosed by '\"' escaped by '\"' "
      + "lines terminated by '\n' "
      + "ignore 1 lines";
      stmt.executeUpdate(query);
      rslt = stmt.getResultSet();
    } catch (SQLException e) {
      System.out.println("SQLException: " + e.getMessage());
      System.out.println("SQLState: " + e.getSQLState());
      System.out.println("VendorError: " + e.getErrorCode());
      throw e;
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }   
    return rslt;
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
  public SQL_Connector() throws Exception {
    System.out.println("Connecting database...");
    try {
      conn = DriverManager.getConnection(url, username, password);
      //I haven't the slightest idea what this does but I think its necessary
      Class.forName("com.mysql.jdbc.Driver");
      // Do something with the Connection
      System.out.println("Database connected!");

    } catch (SQLException ex) {
      // handle any errors
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
      throw new Exception(ex.getMessage());
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
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
      Class.forName("com.mysql.jdbc.Driver");
      // Do something with the Connection
      System.out.println("Database connected!");

    } catch (SQLException ex) {
      // handle any errors
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
      throw new Exception(ex.getMessage());
    }
  }

  /** 
  * Closes socket connection.
  */
  public void closeConnection() throws Exception {
    try {
      conn.close();
    } catch (Exception e) {
      throw e;
    }
  }

  /**
  * Checks if connection is still valid by running a ping query. Should only work if server is responding.
  * Has a two second time-out
  * 
  * @return   true if connected, false otherwise
  */
  public boolean isConnected() throws Exception {
    try {
      return conn.isValid(2000);
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * Creates Database. Assumes it does not already exist or it will throw an error
   * 
   * @param dbname String for the name of the database to be created
   */
  public void createDatabase(String dbname) throws SQLException {
    Statement stmt = null;
    stmt = conn.createStatement();
    String query;
    try {
      // if (dbname database exists) {
      //   query = "DROP DATABASE " + dbname;
      //   stmt.executeUpdate(query);
      // }
      query = "CREATE DATABASE IF NOT EXISTS " + dbname;
      stmt.executeUpdate(query);
    } catch (SQLException e) {
      System.out.println("SQLException: " + e.getMessage());
      System.out.println("SQLState: " + e.getSQLState());
      System.out.println("VendorError: " + e.getErrorCode());
      throw e;
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  /** 
   * Returns ResultSet containing, among other things, all of the table names
   * 
   * @return ResultSet
   */
  public ResultSet getAllTableNames() throws SQLException {
    ResultSet rs = null;
    try {
      DatabaseMetaData md = conn.getMetaData();
      rs = md.getTables(null, null, "%", null);
    } catch (SQLException e) {
      System.out.println("SQLException: " + e.getMessage());
      System.out.println("SQLState: " + e.getSQLState());
      System.out.println("VendorError: " + e.getErrorCode());
      throw e;
    }
    return rs;
  }

  /**
   * Creates "registration" table on "records" database with space for all data in CSV
   * 
   * 
   */
  public void createRegistrationTable() throws SQLException {
    Statement stmt = null;
    try {
      // this is awful. jdbc does not support executing SQL scripts from files so this is the only way I can think to do it with jdbc
      // alternatively we could write code to parse the sql file line by line and execute, but I didn't want to do that.
      // if someone wants to take charge of that, it would def be a better long term strategy
      stmt = conn.createStatement();
      String query = "DROP TABLE IF EXISTS REGISTRATION";
      stmt.executeUpdate(query);
      query = "CREATE TABLE IF NOT EXISTS REGISTRATION (Pidm INT)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Term_Code INT";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Part_of_Term_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Part_of_Term_Desc VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Enrolled_Ind VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Registered_Ind VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Junk VARCHAR(2)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Student_Status_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Student_Status_Desc VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Level_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Level_Desc VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Student_Type_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Student_Type_Desc VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Program_Code1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Program_Code2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Campus_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Campus_Desc VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Department_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Department_Desc VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Degree_Code1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Degree_Desc1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN College_Code1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN College_Desc1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Major_Code1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Major_Desc1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Major_Code1_2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Major_Desc1_2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Degree_Code2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Degree_Desc2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN College_Code2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN College_Desc2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Major_Code2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Major_Desc2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Class_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Class_Desc VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN CRN VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Reg_STS_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Reg_STS_Desc VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Spec_Approval_Ind VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Reg_Error_Flag VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Subject_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Subject_Desc VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Course_Number VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Section_Number VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Course_Title VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Course_Level_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Course_Campus_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Billing_Hours VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Credit_Hours VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Instructor_ID VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Instructor_Name VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Hours_Attended VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Grade_Mode_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Grade_Mode_Desc VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Midterm_Grade_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Grade_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Banner_ID VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN First_Name VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Last_Name VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Middle_Name VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Prefix VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Suffix VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Preferred_First_Name VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Confid_Ind VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN ACU_Email_Address VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Home_Email_Address VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Begin_Time_1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN End_Time1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Bldg_Code1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Bldg_Desc1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Room_Code1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Schd_Code1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Monday_Ind1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Tuesday_Ind1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Wednesday_Ind1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Thursday_Ind1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Friday_Ind1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Saturday_Ind1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Sunday_Ind1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Begin_Time2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN End_Time2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Bldg_Code2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Bldg_Desc2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Room_Code2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Schd_Code2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Monday_Ind2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Tuesday_Ind2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Wednesday_Ind2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Thursday_Ind2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Friday_Ind2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Saturday_Ind2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Sunday_Ind2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Advisor1_Term_Code_Eff VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Advisor1_Last_Name VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Advisor1_First_Name VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Advisor1_Advisor_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Advisor1_Primary_Advisor_Ind VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Sport1_Activity_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Sport1_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Sport1_Eligibilty_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Sport1_Athletic_Aid_Ind VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Sport2_Activity__Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Sport2_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Sport2_Eligibility_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Sport2_Athletic_Aid_Ind VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Vet_Term VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Vet_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Vet_Desc VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Vet_Certified_Hours VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Vet_Certified_Date VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Vet_Certified_Hours2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Minor_Code1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Minor_Desc1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Conc_Code1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Conc_Desc1 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Minor_Code1_2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Minor_Desc1_2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Conc_Code1_2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Conc_Desc1_2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Minor_Code2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Minor_Desc2 VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Rate_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Ovrall_Cumm_GPA_Hrs_Attempted VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Ovrall_Cumm_GPA__Hours_Earned VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Ovrall_Cumm_GPA_Hrs VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Ovrall_Cumm_GPA_Quality_Points VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Ovrall_Cumm_GPA VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Ovrall_Cumm_GPA_Hrs_Passed VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Dead_Ind VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Date_Class_Added VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Registration_Status_Date VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Activity_Date VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Course_College_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Course_College_Desc VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Course_Dept_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Course_Dept_Desc VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN International_Ind VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Part_of_Term_Start_Date VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Part_of_Term_End_Date VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Section_Max_Enrollment VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Section_Enrollment VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Section_Available_Seats VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Section_Schedule_Type VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Section_Instruction_Method VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Section_Session_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Ipeds_Ethnic_Code VARCHAR(36)";
      stmt.executeUpdate(query);
      query = "ALTER TABLE REGISTRATION ADD COLUMN Ipeds_Ethnic_Desc VARCHAR(36)";
      stmt.executeUpdate(query);
    } catch (SQLException e) {
      System.out.println("SQLException: " + e.getMessage());
      System.out.println("SQLState: " + e.getSQLState());
      System.out.println("VendorError: " + e.getErrorCode());
      throw e;
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  public ResultSet studentsInClass(String class_code) throws SQLException
  {
    ResultSet rslt = null;
    Statement stmt = null;
    try
    {
      
      stmt = conn.createStatement();
      String query = "SELECT First_Name, Last_Name FROM REGISTRATION WHERE CONCAT(Subject_Code, Course_Number) = '"+class_code+"' AND Term_Code = 201710";
      stmt.executeUpdate(query);
      rslt = stmt.getResultSet();
      return rslt;
    }
    catch (SQLException e) {
      System.out.println("SQLException: " + e.getMessage());
      System.out.println("SQLState: " + e.getSQLState());
      System.out.println("VendorError: " + e.getErrorCode());
      throw e;
    }
    finally {
      if (stmt != null) {
        stmt.close();
      }
    }
  }

}
