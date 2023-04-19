 -- create database GraduationProjectManagement;
 use GraduationProjectManagement;

 create table Users(
	id int IDENTITY(1,1) PRIMARY KEY,
	name NVARCHAR(255) NOT NULL,
	username VARCHAR(255) NOT NULL,
	phonenumber VARCHAR(20) NOT NULL,
	password VARCHAR(255) NOT NULL,
 );

/*
SELECT * FROM Users;
Delete  from Users
SELECT * FROM Users WHERE username = 'admin' AND password = 'c4ca4238a0b923820dcc509a6f75849b'
SELECT * FROM Users
*/


create table SchoolYears(
	id int IDENTITY(1,1) PRIMARY KEY,
	name VARCHAR(20) NOT NULL,
)
ALTER TABLE SchoolYears ADD UNIQUE(name);


UPDATE SchoolYears SET name='2020 - 2021' WHERE id='3'
DELETE SchoolYears WHERE id='3'

SELECT * FROM SchoolYears


create table Majors(
	id int IDENTITY(1,1) PRIMARY KEY,
	majorsId VARCHAR(20) NOT NULL ,
	name NVARCHAR(200) NOT NULL,
	description NVARCHAR(200),
)
ALTER TABLE Majors ADD UNIQUE(majorsId)



/*
DELETE Majors WHERE id='7480201'
SELECT * FROM Majors
Delete from Majors
*/

create table Courses(
	id int IDENTITY(1,1) PRIMARY KEY,
	name NVARCHAR(200) NOT NULL,
	description NVARCHAR(200),
	studyTime VARCHAR(20) NOT NULL,
	majors int FOREIGN KEY REFERENCES Majors(id)
)
ALTER TABLE Courses ADD UNIQUE(name)
/*
SELECT * FROM Courses
Delete from Majors
*/
SELECT Courses.id, Courses.name, Courses.description, Majors.name as majors FROM Courses, Majors WHERE Courses.majors = Majors.id

create table Classes(
	id int IDENTITY(1,1) PRIMARY KEY,
	name NVARCHAR(200) NOT NULL,
	description NVARCHAR(200),
	course int FOREIGN KEY REFERENCES Courses(id)
)

ALTER TABLE Classes ADD UNIQUE(name)



SELECT * FROM Classes
SELECT Classes.id, Classes.name, Courses.name as course, Majors.name as majors, Courses.studyTime, Classes.description FROM Classes, Courses, Majors WHERE Classes.course = Courses.id and Courses.majors = Majors.id ORDER BY Courses.studyTime, name

create table Teachers(
	id int IDENTITY(1,1) PRIMARY KEY,
	name NVARCHAR(200) NOT NULL,
	academicRank NVARCHAR(100) NOT NULL,
	majors int FOREIGN KEY REFERENCES Majors(id),
	phonenumber VARCHAR(20) NOT NULL,
	email VARCHAR(200) NOT NULL,
)

ALTER TABLE Teachers ADD UNIQUE(phonenumber, email)
SELECT * FROM Teachers

SELECT Teachers.id, Teachers.name, Teachers.academicRank, Majors.name as majors, Teachers.phonenumber, Teachers.email FROM Teachers, Majors WHERE Teachers.majors = Majors.id ORDER BY Teachers.name

SELECT DISTINCT Majors.name FROM Classes, Courses, Majors WHERE Courses.id = Classes.course and Majors.id = Courses.majors and Classes.name = 'CT3A'
SELECT DISTINCT Majors.name FROM Classes, Courses, Majors WHERE Courses.id = Classes.course and Majors.id = Courses.majors and Courses.name = 'CT3'
SELECT DISTINCT Courses.name FROM Classes, Courses, Majors WHERE Courses.id = Classes.course and Majors.id = Courses.majors and Classes.name = 'CT3A'
SELECT DISTINCT Classes.name FROM Classes, Courses, Majors WHERE Courses.id = Classes.course and Majors.id = Courses.majors and Majors.id = '1'
SELECT DISTINCT Majors.name FROM  Courses, Majors WHERE Majors.id = Courses.majors and Courses.name = 'CT2'

create table Students(
	id VARCHAR(12) PRIMARY KEY,
	name NVARCHAR(100) NOT NULL,
	gender NVARCHAR (10) NOT NULL,
	birthday VARCHAR(100) NOT NULL,
	class int FOREIGN KEY REFERENCES Classes(id),
	phonenumber VARCHAR(20) NOT NULL UNIQUE,
	email VARCHAR(100) NOT NULL UNIQUE,
)

