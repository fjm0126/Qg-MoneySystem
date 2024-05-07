CREATE TABLE UserEnterpriseGroup (
    id INT AUTO_INCREMENT PRIMARY KEY,
     user_name VARCHAR(255),
     enterprise_group_name VARCHAR(255),
     role VARCHAR(255),
     personal_fund DECIMAL(10,2) DEFAULT 0
);
