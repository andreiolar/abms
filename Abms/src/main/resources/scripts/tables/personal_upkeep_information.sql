CREATE TABLE IF NOT EXISTS PERSONAL_UPKEEP_INFORMATION(
	aptNumber varchar(4) NOT NULL,
    spatiuComun varchar(5) NOT NULL,
    suprafataApt varchar(5) NOT NULL,
    incalzire varchar(10) NOT NULL,
    apaCaldaMenajera varchar(10) NOT NULL,
    apaReceSiCanalizare varchar(10) NOT NULL,
    numarPersoane varchar(2) NOT NULL,
    gunoi varchar(10) NOT NULL,
    curent varchar(10) NOT NULL,
    gaz varchar(10) NOT NULL,
    servicii varchar(10) NOT NULL,
    gospodaresti varchar(10) NOT NULL,
    nume varchar(50) NOT NULL,
    costTotal varchar(12) NOT NULL,
    luna varchar(25) NOT NULL
);