INSERT INTO
	`persona_db`.`persona`(`cc`,`nombre`,`apellido`,`genero`,`edad`)
VALUES
	(123456789,'Pepe','Perez','M',30),
	(987654321,'Pepito','Perez','M',null),
	(321654987,'Pepa','Juarez','F',30),
	(147258369,'Pepita','Juarez','F',10),
	(963852741,'Fede','Perez','M',18);

INSERT INTO
	`persona_db`.`profesion`(`id`,`nom`,`des`)
VALUES
	(100,'Ingeniero de Sistemas','Diseno y construccion de software'),
	(200,'Medico','Profesional de la salud'),
	(300,'Abogado','Profesional del derecho');

INSERT INTO
	`persona_db`.`telefono`(`num`,`oper`,`duenio`)
VALUES
	('3001112233','Claro',123456789),
	('3014445566','Movistar',987654321),
	('3157778899','Tigo',321654987);

INSERT INTO
	`persona_db`.`estudios`(`id_prof`,`cc_per`,`fecha`,`univer`)
VALUES
	(100,123456789,'2018-12-10','Pontificia Universidad Javeriana'),
	(200,321654987,'2015-07-20','Universidad Nacional'),
	(300,963852741,'2020-11-05','Universidad de los Andes');
