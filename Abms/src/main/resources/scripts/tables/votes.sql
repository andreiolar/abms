CREATE TABLE IF NOT EXISTS VOTES(
    vote_option varchar(50) NOT NULL,
    number_of_votes int(11),
    vote_id int(11) NOT NULL,
    active varchar(6) NOT NULL
);