package com.blinkendorf.app;

import java.util.ArrayList;

public class Data {

  ArrayList<String> c_titles = new ArrayList<String>();
  ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

  public void appendColumn(String title) 
  {
    c_titles.add(title);
  }

  public void add(ArrayList<String> record)
  {
    data.add(record);
  }

  public String printData()
  {
    int numColumns;
    String formattedTable = "";
      numColumns = c_titles.size();
      formattedTable += String.format("|%12.12s|", c_titles.get(0));
      for(int i = 2; i <= numColumns; i++)
      {
        formattedTable += String.format("|%12.12s|", c_titles.get(0));
      }
      formattedTable += "\n";
    //   while( rslt.next() )
    //   {
    //     formattedTable += String.format("|%12.12s|", rslt.getString(1));
    //     for(int i = 2; i <= numColumns; i++)
    //     {
    //       formattedTable += String.format("|%12.12s|", rslt.getString(i));
    //     }
    //     formattedTable += "\n";
    //   }
      return formattedTable;
    }

    public String getFirstRecord()
    {
      return data.get(0).get(0) + " " + data.get(0).get(1);
    }


  }

