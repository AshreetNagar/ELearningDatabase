/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CPS_Assignment_A9;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Scanner;
/**
 *
 * @author homeperson4
 */
public class ShellUI {
    
    //Internal function used to display pause prompt
    public static void pause(){
        //Pause using the scanner object waiting for next line
        Scanner myObj = new Scanner(System.in);  
        System.out.print("\n Press Enter To Continue");
        myObj.nextLine();
    }
    
    //Internal function used to run queries
    public static void run_query(String[] queries,Connection conn1){
        //A Try is used for the creation of a statement with the Oracle DB Connection
        try (Statement stmt = conn1.createStatement()) {
            //The program loops through the array of queries provided
            for(int index=0; index<queries.length;index++)
            {
                //Execute the current query and save its data
                ResultSet rs = stmt.executeQuery(queries[index]);
                ResultSetMetaData rsmd = rs.getMetaData();
                //Print out the data from that query, with a new line for each row
                while (rs.next()) {
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        if (i > 1) System.out.print(",  ");
                        String columnValue = rs.getString(i);
                        System.out.print(rsmd.getColumnName(i) + ": "+ columnValue + "\t");
                    }
                    System.out.println("");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
    }
    
    //Internal function used to present input prompts to the user
    public static String user_input(String question){
        //Present the given question, then get and return user input using Scanner
        Scanner myObj = new Scanner(System.in);
        System.out.print(question);
        String choice = myObj.nextLine();
        return choice;
    }
    
    //Function to drop all tables from the database
    public static void dropall(Connection conn1){
        //Construct a array of all queries needed to delete all tables
        String[] queries = {"DROP TABLE Quizanswer",
                            "DROP TABLE QinQuiz",
                            "DROP TABLE quizquestion",
                            "DROP TABLE chapterprogress",
                            "DROP TABLE QuizInCh",
                            "DROP TABLE quiz",
                            "DROP TABLE ChInCourse",
                            "DROP TABLE chapter",
                            "DROP TABLE progress",
                            "DROP TABLE CourseIsTopic",
                            "DROP TABLE topic",
                            "DROP TABLE taught",
                            "DROP TABLE course",
                            "DROP TABLE instructor",
                            "DROP TABLE student"};
        //Run the queries and print out the given code
        for(int i=0; i<queries.length;i++)
        {
            try (Statement stmt = conn1.createStatement()) {
                ResultSet rs = stmt.executeQuery(queries[i]);
                while (rs.next()) {
                }
            } catch (SQLException e) {
                System.out.println(e.getErrorCode());
            }  
        }
    }
    
    //Function to create all tables in the database
    public static void createall(Connection conn1){
        //Construct a array of all queries needed to create all tables
        String[] queries = new String[15];
        queries[0] = "CREATE TABLE student(StudentUsername VARCHAR2(25) NOT NULL PRIMARY KEY,StudentPassword VARCHAR2(25) NOT NULL,StudentName VARCHAR2(50) ,StudentDescription VARCHAR2(255))";
        queries[1] = "CREATE TABLE instructor(InstructorUsername VARCHAR2(25) NOT NULL PRIMARY KEY,    InstructorPassword VARCHAR2(25) NOT NULL,    InstructorName VARCHAR2(50),    InstructorDescription VARCHAR2(255))";
        queries[2] = "CREATE TABLE course(    CourseID VARCHAR2(25) NOT NULL PRIMARY KEY,    CourseName VARCHAR2(25) NOT NULL,    CourseDescription VARCHAR2(255) NOT NULL,    CourseDifficulty VARCHAR2(25),    CoursePrice NUMBER(4,1) DEFAULT 0 NOT NULL,    CourseHasCertification VARCHAR2(25),    Enrollments NUMBER(10) DEFAULT 0 )";
        queries[3] = "CREATE TABLE taught(    CourseID VARCHAR2(25) REFERENCES course(CourseID),    InstructorUsername VARCHAR2(25) REFERENCES instructor(InstructorUsername))";
        queries[4] = "CREATE TABLE topic(    TopicName VARCHAR2(25) NOT NULL PRIMARY KEY,    TopicInfo VARCHAR2(255))";
        queries[5] = "CREATE TABLE CourseIsTopic(    CourseID VARCHAR2(25) REFERENCES course(CourseID),    TopicName VARCHAR2(25) REFERENCES topic(TopicName))";
        queries[6] = "CREATE TABLE progress(    UserID VARCHAR2(25) REFERENCES student(StudentUsername),    CourseID VARCHAR2(25) REFERENCES course(CourseID),    Fails NUMBER(1) DEFAULT 0 NOT NULL CHECK(Fails BETWEEN 0 AND 1) ,    Completed NUMBER(1) DEFAULT 0 NOT NULL CHECK(Completed BETWEEN 0 AND 1),    InProgress NUMBER(3) DEFAULT 0 NOT NULL CHECK(InProgress BETWEEN 0 and 100),    PlanToDo NUMBER(1) DEFAULT 0 NOT NULL CHECK(PlanToDo BETWEEN 0 AND 1))";
        queries[7] = "CREATE TABLE chapter(    ChapterID VARCHAR2(25) NOT NULL PRIMARY KEY,    Videos VARCHAR2(255),    Readings VARCHAR2(4000),    ChapterEndQuiz NUMBER(1) DEFAULT 0 NOT NULL CHECK(ChapterEndQuiz BETWEEN 0 AND 1),    CourseID VARCHAR2(25) REFERENCES course(CourseID))";
        queries[8] = "CREATE TABLE ChInCourse(    CourseID VARCHAR2(25) REFERENCES course(CourseID),    ChapterID VARCHAR2(25) REFERENCES chapter(ChapterID))";
        queries[9] = "CREATE TABLE quiz(   QuizID VARCHAR2(25) NOT NULL PRIMARY KEY,    QuizInfo VARCHAR2(255),    QuizQuestionAmount NUMBER(4) NOT NULL CHECK(QuizQuestionAmount > 0))";
        queries[10] = "CREATE TABLE QuizInCh(    ChapterID VARCHAR2(25) REFERENCES chapter(ChapterID),    QuizID VARCHAR2(25) REFERENCES quiz(QuizID))";
        queries[11] = "CREATE TABLE chapterprogress(    ReadingsDone NUMBER(3) DEFAULT 0 NOT NULL CHECK(ReadingsDone >= 0),    VideosDone NUMBER(3) DEFAULT 0 NOT NULL CHECK(VideosDone >= 0),   ProblemSetsDone NUMBER(3) DEFAULT 0 NOT NULL CHECK(ProblemSetsDone >= 0),    EndofChapterQuizDone NUMBER(3) DEFAULT 0 NOT NULL CHECK(EndofChapterQuizDone >= 0),   ChapterID VARCHAR2(25) REFERENCES chapter(ChapterID),    UserID VARCHAR2(25) REFERENCES student(StudentUsername))";
        queries[12] = "CREATE TABLE quizquestion(    QuestionID VARCHAR2(25) NOT NULL PRIMARY KEY,   Question VARCHAR2(4000) NOT NULL,   QuestionNumber NUMBER(4) NOT NULL CHECK(QuestionNumber > 0),   QuizID VARCHAR2(25) REFERENCES quiz(QuizID))";
        queries[13] = "CREATE TABLE QinQuiz(    QuizID VARCHAR2(25) REFERENCES quiz(QuizID),    QuestionID VARCHAR2(25) REFERENCES quizquestion(QuestionID))";
        queries[14] = "CREATE TABLE Quizanswer(   QuestionID VARCHAR2(25) REFERENCES quizquestion(QuestionID), Answer VARCHAR2(4000) NOT NULL,  Correct NUMBER(1) DEFAULT 0 NOT NULL CHECK(Correct BETWEEN 0 AND 1))";
        //Run the queries and print out the given code
        for(int i=0; i<queries.length;i++)
        {
            try (Statement stmt = conn1.createStatement()) {
                ResultSet rs = stmt.executeQuery(queries[i]);
                while (rs.next()) {
                }
            } catch (SQLException e) {
                System.out.println(e.getErrorCode());
            }  
        }
    }
    
    //Function to populate tables with sample data
    public static void populate(Connection conn1){
        //Construct a array of all queries needed to populate all tables
        String[] queries = new String[32];
        queries[0] = "INSERT INTO student VALUES ('mg_the_goat','abc123','Micheal G. Gordon','the student micheal geoffrey gordon')";
        queries[1] = "INSERT INTO student VALUES ('messi_the_goat','abc123','messi','the student messi')";
        queries[2] = "INSERT INTO instructor VALUES ('cr_instructor','abc123!','Christopher Ronald','the instructor christopher ronald')";        
        queries[3] = "INSERT INTO instructor VALUES ('fresh_instructor','abc123!','New instructor','new instructor never taught a course')";        
        queries[4] = "INSERT INTO course VALUES ('CPS510','Database Systems','we learn about database','MEDIUM',999.5,'TRUE',2)";
        queries[5] = "INSERT INTO course VALUES ('COE538','microprocessors','we learn about microprocessors','MEDIUM',99.95,'TRUE',2)";
        queries[6] = "INSERT INTO course VALUES ('COE001','microcontroller test','test create a microcontroller course','EASY',99.95,'TRUE',0)";
        queries[7] = "INSERT INTO taught VALUES ('CPS510','cr_instructor')";
        queries[8] = "INSERT INTO taught VALUES ('COE001','fresh_instructor')";
        queries[9] = "INSERT INTO topic VALUES ('Computers','Computers Programming')";
        queries[10] = "INSERT INTO topic VALUES ('UNFINISHED','Unfinished / Beta Courses')";
        queries[11] = "INSERT INTO CourseIsTopic VALUES ('CPS510','Computers')";
        queries[12] = "INSERT INTO CourseIsTopic VALUES ('COE538','Computers')";
        queries[13] = "INSERT INTO CourseIsTopic VALUES ('COE001','UNFINISHED')";
        queries[14] = "INSERT INTO progress VALUES ('mg_the_goat','CPS510',0,0,1,0)";
        queries[15] = "INSERT INTO progress VALUES ('messi_the_goat','CPS510',0,0,2,0)";
        queries[16] = "INSERT INTO progress VALUES ('mg_the_goat','COE538',0,0,1,0)";
        queries[17] = "INSERT INTO progress VALUES ('messi_the_goat','COE538',0,0,2,0)";
        queries[18] = "INSERT INTO chapter VALUES ('CPS510_CH1.1','https://www.youtube.com/watch?v=dQw4w9WgXcQ','There are tables, .',1,'CPS510')";
        queries[19] = "INSERT INTO chapter VALUES ('CPS510_CH1.2','https://www.youtube.com/watch?v=dQw4w9WgXcQ','There are columns.',0,'CPS510')";
        queries[20] = "INSERT INTO chapter VALUES ('CPS510_CH1.3','https://www.youtube.com/watch?v=dQw4w9WgXcQ','There are relationships.',1,'CPS510')";        
        queries[21] = "INSERT INTO ChInCourse VALUES ('CPS510','CPS510_CH1.1')";
        queries[22] = "INSERT INTO quiz VALUES ('CPS510_CH1.1_Qu1','Basic Database Terminology',10)";
        queries[23] = "INSERT INTO quiz VALUES ('CPS510_CH1.1_Qu2','Basic Database Terminology 2',10)"; 
        queries[24] = "INSERT INTO QuizInCh VALUES ('CPS510_CH1.1','CPS510_CH1.1_Qu1')";
        queries[25] = "INSERT INTO chapterprogress VALUES (1,1,1,1,'CPS510_CH1.1','mg_the_goat')";
        queries[26] = "INSERT INTO chapterprogress VALUES (1,0,0,0,'CPS510_CH1.2','mg_the_goat')"; 
        queries[27] = "INSERT INTO chapterprogress VALUES (1,1,1,1,'CPS510_CH1.1','messi_the_goat')";
        queries[28] = "INSERT INTO chapterprogress VALUES (0,0,0,0,'CPS510_CH1.2','messi_the_goat')"; 
        queries[29] = "INSERT INTO quizquestion VALUES ('CPS510_CH1.1_Qu1_Q1','How do you insert information into a table',1,'CPS510_CH1.1_Qu1')";
        queries[30] = "INSERT INTO QinQuiz VALUES ('CPS510_CH1.1_Qu1','CPS510_CH1.1_Qu1_Q1')";
        queries[31] = "INSERT INTO Quizanswer VALUES ('CPS510_CH1.1_Qu1_Q1','insert',1)";
        //Run the queries and print out the given code
        for(int i=0; i<queries.length;i++)
        {
            try (Statement stmt = conn1.createStatement()) {
                ResultSet rs = stmt.executeQuery(queries[i]);
                while (rs.next()) {
                }
            } catch (SQLException e) {
                System.out.println(e.getErrorCode()+" on "+queries[i]);
            }  
        }
    }

    //UI function, presented when user choosed "Query Tables" in the main UI
    public static void query_ui(Connection conn1){
        //This function is implemented similarly to the "ui" function
        Boolean quit = false;
        while(quit == false){
            System.out.println("Options: ");
            System.out.println("\tList Tables");
            System.out.println("\tShow Table Data");
            System.out.println("\tSearch for Record");  
            System.out.println("\tUpdate Records");
            System.out.println("\tDelete Records");
            System.out.println("\tAdd Record");
            System.out.println("\tEXIT");
            String choice = user_input("\nENTER OPTION: ");
            
            if(choice.equalsIgnoreCase("List Tables")){
                listtables(conn1);
                pause();
            }else if(choice.equalsIgnoreCase("Show Table Data")){
                show_table_data(conn1);
                pause();
            }else if(choice.equalsIgnoreCase("Search for Record")){
                record_search(conn1);
                pause();                
            }else if(choice.equalsIgnoreCase("Update Records")){
                update_record(conn1);
                pause();
            }else if(choice.equalsIgnoreCase("Delete Records")){
                delete_record(conn1);
                pause();             
            }else if(choice.equalsIgnoreCase("Add Record")){
                add_record(conn1);
                pause();
            }else if(choice.equalsIgnoreCase("EXIT")){
                quit = true;
            }
        }
    }
    
    //Function to list all tables in the database
    public static void listtables(Connection conn1){
        //Run a query for listing all table names
        String[] queries = {"Select table_name from user_tables"};
        run_query(queries,conn1); 
    }
    
    //Function to show data from a user requested table
    public static void show_table_data (Connection conn1){
        //Construct a query with the user's table choice and run it
        String tablename = user_input("What table do you want to list data: ");
        String[] queries = {"Select * from "+tablename};
        run_query(queries,conn1);       
    }
    
    //Function to search for records from user parameters
    public static void record_search(Connection conn1){
        //Construct a query with the user's table,column, and WHERE parameters and run it
        String table = user_input("Which table do you want to search: ");
        String column = user_input("Which column do you want data from (* for all): ");;
        String param = user_input("What terms do you want to search with (ex:StudentName='messi'): ");
        String[] query = {"SELECT "+column+" FROM "+table+" WHERE "+param};
        run_query(query,conn1);
    }
    
    //Function to update records based on user input
    public static void update_record(Connection conn1){
        //Construct a table with the user's table, SET and WHERE parameter, and run it
        String table = user_input("Which table do you want to update:");
        String set = user_input("What data do you want to set (ex:Fails=1):");
        String where = user_input("On which records (ex:userid='mg_the_goat'):");
        String[] query = {"UPDATE "+table+" SET "+set+" WHERE "+where};
        run_query(query,conn1);
    }
    
    //Function to delete user specified records
    public static void delete_record(Connection conn1){
        //Construct a query with the user's table and WHERE parameters, and run it
        String table = user_input("Which table do you want to remove records:");
        String cond = user_input("Which records do you want removed (ex:studentname='messi')");
        String[] query = {"DELETE FROM "+table+" WHERE "+cond};
        run_query(query,conn1);
    }
    
    //Function to add records based on user input
    public static void add_record(Connection conn1){
        //Construct a query using the user's table and VALUES parameter and run it
        String table = user_input("Which table do you want to add records to:");
        String data = user_input("What data do you want inserted (ex: ('COE001','UNFINISHED') )");
        String[] query = {"INSERT INTO "+table+" VALUES "+data};
        run_query(query,conn1);
    }
    
    //Main UI Function. Fist UI the user is presented with when launching the program
    public static void ui(Connection conn1){
        //Variable to loop until the user wants to quit
        Boolean quit = false;
        //Loop while the user has not chosen to quit
        while(quit == false){
            //Present the user with all options
            System.out.println("Options: ");
            System.out.println("\tDrop Tables");
            System.out.println("\tCreate Tables");            
            System.out.println("\tPopulate Tables");  
            System.out.println("\tQuery Tables");
            System.out.println("\tEXIT");
            //Get the user's choice
            String choice =user_input("\nENTER OPTION: ");
            //Depending on the user's choice, run the requested function then pause
            if(choice.equalsIgnoreCase("Drop Tables")){
                dropall(conn1);
                System.out.println("All Tables dropped");
                pause();
            }else if(choice.equalsIgnoreCase("Create Tables")){
                createall(conn1);
                System.out.println("Tables created");
                pause();
            }else if(choice.equalsIgnoreCase("Populate Tables")){
                populate(conn1);
                System.out.println("Tables populated");
                pause();               
            }else if(choice.equalsIgnoreCase("Query Tables")){
                query_ui(conn1);
                pause();
            //If the user chooses to exit, set the quit variable to true so the while loop quits
            }else if(choice.equalsIgnoreCase("EXIT")){
                quit = true;
            }
        }
    }
    
    //Main function. Connected to database then launches UI
    public static void main(String[] args) {
        Connection conn1 = null;

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            String dbURL1 = "jdbc:oracle:thin:a2thanas/10282758@oracle.scs.ryerson.ca:1521:orcl";  // that is school Oracle database and you can only use it in the labs
            conn1 = DriverManager.getConnection(dbURL1);
            if (conn1 != null) {
                System.out.println("Connected with connection #1");
                ui(conn1);
                //query_ui(conn1);
                //record_search(conn1);
                /*
                dropall(conn1);
                createall(conn1);
                populate(conn1);
                */
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn1 != null && !conn1.isClosed()) {
                    conn1.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}