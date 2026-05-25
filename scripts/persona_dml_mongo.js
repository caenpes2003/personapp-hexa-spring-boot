db.getSiblingDB("persona_db").persona.insertMany([
	{
		"_id": NumberInt(123456789),
		"nombre": "Pepe",
		"apellido": "Perez",
		"genero": "M",
		"edad": NumberInt(30),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(987654321),
		"nombre": "Pepito",
		"apellido": "Perez",
		"genero": "M",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(321654987),
		"nombre": "Pepa",
		"apellido": "Juarez",
		"genero": "F",
		"edad": NumberInt(30),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(147258369),
		"nombre": "Pepita",
		"apellido": "Juarez",
		"genero": "F",
		"edad": NumberInt(10),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(963852741),
		"nombre": "Fede",
		"apellido": "Perez",
		"genero": "M",
		"edad": NumberInt(18),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	}
], { ordered: false });

db.getSiblingDB("persona_db").profesion.insertMany([
	{
		"_id": NumberInt(100),
		"nom": "Ingeniero de Sistemas",
		"des": "Diseno y construccion de software",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
	},
	{
		"_id": NumberInt(200),
		"nom": "Medico",
		"des": "Profesional de la salud",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
	},
	{
		"_id": NumberInt(300),
		"nom": "Abogado",
		"des": "Profesional del derecho",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
	}
], { ordered: false });

db.getSiblingDB("persona_db").telefono.insertMany([
	{
		"_id": "3001112233",
		"oper": "Claro",
		"primaryDuenio": NumberInt(123456789),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
	},
	{
		"_id": "3014445566",
		"oper": "Movistar",
		"primaryDuenio": NumberInt(987654321),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
	},
	{
		"_id": "3157778899",
		"oper": "Tigo",
		"primaryDuenio": NumberInt(321654987),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
	}
], { ordered: false });

db.getSiblingDB("persona_db").estudios.insertMany([
	{
		"_id": "123456789-100",
		"primaryPersona": NumberInt(123456789),
		"primaryProfesion": NumberInt(100),
		"fecha": ISODate("2018-12-10T00:00:00Z"),
		"univer": "Pontificia Universidad Javeriana",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
	},
	{
		"_id": "321654987-200",
		"primaryPersona": NumberInt(321654987),
		"primaryProfesion": NumberInt(200),
		"fecha": ISODate("2015-07-20T00:00:00Z"),
		"univer": "Universidad Nacional",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
	},
	{
		"_id": "963852741-300",
		"primaryPersona": NumberInt(963852741),
		"primaryProfesion": NumberInt(300),
		"fecha": ISODate("2020-11-05T00:00:00Z"),
		"univer": "Universidad de los Andes",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
	}
], { ordered: false });
