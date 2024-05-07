CREATE TABLE EnterpriseFinancialFlow (
     id INT AUTO_INCREMENT PRIMARY KEY,
    enterprise_name VARCHAR(255),
    amount DECIMAL(10,2),
    type VARCHAR(255),
    time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
