CREATE TABLE IF NOT EXISTS VOTE_DESCRIPTIONS(
	vote_id int(11) PRIMARY KEY AUTO_INCREMENT,
    title varchar(50) NOT NULL,
    description varchar(500) NOT NULL,
    active boolean NOT NULL
);