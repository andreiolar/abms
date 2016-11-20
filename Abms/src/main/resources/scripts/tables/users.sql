CREATE TABLE IF NOT EXISTS USERS(
	id INT(10) PRIMARY KEY AUTO_INCREMENT,
	username varchar(25) UNIQUE NOT NULL,
	password varchar(500) NOT NULL,
	type varchar(25) NOT NULL
);