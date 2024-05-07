CREATE TABLE userFinancialFlow (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255),
    amount DECIMAL(10,2),
    type ENUM('充值', '转账支出','转账收入'),
    object VARCHAR(255),
    time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

