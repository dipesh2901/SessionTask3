create database student;
use student;
create table if not exists student_info(
rno int auto_increment primary key not null,
name varchar(20) not null,
city varchar(30) not null
);
desc student_info;
insert into student_info values(1,'gautam','jalgaon'),
(2,'balaji','parbhani'),
(3,'rushin','jalgaon');
select* from student_info;

create table marks(
rno int,
sub_code int,
marks int);
insert into marks values(1,100,80),
(2,100,null),(1,101,90),(2,101,78),(3,100,30),(3,101,null);
select* from marks;
create table subject(
sub_code int,
sub_name varchar(20));
desc subject;
insert into subject values(100,'dbms'),(101,'SE');
select* from subject;
-- 1st query
select rno,avg(marks) as Average_marks from marks group by rno;
-- 2nd query
select sub_name,marks,count(sub_name) as NumberOfStudentFailed from subject right join marks using(sub_code) where sub_name='dbms' and marks<40;

use student;
select* from student_info;
-- 3rd query
select name from student_info inner join marks using(rno) where marks is null;
select* from marks;

-- 4th Query
select name,city from student_info where city in ('pune','jalgaon');

-- 5th Query
select rno,sum(marks) as totalMarks from marks group by rno;

-- 6th Query
select name from student_info where name like 'ba%';

select count(rno) from marks where marks>75 ;

-- query 7
select count(distinct rno) as totalStudents from marks where marks>75;

-- query 9
select count(*) as TotalStudents from marks where marks is not null;

