CREATE TABLE IF NOT EXISTS COMPLAINTS(
	id int(11) PRIMARY KEY AUTO_INCREMENT,
    username varchar(25) NOT NULL,
    complaint_to varchar(40) NOT NULL,
    date date NOT NULL,
    phone_number varchar(12) NOT NULL
);