CREATE TABLE student(
    StudentUsername VARCHAR2(25) NOT NULL PRIMARY KEY,
    StudentPassword VARCHAR2(25) NOT NULL,
    StudentName VARCHAR2(50) ,
    StudentDescription VARCHAR2(255)
);
INSERT INTO student VALUES ('mg_the_goat','abc123','Micheal G. Gordon','the student micheal geoffrey gordon');
SELECT StudentUsername,StudentPassword,StudentName,StudentDescription from student;

CREATE TABLE instructor(
    InstructorUsername VARCHAR2(25) NOT NULL PRIMARY KEY,
    InstructorPassword VARCHAR2(25) NOT NULL,
    InstructorName VARCHAR2(50),
    InstructorDescription VARCHAR2(255)
);
INSERT INTO instructor VALUES ('cr_instructor','abc123!','Christopher Ronald','the instructor christopher ronald');
SELECT InstructorUsername,InstructorPassword,InstructorName, InstructorDescription from instructor;

CREATE TABLE course(
    CourseID VARCHAR2(25) NOT NULL PRIMARY KEY,
    CourseName VARCHAR2(25) NOT NULL,
    CourseDescription VARCHAR2(255) NOT NULL,
    CourseDifficulty VARCHAR2(25),
    CoursePrice NUMBER(4,1) DEFAULT 0 NOT NULL,
    CourseHasCertification VARCHAR2(25)
);
INSERT INTO course VALUES ('CPS510','Database Systems','we learn about database','MEDIUM',999.5,'TRUE');
SELECT CourseID,CourseName,CourseDescription,CourseDifficulty,CoursePrice,CourseHasCertification from course;

CREATE TABLE taught(
    CourseID VARCHAR2(25) REFERENCES course(CourseID),
    InstructorUsername VARCHAR2(25) REFERENCES instructor(InstructorUsername)
);
INSERT INTO taught VALUES ('CPS510','cr_instructor');
SELECT CourseID,InstructorUsername from taught;

CREATE TABLE topic(
    TopicName VARCHAR2(25) NOT NULL PRIMARY KEY,
    TopicInfo VARCHAR2(255)
);
INSERT INTO topic VALUES ('Computers','Computers Programming');
SELECT TopicName,TopicInfo from topic;

CREATE TABLE CourseIsTopic(
    CourseID VARCHAR2(25) REFERENCES course(CourseID),
    TopicName VARCHAR2(25) REFERENCES topic(TopicName)
);
INSERT INTO CourseIsTopic VALUES ('CPS510','Computers');
SELECT CourseID,TopicName from CourseIsTopic;

CREATE TABLE progress(
    UserID VARCHAR2(25) REFERENCES student(StudentUsername),
    CourseID VARCHAR2(25) REFERENCES course(CourseID),
    Fails NUMBER(1) DEFAULT 0 NOT NULL CHECK(Fails BETWEEN 0 AND 1) ,
    Completed NUMBER(1) DEFAULT 0 NOT NULL CHECK(Completed BETWEEN 0 AND 1),
    InProgress NUMBER(3) DEFAULT 0 NOT NULL CHECK(InProgress BETWEEN 0 and 100),
    PlanToDo NUMBER(1) DEFAULT 0 NOT NULL CHECK(PlanToDo BETWEEN 0 AND 1)
);
INSERT INTO progress VALUES ('mg_the_goat','CPS510',0,0,50,0);
SELECT UserID,CourseID,Fails,Completed,InProgress,PlanToDo from progress;

CREATE TABLE chapter(
    ChapterID VARCHAR2(25) NOT NULL PRIMARY KEY,
    Videos VARCHAR2(255),
    Readings VARCHAR2(4000),
    ChapterEndQuiz NUMBER(1) DEFAULT 0 NOT NULL CHECK(ChapterEndQuiz BETWEEN 0 AND 1),
    CourseID VARCHAR2(25) REFERENCES course(CourseID)
);
INSERT INTO chapter VALUES ('CPS510_CH1.1','https://www.youtube.com/watch?v=dQw4w9WgXcQ','There are tables, columns, relationships.',1,'CPS510');
SELECT ChapterID,Videos,Readings,ChapterEndQuiz,CourseID from chapter;

