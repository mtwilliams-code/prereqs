package com.blinkendorf.app;

import java.util.Scanner; 
import com.blinkendorf.app.SQL_Connector;


/**
 * Hello world!
 *
 */
public class App 
{
    private static Scanner scanner = new Scanner( System.in );
    public static void main( String[] args )
    {
        SQL_Connector conn = null;
        System.out.println( "Give us a moment to ensure we can connect to the database..." );
        try {
        conn = new SQL_Connector();
        }
        catch (Exception ex) {
            System.out.println("Uh oh! Something went wrong and we could not connect to the database.");
            System.out.println("Please ensure you are hosting a mySQL server listening to port 3306 with user:java and pass: Java.");
        }

        System.out.println("Hello. What would you like to do? You may enter 'help' for options: ");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("help")) {
            System.out.println("You may enter the following commands (caps insensitive):");
            System.out.println("'who is in <subject> <classnumber>.<sectionnumber>', where <subject> is like 'CS' and <classnumber> is like '274' and  <sectionnumber> is like '01'.");
            System.out.println("'who is not qualified for <subject> <classnumber>.<sectionnumber>', where <subject> is like 'CS' and <classnumber> is like '274' and  <sectionnumber> is like '01'.");
        }



    }
}