DELETE Classes WHERE id='3'
SELECT * FROM Students
SELECT Students.id, Students.name, Students.gender, Students.birthday, Classes.name as class, Students.phonenumber, Students.email 
FROM Students, Classes WHERE Students.class = Classes.id 
GROUP BY Classes.name, Students.id, Students.name, Students.gender, Students.birthday,Students.phonenumber, Students.email
ORDER BY  Classes.name, Students.id
INSERT INTO Students(id,name,gender,birthday,class,phonenumber,email)VALUES('CT030401',N'Bùi Thị B',N'Nữ','29-02-2000','10','0128679646','bbt@gmail.com')

SELECT DISTINCT Majors.name FROM Majors ORDER BY Majors.name

create table ProjectTopics(
	id VARCHAR(12) PRIMARY KEY,
	name NVARCHAR(100) NOT NULL,
	description NVARCHAR(1000),
	require NVARCHAR(1000),
	teacher int FOREIGN KEY REFERENCES Teachers(id) NOT NULL,
	course int FOREIGN KEY REFERENCES Courses(id) NOT NULL,
	schoolYear int FOREIGN KEY REFERENCES SchoolYears(id) NOT NULL,
	student VARCHAR(12) FOREIGN KEY REFERENCES Students(id),
	topicStatus VARCHAR(3) NOT NULL,
	createdBy int FOREIGN KEY REFERENCES Users(id) NOT NULL,
	createdAt VARCHAR(35) NOT NULL
)

create table Documents(
	id varchar(12) primary key,
	topic varchar(12) FOREIGN KEY REFERENCES ProjectTopics(id) NOT NULL,
	path varchar(200) NOT NULL,
	type varchar(1) NOT NULL,
	createdAt varchar(35) NOT NULL
)

Create table Similar(
	document1 varchar(12) FOREIGN KEY REFERENCES Documents(id) NOT NULL,
	document2 varchar(12) FOREIGN KEY REFERENCES Documents(id) NOT NULL,
	similarPercent varchar(20) NOT NULL,
)

delete from Similar
delete from Documents

select * from Documents
select * from Similar
SELECT * FROM Similar
WHERE (Similar.document1 = '170147'
AND Similar.document2 = '974790')
OR (Similar.document1 = '974790'
AND Similar.document2 = '170147')

SELECT * FROM Similar
WHERE (Similar.document1 = '974790'
AND Similar.document2 = '344016')
OR (Similar.document1 = '344016'
AND Similar.document2 = '974790')

SELECT * FROM Documents WHERE Documents.topic = '001' and Documents.type = '1'
SELECT * FROM Documents WHERE Documents.id ='2486909'
Select * from ProjectTopics

SELECT DISTINCT ProjectTopics.id, 
ProjectTopics.name, 
ProjectTopics.description, 
ProjectTopics.require, 
Students.id as studentId,
Students.name as studentName,	
TeachersInfor.id as teacherId,
TeachersInfor.name as teacherName,
TeachersInfor.academicRank as teacherAcademicRank,
TeachersInfor.majorsName as teacherMajors,
Courses.name as course,
Majors.name as majors,
SchoolYears.name as schoolYear,
ProjectTopics.createdAt,
Users.id as userId,
Users.name as createdBy  
FROM ProjectTopics, Courses, Majors, SchoolYears, Students, Users, (
SELECT Teachers.id, Teachers.name, Teachers.academicRank, Teachers.majors as majorsId, majors.name as majorsName 
FROM Teachers, Majors
WHERE Teachers.majors = Majors.id
) as TeachersInfor
WHERE ProjectTopics.teacher = TeachersInfor.id
and Courses.id = ProjectTopics.course 
and Courses.majors = Majors.id 
AND Students.id = ProjectTopics.student
and SchoolYears.id = ProjectTopics.schoolYear
and Users.id = ProjectTopics.createdBy
ORDER BY ProjectTopics.id ASC



SELECT * FROM ProjectTopics

INSERT INTO ProjectTopics(id,name,description,require,teacher,course,schoolYear,createdBy,createdAt)VALUES('002',N'Tên đề tài',N'Mô tả',N'Yêu Cầu','2','6','5','10','19:19:40.565 27-03-2023')

UPDATE ProjectTopics SET student='CT030428' where id='001'