CREATE TABLE ChInCourse(
    CourseID VARCHAR2(25) REFERENCES course(CourseID),
    ChapterID VARCHAR2(25) REFERENCES chapter(ChapterID)
);
INSERT INTO ChInCourse VALUES ('CPS510','CPS510_CH1.1');
SELECT CourseID,ChapterID from ChInCourse;

CREATE TABLE quiz(
    QuizID VARCHAR2(25) NOT NULL PRIMARY KEY,
    QuizInfo VARCHAR2(255),
    QuizQuestionAmount NUMBER(4) NOT NULL CHECK(QuizQuestionAmount > 0),
    CourseID VARCHAR2(25) REFERENCES course(CourseID)
);
INSERT INTO quiz VALUES ('CPS510_CH1.1_Qu1','Basic Database Terminology',10,'CPS510');
SELECT QuizID,QuizInfo,QuizQuestionAmount,CourseID from quiz;

CREATE TABLE QuizInCh(
    ChapterID VARCHAR2(25) REFERENCES chapter(ChapterID),
    QuizID VARCHAR2(25) REFERENCES quiz(QuizID)
);
INSERT INTO QuizInCh VALUES ('CPS510_CH1.1','CPS510_CH1.1_Qu1');
SELECT ChapterID,QuizID from QuizInCh;

CREATE TABLE chapterprogress(
    ReadingsDone NUMBER(3) DEFAULT 0 NOT NULL CHECK(ReadingsDone >= 0),
    VideosDone NUMBER(3) DEFAULT 0 NOT NULL CHECK(VideosDone >= 0),
    ProblemSetsDone NUMBER(3) DEFAULT 0 NOT NULL CHECK(ProblemSetsDone >= 0),
    EndofChapterQuizDone NUMBER(3) DEFAULT 0 NOT NULL CHECK(EndofChapterQuizDone >= 0),
    ChapterID VARCHAR2(25) REFERENCES chapter(ChapterID),
    UserID VARCHAR2(25) REFERENCES student(StudentUsername)
);
INSERT INTO chapterprogress VALUES (0,0,0,0,'CPS510_CH1.1','mg_the_goat');
SELECT ReadingsDone,VideosDone,ProblemSetsDone,EndofChapterQuizDone,ChapterID,UserID from chapterprogress;

CREATE TABLE quizquestion(
    QuestionID VARCHAR2(25) NOT NULL PRIMARY KEY,
    Question VARCHAR2(4000) NOT NULL,
    QuestionNumber NUMBER(4) NOT NULL CHECK(QuestionNumber > 0),
    QuizID VARCHAR2(25) REFERENCES quiz(QuizID)
);
INSERT INTO quizquestion VALUES ('CPS510_CH1.1_Qu1_Q1','How do you insert information into a table',1,'CPS510_CH1.1_Qu1');
SELECT QuestionID,Question,QuestionNumber,QuizID from quizquestion;

CREATE TABLE QinQuiz(
    QuizID VARCHAR2(25) REFERENCES quiz(QuizID),
    QuestionID VARCHAR2(25) REFERENCES quizquestion(QuestionID)
);
INSERT INTO QinQuiz VALUES ('CPS510_CH1.1_Qu1','CPS510_CH1.1_Qu1_Q1');
SELECT QuizID,QuestionID from QinQuiz;

CREATE TABLE Quizanswer(
    QuestionID VARCHAR2(25) REFERENCES quizquestion(QuestionID),
    Answer VARCHAR2(4000) NOT NULL,
    Correct NUMBER(1) DEFAULT 0 NOT NULL CHECK(Correct BETWEEN 0 AND 1)
);
INSERT INTO Quizanswer VALUES ('CPS510_CH1.1_Qu1_Q1','insert',1);
SELECT QuestionID,Answer,Correct from Quizanswer;