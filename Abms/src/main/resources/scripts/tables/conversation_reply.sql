CREATE TABLE IF NOT EXISTS CONVERSATION_REPLY(
	id int(11) PRIMARY KEY AUTO_INCREMENT,
    reply text,
    username varchar(25) NOT NULL,
    ip varchar(30) DEFAULT NULL,
    date varchar(50) NOT NULL,
    conv_id_fk int(11) NOT NULL REFERENCES conversation(id)
);