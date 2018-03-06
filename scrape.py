#import libraries
import urllib
from bs4 import BeautifulSoup
import re
import csv
from datetime import datetime
import time

classRegex = "([a-zA-Z]{2,4}) (\\d{3})"
prereqRegex = r"(?:\s*([a-zA-Z]{2,4}) (\d{3}),?)"
prereqRegex2 = r"(?:"+prereqRegex+r"(?:\s*(?:;\s*)?or"+prereqRegex+")+,?|"+prereqRegex+")"
start = time.process_time()
file_IO_time = 0
regex_time = 0
BS4_time = 0
download_time = 0
last_time = start
# try-catch block to handle file output errors
with open('scrapedPrereqs.csv', 'w') as csv_file:
  writer = csv.writer(csv_file)
  #timing
  file_IO_time += time.process_time() - last_time
  # runs on each of the 19 pages of the catalog
  for pageNum in range(1,19):
    print("Page " + str(pageNum))
    print("++++++++++++++++++++++++++++++++++++++++++++")
    totalTime = time.process_time() - start
    if pageNum != 1:
      print("Time taken: file_IO|{0:.2f} regex|{1:.2f} BS4|{2:.2f} DL|{3:.2f}".format(file_IO_time/totalTime, regex_time/totalTime, BS4_time/totalTime, download_time/totalTime))
      print("++++++++++++++++++++++++++++++++++++++++++++")
    page_link = "http://catalog.acu.edu/content.php?catoid=2&catoid=2&navoid=62&filter%5B" 
    page_link += "item_type%5D=3&filter%5Bonly_active%5D=1&filter%5B3%5D=1&filter%5Bc"
    page_link += "page%5D=" + str(pageNum) + "#acalog_template_course_filter"
    # timing
    last_time = time.process_time()
    #downloads index page and builds BS4 object
    page = urllib.request.urlopen(page_link)
    #timing
    download_time += time.process_time() - last_time
    last_time = time.process_time()
    soup = BeautifulSoup(page, 'html.parser')
    # timing
    BS4_time += time.process_time() - last_time
    last_time = time.process_time()
    # for each link that matches the regex for 'SUBJ 000'
    for link in soup.find_all("a", string=re.compile(classRegex)):
      regex_time += time.process_time() - last_time
      last_time = time.process_time()
      # downloads page and turns into BS4 object
      courseLink = "http://catalog.acu.edu/" + link.get("href")
      coursePage = urllib.request.urlopen(courseLink)
      #timing
      download_time += time.process_time() - last_time
      last_time = time.process_time()
      courseSoup = BeautifulSoup(coursePage, 'html.parser')
      # timing
      BS4_time += time.process_time() - last_time
      last_time = time.process_time()
      # just captures, saves, and prints the subject code/number
      # we matched this on line 25, but I couldnt figure out how to reuse it
      m = re.match(classRegex,str(link.text))
      if m != None:
        courseName = m.group(1) + m.group(2)
        print("Class: " + link.text) #link.text) # + " | " + courseLink)
        # finds the first <hr> element
        prereqs = courseSoup.find("hr")
        # splits so that we only read between "Prerequisite:" and "same"(opt)
        prereqText = re.split(r"Prerequisite: |[sS]ame",prereqs.text)
        #timing
        if (len(prereqText) == 2 or len(prereqText) == 3): 
          prereqText = prereqText[1]
        else:
          prereqText = prereqText[0]
        # builds and iterator for all matches to prereqRegex2 inside the first <hr> element
        matches = re.finditer(prereqRegex2,prereqText)
        regex_time += time.process_time() - last_time
        if matches != None:
          # for reach one: removes formatting chars, prints, and writes to .csv
          for m in matches:
            last_time = time.process_time()
            prereqCode = m.group().strip(",; ").replace(" ","").replace("  ","").replace(" ;","").replace(" ","")
            print("| " + prereqCode)
            writer.writerow([courseName,prereqCode])
            file_IO_time += time.process_time() - last_time
        last_time = time.process_time()

    # I am getting some weird whitespace artifacts from
    # the way BS4 produces prereq.text
    #CS120,MATH109  orMATH124  orMATH185 ;orCS115