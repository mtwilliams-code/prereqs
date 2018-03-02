package com.blinkendorf.app;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.blinkendorf.app.SQL_Connector;


/**
 * Hello world!
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
        Pattern classMembersRegex = Pattern.compile("who is in (\\w*) (\\d*)\\.(\\d*)");
        Pattern notQualifiedRegex = Pattern.compile("who is not qualified for (\\w*) (\\d*)\\.(\\d*)");
        System.out.print("Enter Query: ");
        while (!(input = scanner.nextLine().toLowerCase()).equalsIgnoreCase("end")) {
            Matcher m1 = classMembersRegex.matcher(input); 
            Matcher m2 = notQualifiedRegex.matcher(input); 
            if (input.equalsIgnoreCase("help")) {
                System.out.println("You may enter the following commands (caps insensitive):");
                System.out.println("end        :to exit the program");
                System.out.println("who is in <subject> <classnumber>.<sectionnumber>        :where <subject> is like 'CS' and <classnumber> is like '274' and  <sectionnumber> is like '01'.");
                System.out.println("who is not qualified for <subject> <classnumber>.<sectionnumber>        :where <subject> is like 'CS' and <classnumber> is like '274' and  <sectionnumber> is like '01'.");
            }
            else if (m1.find()) {
                System.out.println("You want to know who is in " + m1.group(1) + " " + m1.group(2) + "." + m1.group(3) + "? Too bad. That's not implemented yet.");
                try{
                    result = conn.namesInClass(m2.group(1) + m2.group(2) + m2.group(3),201410);
                }
                catch(SQLException e){}
            }
            else if (m2.find()) {
                System.out.println("You want to know who is not qualified for " + m2.group(1) + " " + m2.group(2) + "." + m2.group(3) + "? Too bad. That's not implemented yet.");
                try{
                    result = conn.namesInClass(m2.group(1) + m2.group(2) + m2.group(3),201410);
                }
                catch(SQLException e){}
            }
            else System.out.println("I'm sorry, I didn't understand that. Please enter 'help' for information on acceptable queries.");
            System.out.print("Enter Query: ");
        }

    }
}
