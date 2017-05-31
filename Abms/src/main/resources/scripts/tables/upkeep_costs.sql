create table upkeep_costs(
	id int(11) primary key auto_increment,
    apt_number int(3),
    cost varchar(12),
    month varchar(25),
    status boolean default false
);