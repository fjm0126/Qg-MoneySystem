CREATE TABLE User (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    Sdcard VARCHAR(255) UNIQUE,
    nickname VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(20),
    address VARCHAR(255),
    email VARCHAR(255),
	personal_fund DECIMAL(10,2) DEFAULT 0,
    company_name VARCHAR(255) DEFAULT '无',
    avatar VARCHAR(255) DEFAULT 'default_avatar.jpg'
);