INSERT INTO ProjectTopics(id,name,description,require,teacher,course,schoolYear,createdBy,createdAt)VALUES('003',N'Tên đề tài
Tên đề tài
Tên đề tài',N'Mô tả
Mô tả',N'Yêu Cầu
Yêu Cầu
Yêu cầu
yêu cầu','4','6','5','10','21:03:07.286 27-03-2023')


SELECT ProjectTopics.id, 
ProjectTopics.name, 
ProjectTopics.description, 
ProjectTopics.require, 
ProjectTopics.student,
TeachersInfor.id as teacherId,
TeachersInfor.name as teacherName,
TeachersInfor.academicRank as teacherAcademicRank,
TeachersInfor.majorsName as teacherMajors,
Courses.name as course,
Majors.name as majors,
SchoolYears.name as schoolYear,
ProjectTopics.createdAt,
Users.id as userId,
Users.name as createdBy  
FROM ProjectTopics, Courses, Majors, SchoolYears, Users, (
SELECT Teachers.id, Teachers.name, Teachers.academicRank, Teachers.majors as majorsId, majors.name as majorsName 
FROM Teachers, Majors
WHERE Teachers.majors = Majors.id
) as TeachersInfor
WHERE ProjectTopics.id = '001' 
and ProjectTopics.teacher = TeachersInfor.id
and Courses.id = ProjectTopics.course 
and Courses.majors = Majors.id 
and SchoolYears.id = ProjectTopics.schoolYear
and Users.id = ProjectTopics.createdBy
ORDER BY ProjectTopics.id ASC

SELECT ProjectTopics.id,
ProjectTopics.name,
ProjectTopics.description,
ProjectTopics.require,
TeachersInfor.id as teacherId,
TeachersInfor.name as teacherName,
TeachersInfor.academicRank as teacherAcademicRank,
TeachersInfor.phonenumber as teacherPhoneNumber,
TeachersInfor.majorsName as teacherMajors,
TeachersInfor.email as teacherEmail,
Courses.id as CourseId,
Courses.name as courseName,
Majors.id as majorsId,
Majors.name as majorsName,
SchoolYears.id as schoolYearId,
SchoolYears.name as schoolYearName
FROM ProjectTopics, Courses, Majors, SchoolYears, (
SELECT Teachers.*, Teachers.majors as majorsId, majors.name as majorsName 
FROM Teachers, Majors
WHERE Teachers.majors = Majors.id
) as TeachersInfor
WHERE ProjectTopics.id='001' 
and ProjectTopics.teacher = TeachersInfor.id
and ProjectTopics.course = Courses.id
and ProjectTopics.schoolYear = SchoolYears.id
and Courses.majors = Majors.id



SELECT Students.id, Students.name, Students.gender, Students.birthday, Students.class, Students.phonenumber, Students.email
FROM(
	SELECT Students.id, Students.name, Students.gender, Students.birthday, Classes.name as class, Students.phonenumber, Students.email 
	FROM Students, Classes 
	WHERE Students.class = Classes.id 
) as Students
LEFT JOIN ProjectTopics On ProjectTopics.student = Students.id
WHERE ProjectTopics.student IS NULL
GROUP BY Students.class, Students.id, Students.name, Students.gender, Students.birthday,Students.phonenumber, Students.email
ORDER BY  Students.class, Students.id

SELECT Students.id, Students.name
From Students
LEFT JOIN ProjectTopics On ProjectTopics.student = Students.id
WHERE ProjectTopics.student IS NULL

--get ProjectTopicWithStudentModel
SELECT ProjectTopics.id,
ProjectTopics.name,
ProjectTopics.description,
ProjectTopics.require,
Students.id as studentId,
Students.name as studentName,
TeachersInfor.id as teacherId,
TeachersInfor.name as teacherName,
TeachersInfor.academicRank as teacherAcademicRank,
TeachersInfor.phonenumber as teacherPhoneNumber,
TeachersInfor.majorsName as teacherMajors,
TeachersInfor.email as teacherEmail,
Courses.id as courseId,
Courses.name as courseName,
Majors.id as majorsId,
Majors.name as majorsName,
SchoolYears.id as schoolYearId,
SchoolYears.name as schoolYearName
FROM ProjectTopics, Courses, Majors, SchoolYears, Students, (
SELECT Teachers.*, Teachers.majors as majorsId, majors.name as majorsName
FROM Teachers, Majors
WHERE Teachers.majors = Majors.id
) as TeachersInfor
WHERE ProjectTopics.id='001'
and ProjectTopics.student = Students.id
and ProjectTopics.teacher = TeachersInfor.id
and ProjectTopics.course = Courses.id
and ProjectTopics.schoolYear = SchoolYears.id
and Courses.majors = Majors.id




/**/
