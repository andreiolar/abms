CREATE TABLE IF NOT EXISTS CONVERSATION_FILTER(
	id int(11) NOT NULL REFERENCES conversation(id),
    filter varchar(20) NOT NULL DEFAULT 'Inbox',
    username varchar(25) NOT NULL
);