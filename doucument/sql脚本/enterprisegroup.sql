CREATE TABLE EnterpriseGroup (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    member_count INT,
    scale VARCHAR(255),
    direction VARCHAR(255),
    creator_name VARCHAR(255),
    enterprise_fund DECIMAL(10,2) DEFAULT 0,
    access_mode ENUM('public', 'private')
);
