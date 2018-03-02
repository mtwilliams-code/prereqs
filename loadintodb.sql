DROP DATABASE IF EXISTS RECORDS;
CREATE DATABASE RECORDS;
USE RECORDS;
DROP TABLE IF EXISTS REGISTRATION;
CREATE TABLE REGISTRATION (
	Pidm INT NOT NULL,
    Term_Code INT,
    Part_of_Term_Code VARCHAR(36),
    Part_of_Term_Desc VARCHAR(36),
    Enrolled_Ind VARCHAR(36),
    Registered_Ind VARCHAR(36),
    Junk VARCHAR(2),
    Student_Status_Code VARCHAR(36),
    Student_Status_Desc VARCHAR(36),
    Level_Code VARCHAR(36),
    Level_Desc VARCHAR(36),
    Student_Type_Code VARCHAR(36),
    Student_Type_Desc VARCHAR(36),
    Program_Code1 VARCHAR(36),
    Program_Code2 VARCHAR(36),
    Campus_Code VARCHAR(36),
    Campus_Desc VARCHAR(36),
    Department_Code VARCHAR(36),
    Department_Desc VARCHAR(36),
    Degree_Code1 VARCHAR(36),
    Degree_Desc1 VARCHAR(36),
    College_Code1 VARCHAR(36),
    College_Desc1 VARCHAR(36),
    Major_Code1 VARCHAR(36),
    Major_Desc1 VARCHAR(36),
    Major_Code1_2 VARCHAR(36),
    Major_Desc1_2 VARCHAR(36),
    Degree_Code2 VARCHAR(36),
    Degree_Desc2 VARCHAR(36),
    College_Code2 VARCHAR(36),
    College_Desc2 VARCHAR(36),
    Major_Code2 VARCHAR(36),
    Major_Desc2 VARCHAR(36),
    Class_Code VARCHAR(36),
    Class_Desc VARCHAR(36),
    CRN VARCHAR(36),
    Reg_STS_Code VARCHAR(36),
    Reg_STS_Desc VARCHAR(36),
    Spec_Approval_Ind VARCHAR(36),
    Reg_Error_Flag VARCHAR(36),
    Subject_Code VARCHAR(36),
    Subject_Desc VARCHAR(36),
    Course_Number VARCHAR(36),
    Section_Number VARCHAR(36),
    Course_Title VARCHAR(36),
    Course_Level_Code VARCHAR(36),
    Course_Campus_Code VARCHAR(36),
    Billing_Hours VARCHAR(36),
    Credit_Hours VARCHAR(36),
    Instructor_ID VARCHAR(36),
    Instructor_Name VARCHAR(36),
    Hours_Attended VARCHAR(36),
    Grade_Mode_Code VARCHAR(36),
    Grade_Mode_Desc VARCHAR(36),
    Midterm_Grade_Code VARCHAR(36),
    Grade_Code VARCHAR(36),
    Banner_ID VARCHAR(36),
    First_Name VARCHAR(36),
    Last_Name VARCHAR(36),
    Middle_Name VARCHAR(36),
    Prefix VARCHAR(36),
    Suffix VARCHAR(36),
    Preferred_First_Name VARCHAR(36),
    Confid_Ind VARCHAR(36),
    ACU_Email_Address VARCHAR(36),
    Home_Email_Address VARCHAR(36),
    Begin_Time_1 VARCHAR(36),
    End_Time1 VARCHAR(36),
    Bldg_Code1 VARCHAR(36),
    Bldg_Desc1 VARCHAR(36),
    Room_Code1 VARCHAR(36),
    Schd_Code1 VARCHAR(36),
    Monday_Ind1 VARCHAR(36),
    Tuesday_Ind1 VARCHAR(36),
    Wednesday_Ind1 VARCHAR(36),
    Thursday_Ind1 VARCHAR(36),
    Friday_Ind1 VARCHAR(36),
    Saturday_Ind1 VARCHAR(36),
    Sunday_Ind1 VARCHAR(36),
    Begin_Time2 VARCHAR(36),
    End_Time2 VARCHAR(36),
    Bldg_Code2 VARCHAR(36),
    Bldg_Desc2 VARCHAR(36),
    Room_Code2 VARCHAR(36),
    Schd_Code2 VARCHAR(36),
    Monday_Ind2 VARCHAR(36),
    Tuesday_Ind2 VARCHAR(36),
    Wednesday_Ind2 VARCHAR(36),
    Thursday_Ind2 VARCHAR(36),
    Friday_Ind2 VARCHAR(36),
    Saturday_Ind2 VARCHAR(36),
    Sunday_Ind2 VARCHAR(36),
    Advisor1_Term_Code_Eff VARCHAR(36),
    Advisor1_Last_Name VARCHAR(36),
    Advisor1_First_Name VARCHAR(36),
    Advisor1_Advisor_Code VARCHAR(36),
    Advisor1_Primary_Advisor_Ind VARCHAR(36),
    Sport1_Activity_Code VARCHAR(36),
    Sport1_Code VARCHAR(36),
    Sport1_Eligibilty_Code VARCHAR(36),
    Sport1_Athletic_Aid_Ind VARCHAR(36),
    Sport2_Activity__Code VARCHAR(36),
    Sport2_Code VARCHAR(36),
    Sport2_Eligibility_Code VARCHAR(36),
    Sport2_Athletic_Aid_Ind VARCHAR(36),
    Vet_Term VARCHAR(36),
    Vet_Code VARCHAR(36),
    Vet_Desc VARCHAR(36),
    Vet_Certified_Hours VARCHAR(36),
    Vet_Certified_Date VARCHAR(36),
    Vet_Certified_Hours2 VARCHAR(36),
    Minor_Code1 VARCHAR(36),
    Minor_Desc1 VARCHAR(36),
    Conc_Code1 VARCHAR(36),
    Conc_Desc1 VARCHAR(36),
    Minor_Code1_2 VARCHAR(36),
    Minor_Desc1_2 VARCHAR(36),
    Conc_Code1_2 VARCHAR(36),
    Conc_Desc1_2 VARCHAR(36),
    Minor_Code2 VARCHAR(36),
    Minor_Desc2 VARCHAR(36),
    Rate_Code VARCHAR(36),
    Ovrall_Cumm_GPA_Hrs_Attempted VARCHAR(36),
    Ovrall_Cumm_GPA__Hours_Earned VARCHAR(36),
    Ovrall_Cumm_GPA_Hrs VARCHAR(36),
    Ovrall_Cumm_GPA_Quality_Points VARCHAR(36),
    Ovrall_Cumm_GPA VARCHAR(36),
    Ovrall_Cumm_GPA_Hrs_Passed VARCHAR(36),
    Dead_Ind VARCHAR(36),
    Date_Class_Added VARCHAR(36),
    Registration_Status_Date VARCHAR(36),
    Activity_Date VARCHAR(36),
    Course_College_Code VARCHAR(36),
    Course_College_Desc VARCHAR(36),
    Course_Dept_Code VARCHAR(36),
    Course_Dept_Desc VARCHAR(36),
    International_Ind VARCHAR(36),
    Part_of_Term_Start_Date VARCHAR(36),
    Part_of_Term_End_Date VARCHAR(36),
    Section_Max_Enrollment VARCHAR(36),
    Section_Enrollment VARCHAR(36),
    Section_Available_Seats VARCHAR(36),
    Section_Schedule_Type VARCHAR(36),
    Section_Instruction_Method VARCHAR(36),
    Section_Session_Code VARCHAR(36),
    Ipeds_Ethnic_Code VARCHAR(36),
    Ipeds_Ethnic_Desc VARCHAR(36),
    PRIMARY KEY (Pidm),
    INDEX index2 (Subject_Code ASC , Course_Number ASC)
);

