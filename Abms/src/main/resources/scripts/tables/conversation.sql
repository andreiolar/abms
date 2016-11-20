CREATE TABLE IF NOT EXISTS CONVERSATION(
	id int(11) PRIMARY KEY AUTO_INCREMENT,
    user_one varchar(25) NOT NULL,
    user_two varchar(25) NOT NULL,
    subject varchar(15) NOT NULL,
    ip varchar(30) DEFAULT NULL,
    date varchar(50) NOT NULL,
);