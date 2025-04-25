DROP DATABASE IF EXISTS Pokemon_db; 
CREATE DATABASE Pokemon_db; 
USE Pokemon_db; 

CREATE TABLE pokemon ( 

pok_id INT auto_increment PRIMARY KEY, 
pok_name VARCHAR(58) NOT NULL 

);

CREATE TABLE base_stats ( 

pok_id INT, 
base_hp INT NOT NULL, 
base_atk INT NOT NULL, 
b_def INT NOT NULL,
b_sp_atk INT NOT NULL, 
b_sp_def INT NOT NULL,
b_speed INT NOT NULL, 
CONSTRAINT pok_fk FOREIGN KEY (pok_id) references pokemon (pok_id) ON UPDATE CASCADE
);

CREATE TABLE types ( 

type_id INT auto_increment PRIMARY KEY, 
type_name VARCHAR(58) NOT NULL
);

CREATE TABLE pokemon_types ( 

pok_id INT NOT NULl, 
type_id INT NOT NULL, 
slot INT NOT NULL,
CONSTRAINT pok_frk FOREIGN KEY (pok_id) references pokemon (pok_id) ON UPDATE CASCADE
);

CREATE TABLE pokemon_evolution ( 

evo_id INT auto_increment PRIMARY KEY,
evo_min_level INT NOT NULL
-- include fk for another table

);

CREATE TABLE abilities ( 

ability_id INT auto_increment PRIMARY KEY, 
ability_name VARCHAR(58) NOT NULL
); 

CREATE TABLE pokemon_abilities (

pok_id INT NOT NULL, 
abil_id INT NOT NULL, 
slot INT NOT NULL,
CONSTRAINT pok_id FOREIGN KEY (pok_id) references pokemon (pok_id) ON UPDATE CASCADE,
CONSTRAINT abil_id FOREIGN KEY (abil_id) references abilities (ability_id) ON UPDATE CASCADE
); 

CREATE TABLE pokemon_evo_match ( 

pok_id INT NOT NULL, 
gender_rate INT NOT NULL, 
capture_rate INT NOT NULL, 
base_happiness INT NOT NULL,
CONSTRAINT pok_f FOREIGN KEY (pok_id) references pokemon (pok_id) ON UPDATE CASCADE 
);

CREATE TABLE moves( 

move_id INT auto_increment PRIMARY KEY, 
move_name VARCHAR(58) NOT NULL, 
type_id INT NOT NULL, 
move_power SMALLINT NOT NULL, 
move_pp SMALLINT NOT NULL, 
move_accuracy SMALLINT NOT NULL,
CONSTRAINT type_id FOREIGN KEY (type_id) references types (type_id) ON UPDATE CASCADE
);

CREATE TABLE pokemon_moves ( 

pok_id INT NOT NULL, 
version_group_id INT NOT NULL,  
move_id INT NOT NULL, 
level INT NOT NULL,
CONSTRAINT pok_fork FOREIGN KEY (pok_id) references pokemon(pok_id) ON UPDATE CASCADE

)