DROP function IF EXISTS `CLASS_TAKEN`;

DELIMITER $$
USE `RECORDS`$$
CREATE DEFINER=`root`@`localhost` FUNCTION `CLASS_TAKEN`(PIDMIN INT, GRADE CHAR, CLASS_CODE VARCHAR(10)) RETURNS char(1) CHARSET latin1
BEGIN
	DECLARE done INT DEFAULT FALSE;
    DECLARE CCode VARCHAR(10) default '';
    DECLARE FGrade VARCHAR(2);
    
    DECLARE OUTPUT CHAR DEFAULT 'N';
    
    DECLARE CLASSES CURSOR FOR (SELECT CONCAT(Subject_Code,Course_Number), Grade_Code FROM REGISTRATION WHERE PIDM = PIDMIN);

	DECLARE CONTINUE handler for NOT FOUND SET done = true;

	OPEN CLASSES;

	start_loop: loop
		fetch CLASSES into CCode, FGrade;
        if CCode = CLASS_CODE and (FGrade BETWEEN 'A' AND GRADE OR FGrade IN ('', 'P')) then 
			set OUTPUT='Y'; 
			leave start_loop; 
            end if;
        if done then 
			leave start_loop; 
            end if;
        
    end loop;
    
    CLOSE CLASSES;

RETURN OUTPUT;
END$$

DELIMITER ;
