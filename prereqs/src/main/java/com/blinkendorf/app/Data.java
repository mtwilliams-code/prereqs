package com.blinkendorf.app;

import java.util.ArrayList;

/**
 * This is a class to hold results from SQL queries. Apparently we can not return result sets,
 * so we're building another type to hold the information in an accessible format. 
 * 
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
  public void appendColumn(String title) 
  {
    c_titles.add(title);
  }

  public void add(ArrayList<String> record)
  {
    data.add(record);
  }

  public ArrayList<String> getColumn(int i)
  {
    ArrayList<String> rslt = new ArrayList<String>();
    for (int j = 0; j < data.size(); j++)
    {
      rslt.add(data.get(j).get(i));
    }
    return rslt;
  }

  public ArrayList<String> getRow(int i)
  {
    ArrayList<String> rs = 
  }

  public void printData()
  {
    int numColumns;
    String formattedTable = "";
      numColumns = c_titles.size();
      formattedTable += String.format("|%12.12s|", c_titles.get(0));
      for(int i = 1; i < numColumns; i++)
      {
        formattedTable += String.format("|%12.12s|", c_titles.get(i));
      }
      formattedTable += "\n";
      for (int i = 0; i < data.size(); i++)
      {
        formattedTable += String.format("|%12.12s|", data.get(i).get(0));
        for(int j = 1; j < numColumns; j++)
        {
          formattedTable += String.format("|%12.12s|", data.get(i).get(j));
        }
        formattedTable += "\n";
      }
      if (data.isEmpty()) System.out.println("No one is in this class");
      else System.out.println(formattedTable);
    }

    public String getFirstRecord()
    {
      return data.get(0).get(0) + " " + data.get(0).get(1);
    }


  }

