CREATE TABLE IF NOT EXISTS DELETED_CONVERSATIONS(
	id int(11) NOT NULL REFERENCES conversation(id),
    deleted boolean NOT NULL DEFAULT false,
    username varchar(25) NOT NULL
);