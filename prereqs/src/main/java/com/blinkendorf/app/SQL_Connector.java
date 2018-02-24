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
      query = "load data local infile '" + file.toPath() + "' into table REGISTRATION columns terminated by ',' "
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
      //I haven't the slightest idea what this does but I think its necessary
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(url, username, password);
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
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(url, username, password);
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
      query = "CREATE TABLE REGISTRATION ("
        +"Pidm INT NOT NULL,"
        +"Term_Code INT,"
        +"Part_of_Term_Code VARCHAR(36),"
        +"Part_of_Term_Desc VARCHAR(36),"
        +"Enrolled_Ind VARCHAR(36),"
        +"Registered_Ind VARCHAR(36),"
        +"Junk VARCHAR(2),"
        +"Student_Status_Code VARCHAR(36),"
        +"Student_Status_Desc VARCHAR(36),"
        +"Level_Code VARCHAR(36),"
        +"Level_Desc VARCHAR(36),"
        +"Student_Type_Code VARCHAR(36),"
        +"Student_Type_Desc VARCHAR(36),"
        +"Program_Code1 VARCHAR(36),"
        +"Program_Code2 VARCHAR(36),"
        +"Campus_Code VARCHAR(36),"
        +"Campus_Desc VARCHAR(36),"
        +"Department_Code VARCHAR(36),"
        +"Department_Desc VARCHAR(36),"
        +"Degree_Code1 VARCHAR(36),"
        +"Degree_Desc1 VARCHAR(36),"
        +"College_Code1 VARCHAR(36),"
        +"College_Desc1 VARCHAR(36),"
        +"Major_Code1 VARCHAR(36),"
        +"Major_Desc1 VARCHAR(36),"
        +"Major_Code1_2 VARCHAR(36),"
        +"Major_Desc1_2 VARCHAR(36),"
        +"Degree_Code2 VARCHAR(36),"
        +"Degree_Desc2 VARCHAR(36),"
        +"College_Code2 VARCHAR(36),"
        +"College_Desc2 VARCHAR(36),"
        +"Major_Code2 VARCHAR(36),"
        +"Major_Desc2 VARCHAR(36),"
        +"Class_Code VARCHAR(36),"
        +"Class_Desc VARCHAR(36),"
        +"CRN VARCHAR(36),"
        +"Reg_STS_Code VARCHAR(36),"
        +"Reg_STS_Desc VARCHAR(36),"
        +"Spec_Approval_Ind VARCHAR(36),"
        +"Reg_Error_Flag VARCHAR(36),"
        +"Subject_Code VARCHAR(36),"
        +"Subject_Desc VARCHAR(36),"
        +"Course_Number VARCHAR(36),"
        +"Section_Number VARCHAR(36),"
        +"Course_Title VARCHAR(36),"
        +"Course_Level_Code VARCHAR(36),"
        +"Course_Campus_Code VARCHAR(36),"
        +"Billing_Hours VARCHAR(36),"
        +"Credit_Hours VARCHAR(36),"
        +"Instructor_ID VARCHAR(36),"
        +"Instructor_Name VARCHAR(36),"
        +"Hours_Attended VARCHAR(36),"
        +"Grade_Mode_Code VARCHAR(36),"
        +"Grade_Mode_Desc VARCHAR(36),"
        +"Midterm_Grade_Code VARCHAR(36),"
        +"Grade_Code VARCHAR(36),"
        +"Banner_ID VARCHAR(36),"
        +"First_Name VARCHAR(36),"
        +"Last_Name VARCHAR(36),"
        +"Middle_Name VARCHAR(36),"
        +"Prefix VARCHAR(36),"
        +"Suffix VARCHAR(36),"
        +"Preferred_First_Name VARCHAR(36),"
        +"Confid_Ind VARCHAR(36),"
        +"ACU_Email_Address VARCHAR(36),"
        +"Home_Email_Address VARCHAR(36),"
        +"Begin_Time_1 VARCHAR(36),"
        +"End_Time1 VARCHAR(36),"
        +"Bldg_Code1 VARCHAR(36),"
        +"Bldg_Desc1 VARCHAR(36),"
        +"Room_Code1 VARCHAR(36),"
        +"Schd_Code1 VARCHAR(36),"
        +"Monday_Ind1 VARCHAR(36),"
        +"Tuesday_Ind1 VARCHAR(36),"
        +"Wednesday_Ind1 VARCHAR(36),"
        +"Thursday_Ind1 VARCHAR(36),"
        +"Friday_Ind1 VARCHAR(36),"
        +"Saturday_Ind1 VARCHAR(36),"
        +"Sunday_Ind1 VARCHAR(36),"
        +"Begin_Time2 VARCHAR(36),"
        +"End_Time2 VARCHAR(36),"
        +"Bldg_Code2 VARCHAR(36),"
        +"Bldg_Desc2 VARCHAR(36),"
        +"Room_Code2 VARCHAR(36),"
        +"Schd_Code2 VARCHAR(36),"
        +"Monday_Ind2 VARCHAR(36),"
        +"Tuesday_Ind2 VARCHAR(36),"
        +"Wednesday_Ind2 VARCHAR(36),"
        +"Thursday_Ind2 VARCHAR(36),"
        +"Friday_Ind2 VARCHAR(36),"
        +"Saturday_Ind2 VARCHAR(36),"
        +"Sunday_Ind2 VARCHAR(36),"
        +"Advisor1_Term_Code_Eff VARCHAR(36),"
        +"Advisor1_Last_Name VARCHAR(36),"
        +"Advisor1_First_Name VARCHAR(36),"
        +"Advisor1_Advisor_Code VARCHAR(36),"
        +"Advisor1_Primary_Advisor_Ind VARCHAR(36),"
        +"Sport1_Activity_Code VARCHAR(36),"
        +"Sport1_Code VARCHAR(36),"
        +"Sport1_Eligibilty_Code VARCHAR(36),"
        +"Sport1_Athletic_Aid_Ind VARCHAR(36),"
        +"Sport2_Activity__Code VARCHAR(36),"
        +"Sport2_Code VARCHAR(36),"
        +"Sport2_Eligibility_Code VARCHAR(36),"
        +"Sport2_Athletic_Aid_Ind VARCHAR(36),"
        +"Vet_Term VARCHAR(36),"
        +"Vet_Code VARCHAR(36),"
        +"Vet_Desc VARCHAR(36),"
        +"Vet_Certified_Hours VARCHAR(36),"
        +"Vet_Certified_Date VARCHAR(36),"
        +"Vet_Certified_Hours2 VARCHAR(36),"
        +"Minor_Code1 VARCHAR(36),"
        +"Minor_Desc1 VARCHAR(36),"
        +"Conc_Code1 VARCHAR(36),"
        +"Conc_Desc1 VARCHAR(36),"
        +"Minor_Code1_2 VARCHAR(36),"
        +"Minor_Desc1_2 VARCHAR(36),"
        +"Conc_Code1_2 VARCHAR(36),"
        +"Conc_Desc1_2 VARCHAR(36),"
        +"Minor_Code2 VARCHAR(36),"
        +"Minor_Desc2 VARCHAR(36),"
        +"Rate_Code VARCHAR(36),"
        +"Ovrall_Cumm_GPA_Hrs_Attempted VARCHAR(36),"
        +"Ovrall_Cumm_GPA__Hours_Earned VARCHAR(36),"
        +"Ovrall_Cumm_GPA_Hrs VARCHAR(36),"
        +"Ovrall_Cumm_GPA_Quality_Points VARCHAR(36),"
        +"Ovrall_Cumm_GPA VARCHAR(36),"
        +"Ovrall_Cumm_GPA_Hrs_Passed VARCHAR(36),"
        +"Dead_Ind VARCHAR(36),"
        +"Date_Class_Added VARCHAR(36),"
        +"Registration_Status_Date VARCHAR(36),"
        +"Activity_Date VARCHAR(36),"
        +"Course_College_Code VARCHAR(36),"
        +"Course_College_Desc VARCHAR(36),"
        +"Course_Dept_Code VARCHAR(36),"
        +"Course_Dept_Desc VARCHAR(36),"
        +"International_Ind VARCHAR(36),"
        +"Part_of_Term_Start_Date VARCHAR(36),"
        +"Part_of_Term_End_Date VARCHAR(36),"
        +"Section_Max_Enrollment VARCHAR(36),"
        +"Section_Enrollment VARCHAR(36),"
        +"Section_Available_Seats VARCHAR(36),"
        +"Section_Schedule_Type VARCHAR(36),"
        +"Section_Instruction_Method VARCHAR(36),"
        +"Section_Session_Code VARCHAR(36),"
        +"Ipeds_Ethnic_Code VARCHAR(36),"
        +"Ipeds_Ethnic_Desc VARCHAR(36),"
        +"PRIMARY KEY (Pidm),"
        +"INDEX index2 (Subject_Code ASC , Course_Number ASC))";
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

  public String firstNameInClass(String class_code, int term_code) throws SQLException
  {
    ResultSet rslt = null;
    Statement stmt = null;
    try
    {
      stmt = conn.createStatement();
      String query = "SELECT First_Name, Last_Name FROM REGISTRATION WHERE CONCAT(Subject_Code, Course_Number) = '"+class_code+"' AND Term_Code = "+term_code;
      stmt.executeQuery(query);
      rslt = stmt.getResultSet();
      rslt.next();
      return rslt.getString(1) + " " + rslt.getString(2);
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
