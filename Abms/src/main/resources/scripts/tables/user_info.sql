CREATE TABLE IF NOT EXISTS USER_INFO(
	id int(11) PRIMARY KEY AUTO_INCREMENT,
    first_name varchar(30) NOT NULL,
    last_name varchar(30) NOT NULL,
    date_of_birth date NOT NULL,
    email varchar(50) NOT NULL,
    mobile_number varchar(11) NOT NULL,
    gender varchar(7) NOT NULL,
    address varchar(200) NOT NULL,
    city varchar(30) NOT NULL,
    country varchar(30) NOT NULL,
    personal_number varchar(14) NOT NULL,
    id_series varchar(9) NOT NULL,
    username varchar(25) NOT NULL,
    apartment_number varchar(3)
);