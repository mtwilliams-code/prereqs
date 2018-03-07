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
    private static Scanner scanner = new Scanner( System.in );
    private static Data result;
    public static void main( String[] args )
    {
        SQL_Connector conn = null;
        System.out.println("Give us a moment to ensure we can connect to the database...");
        try {
            String url = "jdbc:mysql://localhost:3306/" + "records";
            String username = "java";
            String password = "Java";
            conn = new SQL_Connector(url, username, password);
        } catch (Exception ex) {
            System.out.println("Uh oh! Something went wrong and we could not connect to the database.");
            System.out.println(
                    "Please ensure you are hosting a mySQL server listening to port 3306 with user:java and pass: Java.");
        }

        System.out.println("Hello. What would you like to do? You may enter 'help' for options: ");
        String input;
        Pattern classMembersRegex = Pattern.compile("who is in (\\w{2,4})\\s?(\\d{3})\\.(\\w{2,3})");
        Pattern notQualifiedRegex = Pattern.compile("who is not qualified for (\\w{2,4})\\s?(\\d{3})\\.(\\w{2,3})");
        Pattern prereqsForRegex = Pattern.compile("what are the prereqs for (\\w{2,4})\\s?(\\d{3})");
        System.out.print("\nEnter Query: ");
        while (!(input = scanner.nextLine().toLowerCase()).equalsIgnoreCase("end")) {
            Matcher m1 = classMembersRegex.matcher(input); 
            Matcher m2 = notQualifiedRegex.matcher(input); 
            Matcher m3 = prereqsForRegex.matcher(input);
            if (input.equalsIgnoreCase("help")) {
                System.out.println("You may enter the following commands (caps insensitive):");
                System.out.println("end        :to exit the program");
                System.out.println("who is in <subject> <classnumber>.<sectionnumber>                :where <subject> is like 'CS' and <classnumber> is like '274' and  <sectionnumber> is like '01'.");
                System.out.println("who is not qualified for <subject> <classnumber>.<sectionnumber> :where <subject> is like 'CS' and <classnumber> is like '274' and  <sectionnumber> is like '01'.");
                System.out.println("what are the prereqs for <subject> <classnumber>                 :where <subject> is like 'CS' and <classnumber> is like '274'.");
            }
            else if (m1.find()) {
                System.out.println("In " + m1.group(1) + " " + m1.group(2) + "." + m1.group(3) + ":");
                try{
                    result = conn.namesInClass(m1.group(1) + m1.group(2) + m1.group(3),201710);
                }
                catch(SQLException e){}
                result.printData();
            }
            else if (m2.find()) {
                System.out.println("Students who have unsatisfied prereqs for "+m2.group(1)+" "+m2.group(2)+"."+m2.group(3)+":");
                try{
                    result = conn.PrereqCheck(m2.group(1),m2.group(2),m2.group(3),201410);
                }
                catch(SQLException e){}
                result.printData();
            }
            else if (m3.find()) {
                System.out.println("Prereqs for "+m3.group(1)+" "+m3.group(2)+":");
                try{
                    result = conn.getPrereqs(m3.group(1),m3.group(2));
                }
                catch(SQLException e){}
                result.printData();
            }
            else System.out.println("I'm sorry, I didn't understand that. Please enter 'help' for information on acceptable queries.");
            System.out.print("Enter Query: ");
        }

    }
}
