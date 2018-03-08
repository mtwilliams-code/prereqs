package com.blinkendorf.app;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.blinkendorf.app.SQL_Connector;


/**
 * Main class of the program, accepts inputs from user in loop and calls all necessary class methods
 *
 */
public class App 
{
  static String localurl = "jdbc:mysql://localhost:3306/";
  static String AWSurl = "jdbc:mysql://rds-mysql-prereqs.cpmwdkch5csn.us-east-2.rds.amazonaws.com/";
  /** String that defines the jdbc driver. */
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  /** Username to connect to database with. */
  static String localusername = "java";
  static String AWSusername = "blinkendorf";
  /** Password to connect to database with. */
  static String localpassword = "Java";
  static String AWSpassword = "brentreeves";

    private static Scanner scanner = new Scanner( System.in );
    private static Data result;
    private static final int TERM_CODE = 201810;
    public static void main( String[] args )
    {
        String input;
        SQL_Connector conn = null;
        System.out.println("Do you want to connect to a local database or AWS?");
        System.out.println("Please enter \"local\" or \"aws\": ");
        input = scanner.nextLine().toLowerCase();
        if (input.equalsIgnoreCase("local"))
        {
            System.out.println("Give us a moment to ensure we can connect to the local database...");
            try {
                String url = localurl + "RECORDS";
                String username = localusername;
                String password = localpassword;
                conn = new SQL_Connector(url, username, password);
            } catch (Exception ex) {
                System.out.println("Uh oh! Something went wrong and we could not connect to the local database.");
                System.out.println(
                        "Please ensure you are hosting a mySQL server listening to port 3306 with user:java and pass: Java.");
            } finally {
                // end program
            }
        }

        if (input.equalsIgnoreCase("aws"))
        {
            System.out.println("Give us a moment to ensure we can connect to the remote AWS database...");
            try {
                String url = AWSurl + "RECORDS";
                String username = AWSusername;
                String password = AWSpassword;
                conn = new SQL_Connector(url, username, password);
            } catch (Exception ex) {
                System.out.println("Uh oh! Something went wrong and we could not connect to the remote AWS database.");
                System.out.println(
                        "Please ensure you have an AWS server listening to port 3306 with user:blinkendorf and pass:brentreeves.");
            }
        }

        System.out.println("Hello. What would you like to do? You may enter 'help' for options: ");
        Pattern classMembersRegex = Pattern.compile("who is in (\\w{2,4})\\s?(\\d{3})\\.(\\w{2,3})");
        Pattern notQualifiedRegex = Pattern.compile("who is not qualified for (\\w{2,4})\\s?(\\d{3})\\.(\\w{2,3})");
        Pattern prereqsForRegex = Pattern.compile("what are the prereqs for (\\w{2,4})\\s?(\\d{3})");
        Pattern sectionsOfRegex = Pattern.compile("what sections are there for (\\w{2,4})\\s?(\\d{3})");
        System.out.print("\nEnter Query: ");
        while (!(input = scanner.nextLine().toLowerCase()).equalsIgnoreCase("end")) {
            Matcher m1 = classMembersRegex.matcher(input); 
            Matcher m2 = notQualifiedRegex.matcher(input); 
            Matcher m3 = prereqsForRegex.matcher(input);
            Matcher m4 = sectionsOfRegex.matcher(input);
            if (input.equalsIgnoreCase("help")) {
                System.out.println("You may enter the following commands (caps insensitive):");
                System.out.println("end                                                              :to exit the program");
                System.out.println("who is in <subject> <classnumber>.<sectionnumber>                :where <subject> is like 'CS' and <classnumber> is like '274' and  <sectionnumber> is like '01'.");
                System.out.println("who is not qualified for <subject> <classnumber>.<sectionnumber> :where <subject> is like 'CS' and <classnumber> is like '274' and  <sectionnumber> is like '01'.");
                System.out.println("what are the prereqs for <subject> <classnumber>                 :where <subject> is like 'CS' and <classnumber> is like '274'.");
                System.out.println("what sections are there for <subject> <classnumber>              :where <subject> is like 'CS' and <classnumber> is like '274'.");
            }
            else if (m1.find()) {
                System.out.println("In " + m1.group(1).toUpperCase() + " " + m1.group(2) + "." + m1.group(3) + ":");
                try{
                    result = conn.namesInClass(m1.group(1).toUpperCase() + m1.group(2) + m1.group(3),TERM_CODE);
                }
                catch(SQLException e){}
                result.printData();
            }
            else if (m2.find()) {
                System.out.println("Prereqs for "+m2.group(1).toUpperCase()+" "+m2.group(2)+" are:");
                try{
                    result = conn.getPrereqs(m2.group(1).toUpperCase(),m2.group(2));
                }
                catch(SQLException e){}
                result.printData();
                System.out.println("Students who have unsatisfied prereqs for "+m2.group(1).toUpperCase()+" "+m2.group(2)+"."+m2.group(3)+", along with the classes they are missing:");
                try{
                    result = conn.PrereqCheck(m2.group(1).toUpperCase(),m2.group(2),m2.group(3),TERM_CODE);
                    System.out.println(result.getRowCount()+"/"+conn.namesInClass(m2.group(1).toUpperCase()+m2.group(2)+m2.group(3),TERM_CODE).getRowCount()+" students in the class do not meet the prerequisites.");
                }
                catch(SQLException e){}
                result.printData();
            }
            else if (m3.find()) {
                System.out.println("Prereqs for "+m3.group(1).toUpperCase()+" "+m3.group(2)+":");
                try{
                    result = conn.getPrereqs(m3.group(1).toUpperCase(),m3.group(2));
                }
                catch(SQLException e){}
                result.printData();
            }
            else if (m4.find()) {
                System.out.println("Sections for "+m4.group(1).toUpperCase()+" "+m4.group(2)+":");
                try{
                    result = conn.getSections(m4.group(1).toUpperCase(),m4.group(2),TERM_CODE);
                }
                catch(SQLException e){}
                result.printData();
            }
            else System.out.println("I'm sorry, I didn't understand that. Please enter 'help' for information on acceptable queries.");
            System.out.print("\nEnter Query: ");
        }

    }
}
