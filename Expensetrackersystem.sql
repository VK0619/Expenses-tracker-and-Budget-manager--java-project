CREATE DATABASE expensetracker;
USE expensetracker;
CREATE TABLE  users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
     description VARCHAR(50)UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL

);
CREATE TABLE category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) UNIQUE NOT NULL
    
    );
CREATE TABLE  expense (
    expense_id INT AUTO_INCREMENT PRIMARY KEY,
    -- user_id INT,
    category_id INT,
    date DATE NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    description VARCHAR(255)
--     FOREIGN KEY (user_id) REFERENCES user(user_id),
--     FOREIGN KEY (category_id) REFERENCES category(category_id)
);
CREATE TABLE income (
    income_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    date DATE NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    description VARCHAR(255)
--     FOREIGN KEY (user_id) REFERENCES user(user_id)
   
);
CREATE TABLE budget (
    budget_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    category_name VARCHAR(50) ,
    monthly_limit DECIMAL(10, 2) NOT NULL,
--     FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (category_name) REFERENCES category(category_name)
);
CREATE TABLE  receipt (
    receipt_id INT AUTO_INCREMENT PRIMARY KEY,
    expense_id INT,
    file_path VARCHAR(255) NOT NULL
   --  FOREIGN KEY (expense_id) REFERENCES expense(expense_id)
);
CREATE TABLE reminder (
    reminder_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    description VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    interval_days INT
    -- FOREIGN KEY (user_id) REFERENCES users(user_id)
);
CREATE TABLE report (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
	category_id INT,
    report_type VARCHAR(50) NOT NULL,
    report_data TEXT,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
--     FOREIGN KEY (user_id) REFERENCES user(user_id),
 -- FOREIGN KEY (category_id) REFERENCES category(category_id)
);
drop database expensetracker;
select *from users;
select *from receipt ;
drop table expense;
select *from category ;
select *from expense ;
select *from receipt ;
select *from income ;
select *from budget ;
select *from report ;
select *from reminder ;
drop table budget;
drop table report;
drop table reminder;
drop table receipt;
