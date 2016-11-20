CREATE TABLE IF NOT EXISTS SELF_READINGS(
	id int(11) PRIMARY KEY AUTO_INCREMENT,
	apartment_number varchar(4) NOT NULL,
    cold_water varchar(10) NOT NULL,
    hot_water varchar(10) NOT NULL,
    electricity varchar(20) NOT NULL,
    gaz varchar(10) NOT NULL,
    month varchar(20) NOT NULL
);