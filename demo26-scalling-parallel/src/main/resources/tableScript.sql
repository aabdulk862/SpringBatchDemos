/* create a database/schema named db_batch_custom in mysql and run the below scripts to get ready with table*/

create table employee( empid int primary key, empname varchar(25), email varchar(40), jobLoc varchar(25));

CREATE TABLE claim(
  claimid int primary key,
  claimname varchar(45),
  requestedBy int,
  claimamount int  
);