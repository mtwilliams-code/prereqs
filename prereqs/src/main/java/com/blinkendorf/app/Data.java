package com.blinkendorf.app;

import java.util.ArrayList;

/**
 * This is a class to hold results from SQL queries. Apparently we can not return result sets,
 * so we're building another type to hold the information in an accessible format. 
 * 
 */
public class Data {

  ArrayList<String> c_titles = new ArrayList<String>();
  ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

  /**
   * Adds a new column title to the end of the current list of titles.
   * 
   * @param title The title of the new column
   */
  public void appendColumn(String title) {
    c_titles.add(title);
  }

  /** 
   * Adds a new row to the Data table
   * 
   * @param record ArrayList<String> containing the data from the row
   */
  public void add(ArrayList<String> record) {
    data.add(record);
  }

  /**
   * Gets a column from the data table.
   * 
   * These will be all of the records' values for a certain attribute
   * 
   * @param i Integer defining the column to pull. 0-indexed.
   * @return ArrayList<String> containing all the data from the column.
   */
  public ArrayList<String> getColumn(int i) {
    ArrayList<String> rslt = new ArrayList<String>();
    for (int j = 0; j < data.size(); j++) {
      String pointless = data.get(j).get(i);
      rslt.add(pointless);
    }
    return rslt;
  }

  /**
   * Gets a row (entry) from the data table.
   * 
   * @param i Integer defining the row to pull. 0-indexed.
   * @return ArrayList<String> containing all the data from the row.
   */
  public ArrayList<String> getRow(int i) {
    ArrayList<String> rs = data.get(i);
    return rs;
  }

  /**
   * Prints all of the column labels and data from the Data object straight to stdout.
   */
  public void printData() {
    int numColumns;
    String formattedTable = "";
    if (!c_titles.isEmpty()) {
      numColumns = c_titles.size();
      formattedTable += String.format("|%18.18s|", c_titles.get(0));
      for (int i = 1; i < numColumns; i++) {
        formattedTable += String.format("|%18.18s|", c_titles.get(i));
      }
    }
    formattedTable += "\n";
    for (int i = 0; i < data.size(); i++) {
      formattedTable += String.format("|%18.18s|", data.get(i).get(0));
      for (int j = 1; j < data.get(i).size(); j++) {
        formattedTable += String.format("|%18.18s|", data.get(i).get(j));
      }
      formattedTable += "\n";
    }
    if (data.isEmpty())
      System.out.println("No one is in this class");
    else
      System.out.println(formattedTable);
  }

  /**
   * Helper function to return first two columns from the first row.
   * @return The first two columns of the first record concatenated together with a space between them.
   */
  public String getFirstRecord() {
    return data.get(0).get(0) + " " + data.get(0).get(1);
  }

  /** 
   * Counts the rows in a Data object.
   * @return Integer with the number of rows.
   */
  public int getRowCount() {
    return data.get(0).size();
  }

  /**
   * Checks if Data is empty.
   * @return Boolean, true if empty, false otherwise.
   */
  public Boolean isEmpty() {
    return (data.size() == 0);
  }

  /**
   * Returns the titles of the columns from Data
   * @return Arraylist<String> containing all column titles
   */
  public ArrayList<String> getTitles()
  {
    return c_titles;
  }

}
