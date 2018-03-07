package com.blinkendorf.app;

import java.lang.String.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.sql.DatabaseMetaData;

/**
 * Class to wrap jdbc mysql connector and handle all queries
 * 
 */
public class SQL_Connector {
  /**
   * URL for the location of the database, along with the protocol to connect through.
   */
  String url = "jdbc:mysql://localhost:3306";
  /** String that defines the jdbc driver. */
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  /** Username to connect to database with. */
  String username = "java";
  /** Password to connect to database with. */
  String password = "Java";
  /** Connection object to interact with SQL server. */
  Connection conn = null;

  /** 
   * returns java.sql.Connection object
   * 
   * 
   */
  public Connection getConnection() {
    return conn;
  }

  /**
   * Loads .csv file into the table specified.
   * 
   * @param file a File object specifying the csv to be read
   * @param table a String title of the table to be loaded into
   */
  public ResultSet loadCSV(File file, String table) throws SQLException {
    Statement stmt = null;
    stmt = conn.createStatement();
    String query;
    ResultSet rslt = null;
    try {
      query = "set unique_checks = 0;";
      stmt.executeUpdate(query);
      query = "set foreign_key_checks = 0;";
      stmt.executeUpdate(query);
      query = "set sql_log_bin=0;";
      stmt.executeUpdate(query);
      query = "set autocommit = 0;";
      stmt.executeUpdate(query);
      query = "load data local infile '" + file.toPath() + "' into table " + table + " columns terminated by ',' "
          + "enclosed by '\"' escaped by '\"' " + "lines terminated by '\n' " + "ignore 1 lines";
      stmt.executeUpdate(query);
      query = "commit";
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
  * given URL with the default username and password.
  *
  * Defaults to database "records" on localhost with username java and password Java.
  */
  public SQL_Connector() throws Exception {
    // System.out.println("Connecting database...");
    try {
      //I haven't the slightest idea what this does but I think its necessary
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(url, username, password);
      // Do something with the Connection
      // System.out.println("Database connected!");

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
  */
  public SQL_Connector(String url, String username, String password) throws Exception {
    // System.out.println("Connecting database...");
    this.url = url;
    this.username = username;
    this.password = password;
    try {
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(url, username, password);
      // Do something with the Connection
      // System.out.println("Database connected!");

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
      query = "CREATE TABLE REGISTRATION (" + "Pidm INT NOT NULL," + "Term_Code INT," + "Part_of_Term_Code VARCHAR(36),"
          + "Part_of_Term_Desc VARCHAR(36)," + "Enrolled_Ind VARCHAR(36)," + "Registered_Ind VARCHAR(36),"
          + "Junk VARCHAR(2)," + "Student_Status_Code VARCHAR(36)," + "Student_Status_Desc VARCHAR(36),"
          + "Level_Code VARCHAR(36)," + "Level_Desc VARCHAR(36)," + "Student_Type_Code VARCHAR(36),"
          + "Student_Type_Desc VARCHAR(36)," + "Program_Code1 VARCHAR(36)," + "Program_Code2 VARCHAR(36),"
          + "Campus_Code VARCHAR(36)," + "Campus_Desc VARCHAR(36)," + "Department_Code VARCHAR(36),"
          + "Department_Desc VARCHAR(36)," + "Degree_Code1 VARCHAR(36)," + "Degree_Desc1 VARCHAR(36),"
          + "College_Code1 VARCHAR(36)," + "College_Desc1 VARCHAR(36)," + "Major_Code1 VARCHAR(36),"
          + "Major_Desc1 VARCHAR(36)," + "Major_Code1_2 VARCHAR(36)," + "Major_Desc1_2 VARCHAR(36),"
          + "Degree_Code2 VARCHAR(36)," + "Degree_Desc2 VARCHAR(36)," + "College_Code2 VARCHAR(36),"
          + "College_Desc2 VARCHAR(36)," + "Major_Code2 VARCHAR(36)," + "Major_Desc2 VARCHAR(36),"
          + "Class_Code VARCHAR(36)," + "Class_Desc VARCHAR(36)," + "CRN VARCHAR(36)," + "Reg_STS_Code VARCHAR(36),"
          + "Reg_STS_Desc VARCHAR(36)," + "Spec_Approval_Ind VARCHAR(36)," + "Reg_Error_Flag VARCHAR(36),"
          + "Subject_Code VARCHAR(36)," + "Subject_Desc VARCHAR(36)," + "Course_Number VARCHAR(36),"
          + "Section_Number VARCHAR(36)," + "Course_Title VARCHAR(36)," + "Course_Level_Code VARCHAR(36),"
          + "Course_Campus_Code VARCHAR(36)," + "Billing_Hours VARCHAR(36)," + "Credit_Hours VARCHAR(36),"
          + "Instructor_ID VARCHAR(36)," + "Instructor_Name VARCHAR(36)," + "Hours_Attended VARCHAR(36),"
          + "Grade_Mode_Code VARCHAR(36)," + "Grade_Mode_Desc VARCHAR(36)," + "Midterm_Grade_Code VARCHAR(36),"
          + "Grade_Code VARCHAR(36)," + "Banner_ID VARCHAR(36)," + "First_Name VARCHAR(36)," + "Last_Name VARCHAR(36),"
          + "Middle_Name VARCHAR(36)," + "Prefix VARCHAR(36)," + "Suffix VARCHAR(36),"
          + "Preferred_First_Name VARCHAR(36)," + "Confid_Ind VARCHAR(36)," + "ACU_Email_Address VARCHAR(36),"
          + "Home_Email_Address VARCHAR(36)," + "Begin_Time_1 VARCHAR(36)," + "End_Time1 VARCHAR(36),"
          + "Bldg_Code1 VARCHAR(36)," + "Bldg_Desc1 VARCHAR(36)," + "Room_Code1 VARCHAR(36),"
          + "Schd_Code1 VARCHAR(36)," + "Monday_Ind1 VARCHAR(36)," + "Tuesday_Ind1 VARCHAR(36),"
          + "Wednesday_Ind1 VARCHAR(36)," + "Thursday_Ind1 VARCHAR(36)," + "Friday_Ind1 VARCHAR(36),"
          + "Saturday_Ind1 VARCHAR(36)," + "Sunday_Ind1 VARCHAR(36)," + "Begin_Time2 VARCHAR(36),"
          + "End_Time2 VARCHAR(36)," + "Bldg_Code2 VARCHAR(36)," + "Bldg_Desc2 VARCHAR(36)," + "Room_Code2 VARCHAR(36),"
          + "Schd_Code2 VARCHAR(36)," + "Monday_Ind2 VARCHAR(36)," + "Tuesday_Ind2 VARCHAR(36),"
          + "Wednesday_Ind2 VARCHAR(36)," + "Thursday_Ind2 VARCHAR(36)," + "Friday_Ind2 VARCHAR(36),"
          + "Saturday_Ind2 VARCHAR(36)," + "Sunday_Ind2 VARCHAR(36)," + "Advisor1_Term_Code_Eff VARCHAR(36),"
          + "Advisor1_Last_Name VARCHAR(36)," + "Advisor1_First_Name VARCHAR(36),"
          + "Advisor1_Advisor_Code VARCHAR(36)," + "Advisor1_Primary_Advisor_Ind VARCHAR(36),"
          + "Sport1_Activity_Code VARCHAR(36)," + "Sport1_Code VARCHAR(36)," + "Sport1_Eligibilty_Code VARCHAR(36),"
          + "Sport1_Athletic_Aid_Ind VARCHAR(36)," + "Sport2_Activity__Code VARCHAR(36)," + "Sport2_Code VARCHAR(36),"
          + "Sport2_Eligibility_Code VARCHAR(36)," + "Sport2_Athletic_Aid_Ind VARCHAR(36)," + "Vet_Term VARCHAR(36),"
          + "Vet_Code VARCHAR(36)," + "Vet_Desc VARCHAR(36)," + "Vet_Certified_Hours VARCHAR(36),"
          + "Vet_Certified_Date VARCHAR(36)," + "Vet_Certified_Hours2 VARCHAR(36)," + "Minor_Code1 VARCHAR(36),"
          + "Minor_Desc1 VARCHAR(36)," + "Conc_Code1 VARCHAR(36)," + "Conc_Desc1 VARCHAR(36),"
          + "Minor_Code1_2 VARCHAR(36)," + "Minor_Desc1_2 VARCHAR(36)," + "Conc_Code1_2 VARCHAR(36),"
          + "Conc_Desc1_2 VARCHAR(36)," + "Minor_Code2 VARCHAR(36)," + "Minor_Desc2 VARCHAR(36),"
          + "Rate_Code VARCHAR(36)," + "Ovrall_Cumm_GPA_Hrs_Attempted VARCHAR(36),"
          + "Ovrall_Cumm_GPA__Hours_Earned VARCHAR(36)," + "Ovrall_Cumm_GPA_Hrs VARCHAR(36),"
          + "Ovrall_Cumm_GPA_Quality_Points VARCHAR(36)," + "Ovrall_Cumm_GPA VARCHAR(36),"
          + "Ovrall_Cumm_GPA_Hrs_Passed VARCHAR(36)," + "Dead_Ind VARCHAR(36)," + "Date_Class_Added VARCHAR(36),"
          + "Registration_Status_Date VARCHAR(36)," + "Activity_Date VARCHAR(36)," + "Course_College_Code VARCHAR(36),"
          + "Course_College_Desc VARCHAR(36)," + "Course_Dept_Code VARCHAR(36)," + "Course_Dept_Desc VARCHAR(36),"
          + "International_Ind VARCHAR(36)," + "Part_of_Term_Start_Date VARCHAR(36),"
          + "Part_of_Term_End_Date VARCHAR(36)," + "Section_Max_Enrollment VARCHAR(36),"
          + "Section_Enrollment VARCHAR(36)," + "Section_Available_Seats VARCHAR(36),"
          + "Section_Schedule_Type VARCHAR(36)," + "Section_Instruction_Method VARCHAR(36),"
          + "Section_Session_Code VARCHAR(36)," + "Ipeds_Ethnic_Code VARCHAR(36)," + "Ipeds_Ethnic_Desc VARCHAR(36),"
          + "INDEX index2 (Subject_Code ASC , Course_Number ASC))";
      stmt.executeUpdate(query);
      query = "DROP function IF EXISTS `CLASS_TAKEN`";
      stmt.executeUpdate(query);
      query = "CREATE DEFINER=`java`@`localhost` FUNCTION `CLASS_TAKEN`(PIDMIN INT, GRADE VARCHAR(15), CLASS_CODE VARCHAR(10))\n"
          + "RETURNS char(1) CHARSET latin1\n" + "BEGIN\n" + "DECLARE done INT DEFAULT FALSE;\n"
          + "DECLARE CCode VARCHAR(10) default '';\n" + "DECLARE FGrade VARCHAR(2);\n"
          + "DECLARE OUTPUT CHAR DEFAULT 'N';\n"
          + "DECLARE CLASSES CURSOR FOR (SELECT CONCAT(Subject_Code,Course_Number), Grade_Code FROM REGISTRATION WHERE PIDM = PIDMIN);\n"
          + "DECLARE CONTINUE handler for NOT FOUND SET done = true;\n" + "OPEN CLASSES;\n" + "start_loop: loop\n"
          + "fetch CLASSES into CCode, FGrade;\n"
          + "if CCode = CLASS_CODE and (FGrade BETWEEN 'A' AND GRADE OR FGrade IN ('', 'P')) then  set OUTPUT='Y';\n"
          + "leave start_loop;\n" + "end if;\n" + "if done then leave start_loop;\n" + "end if;\n" + "end loop;\n"
          + "CLOSE CLASSES;\n" + "RETURN OUTPUT;\n" + "END";
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
   * This is just a small function to make a pre-written query which returns all of the people in a certain class
   * (If possible we should rework this to not append the information together but instead passs each individually).
   * 
   * @param class_code The code of the class, consisting of subject code + subject number + section number all appended together.
   * @param term_code The term code for the class you are interested in.
   * @return A Data object containing the information from the query.
   */
  public Data namesInClass(String class_code, int term_code) throws SQLException {
    ResultSet rslt = null;
    Statement stmt = null;
    Data names = new Data();
    ResultSetMetaData rsmd = null;
    int numColumns = 0;
    try {
      stmt = conn.createStatement();
      String query = "SELECT First_Name AS 'First', Last_Name AS 'Last' FROM REGISTRATION WHERE CONCAT(Subject_Code, Course_Number, Section_Number) = '"
          + class_code + "' AND Term_Code = " + term_code;
      stmt.executeQuery(query);
      rslt = stmt.getResultSet();
      rsmd = rslt.getMetaData();
      numColumns = rsmd.getColumnCount();
      for (int i = 1; i <= numColumns; i++) {
        names.appendColumn(rsmd.getColumnLabel(i));
      }

      while (rslt.next()) {
        ArrayList<String> a = new ArrayList<String>();
        a.add(rslt.getString(1));
        a.add(rslt.getString(2));
        names.add(a);
        // names.add(rslt.getString(1) + " " + rslt.getString(2));
      }
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
    return names;
  }

  /**
   * Deprecated
   */
  public String printQuery(String query) throws SQLException {
    ResultSet rslt = null;
    ResultSetMetaData rsmd = null;
    Statement stmt = null;
    int numColumns;
    String formattedTable = "";
    try {
      stmt = conn.createStatement();
      stmt.executeQuery(query);
      rslt = stmt.getResultSet();
      rsmd = rslt.getMetaData();
      numColumns = rsmd.getColumnCount();
      formattedTable += String.format("|%12.12s|", rsmd.getColumnName(1));
      for (int i = 2; i <= numColumns; i++) {
        formattedTable += String.format("|%12.12s|", rsmd.getColumnName(i));
      }
      formattedTable += "\n";
      while (rslt.next()) {
        formattedTable += String.format("|%12.12s|", rslt.getString(1));
        for (int i = 2; i <= numColumns; i++) {
          formattedTable += String.format("|%12.12s|", rslt.getString(i));
        }
        formattedTable += "\n";
      }
      return formattedTable;
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
   * Takes class details and queries database for all the prereqs of the class.
   * 
   * @param subjectCode The subject code for the class, like "CS" or "MATH"
   * @param subjectNum The class number, like "231" or "410"
   * 
   * @return A Data object containing the prereqs
   */
  public Data getPrereqs(String subjectCode, String subjectNum) throws SQLException {
    ResultSet rslt = null;
    Statement stmt = null;
    Data prereqs = new Data();
    ResultSetMetaData rsmd = null;
    int numColumns = 0;
    try {
      stmt = conn.createStatement();
      String query = "SELECT Class_Code AS 'Class', Prereq_Code AS 'Prereq' from PREREQS where Class_Code = '" + subjectCode + subjectNum
          + "'";
      stmt.executeQuery(query);
      rslt = stmt.getResultSet();
      rsmd = rslt.getMetaData();
      numColumns = rsmd.getColumnCount();
      for (int i = 1; i <= numColumns; i++) {
        prereqs.appendColumn(rsmd.getColumnLabel(i));
      }

      while (rslt.next()) {
        ArrayList<String> a = new ArrayList<String>();
        a.add(rslt.getString(1).replaceAll("[^a-zA-Z0-9]+", ""));
        a.add(rslt.getString(2).replaceAll("[^a-zA-Z0-9]+", ""));
        prereqs.add(a);
        // names.add(rslt.getString(1) + " " + rslt.getString(2));
      }
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
    return prereqs;
  }

  /**
   * Creates pre-formatted prereq table for scrapedPrereqs.csv
   */
  public void createPrereqTable() throws SQLException {
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String query = "DROP TABLE IF EXISTS PREREQS";
      stmt.executeUpdate(query);
      query = "CREATE TABLE PREREQS ( Class_Code VARCHAR(7) NOT NULL, " + "Prereq_Code VARCHAR(45) NOT NULL, "
          + "PRIMARY KEY(Class_Code,Prereq_Code))";
      stmt.executeUpdate(query);
    } catch (SQLException e) 
    {
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
   * Builds a query to find all the students who have taken the given class. This query references the CLASS_TAKEN function in the database. 
   * 
   * @param prereq_list A Data object containing the prereqs for the class
   * @param subject_code Subject code for the class, like "CS"
   * @param course_number Course number for the class, like "310"
   * @param section_num Section number for the class, used to find students currently in class. Like "201410"
   * @return A string representing the query to be run
   */
  private String classTakenQuery(Data prereq_list, String subject_code, String course_number, String section_num, int term_code) 
  {
    String class_code = subject_code + course_number + section_num;
    String ttr = "SELECT First_Name, Last_Name";
    for (int i = 0; i < prereq_list.getRowCount(); i++)
    {
      ttr += ", ";
      String prereqCode = prereq_list.getColumn(i).get(0);
      String[] prereqCodes = prereqCode.split("or");
      for (int j = 0; j < prereqCodes.length; i++) 
      {
        if (j != 0) ttr += " OR ";
        ttr += "CLASS_TAKEN(PIDM, 'C', '"+prereqCodes[j]+"') AS '"+prereqCodes[j]+"'";
      }
    }
    ttr += " FROM REGISTRATION WHERE CONCAT(Subject_Code, Course_Number, Section_Number) = '" + class_code
        + "' AND Term_Code = " + term_code;
    return ttr;
  }

    /**
   * This calls function to get prerequisites. It then checks this against the CLASS_TAKEN function in the database. It reorganizes this data into a list of students with the classes they are missing and returns that.
   *
   * @param subjectCode The subject code for the class, like "CS" or "MATH"
   * @param subjectNum The class number, like "231" or "410"
   * @param sectionCode The section number, like ".01" or ".H1"
   * @param termCode The term code, like "201410" or "201720"
   *
   * @return A Data object that lists ineligible students and the classes they are missing to be eligible.
   */
  public Data PrereqCheck(String subjectCode, String subjectNum, String sectionCode, int termCode) throws SQLException {
    ResultSet rslt = null;
    Statement stmt = null;
    Data list = new Data();
    Data newList = new Data();

    ResultSetMetaData rsmd = null;
    int numColumns = 0;

    // call get prereqs
    // this returns a data object that is a list of
    Data pre = getPrereqs(subjectCode, subjectNum);

    // query the database with John's string
    // he's writing a query and I need to write the stuff that passes to the db and runs it
    // ClassTakenQuery will give me a string to run
    String query = classTakenQuery(pre, subjectCode, subjectNum, sectionCode, termCode);

    try {
      stmt = conn.createStatement();
      stmt.executeQuery(query);
      rslt = stmt.getResultSet();
      rsmd = rslt.getMetaData();
      numColumns = rsmd.getColumnCount();
      for (int i = 1; i <= numColumns; i++) {
        list.appendColumn(rsmd.getColumnLabel(i));
      }
      while (rslt.next()) {
        ArrayList<String> a = new ArrayList<String>();

        for (int i = 1; i <= numColumns; i++) {
          a.add(rslt.getString(i));
        }
        list.add(a);
      }
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

    // Store all the returns in a Data object
    // limit it to show firstName, lastName, list of classes where N.

    // this pulls the first column from the data object, it is just student names
    ArrayList<String> listStudentNames = list.getColumn(0);
    // this pulls the first row, it is just the class names
    ArrayList<String> listColumnNames = list.getRow(0);

    int rows = list.getRowCount();

    // here I am going to loop through the height of the data object
    for (int i = 1; i <= rows; i++) {
      // the plan here is only add students to the new data object if they have an "N"
      ArrayList<String> student = list.getRow(i);
      ArrayList<String> newStudent = new ArrayList<String>();
      newStudent.add(student.get(0));
      for (int j = 1; j <= numColumns; j++) {
        if (student.get(j).equalsIgnoreCase("N")) {
          newStudent.add(listColumnNames.get(j));
        }
      }

      if (newStudent.size() > 1) {
        newList.add(newStudent);
      }

    }
    if (newList.isEmpty()) {
      ArrayList<String> errorThing = new ArrayList<String>();
      errorThing.add("All students met the required prerequisites for this course. ");
      newList.add(errorThing);
    }
    // return the data object
    return newList;
  }

  /**
   * Executes any arbitrary query and returns the results as a Data object
   * 
   * @param query A String representing the query to be run
   * @return A Data object containing the results
   */
  public Data runQuery(String query) throws SQLException {
    ResultSet rslt = null;
    Statement stmt = null;
    Data result = new Data();
    ResultSetMetaData rsmd = null;
    int numColumns = 0;

    try {
      stmt = conn.createStatement();
      stmt.executeQuery(query);
      rslt = stmt.getResultSet();
      rsmd = rslt.getMetaData();
      numColumns = rsmd.getColumnCount();
      for (int i = 1; i <= numColumns; i++) {
        result.appendColumn(rsmd.getColumnLabel(i));
      }
      while (rslt.next()) {
        ArrayList<String> a = new ArrayList<String>();
        for (int i = 1; i <= numColumns; i++) {
          a.add(rslt.getString(i));
        }
        result.add(a);
      }
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

    // Store all the returns in a Data object
    // limit it to show firstName, lastName, list of classes where N.

    // return the data object
    return result;
  }

}
