create table users (
id integer generated by default as identity,
avatar varchar(255),
birth_date varchar(255),
city varchar(255),
company varchar(255),
country varchar(255),
email varchar(255),
first_name varchar(255),
job_position varchar(255),
last_name varchar(255),
mobile varchar(255),
password varchar(255),
role varchar(255),
username varchar(255),
primary key (id)
);