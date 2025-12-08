/* create a database/schema named db_batch_custom in mysql and run the below scripts to get ready with data*/

create table employee( empid int primary key, empname varchar(25), email varchar(40), jobLoc varchar(25));
insert into employee values (101,'ram','ram@infy.com','chennai');
insert into employee values(102,'shiv','shiv@infy.com','pune');
insert into employee values(103,'prem','prem@infy.com','chennai');
insert into employee values(104,'ria','ria@infy.com','bangalore');
insert into employee values(105,'dia','dia@infy.com','pune');
