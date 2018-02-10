Matthew 2/9/18 11:33pm
======================

I just built a maven project in this directory so that we can use it to manage dependencies.

I am going to next try to get maven to include cucumber and JSEF.

JSEF is an api which will hopefully let us read easily from a CSV into any arbitrary Java object.

The plan is to read these objects from the file and shove them into an sql database. I vote we make John M. be in charge of the SQL.

I am of the opinion that it would be wise to assign some of the positions described in chapter two of the Agile textbook. 

We should start next week as our first iteration, and strive to have iterations either weekly or biweekly. I am open to input. 

Proposed schedule:
------------------
+ Iteration 1: Read CSV file (using JSEF?) and put it into SQL. Have basic queries working.
                Possible test cases/user stories include giving a couple of class records and reading back the number of students registered for a class.
+ Iteration 2: Process data/build SQL queries to confirm that all students registered have completed all prereqs satisfactorily. 
                Test cases should validate the full range of expected features.

If you would rather divide this further and can think of a reasonable way to do so, I'm open to the idea.

Anyway, expect another update later tonight when I get JSEF working.



*[JSEF]: Java Simple Exchange Format
