package co.edu.javeriana.as.personapp.terminal.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterCli {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperCli personaMapperCli;

	PersonInputPort personInputPort;

	public void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Listando Personas");
		personInputPort.findAll().stream()
				.map(personaMapperCli::fromDomainToAdapterCli)
				.forEach(System.out::println);
	}

	public void buscarUno(Integer cc) {
		try {
			Person p = personInputPort.findOne(cc);
			System.out.println(personaMapperCli.fromDomainToAdapterCli(p));
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void crear(Integer cc, String nombre, String apellido, String generoStr, Integer edad) {
		Person p = new Person();
		p.setIdentification(cc);
		p.setFirstName(nombre);
		p.setLastName(apellido);
		p.setGender(parseGender(generoStr));
		p.setAge(edad);
		Person creada = personInputPort.create(p);
		System.out.println("Persona creada:");
		System.out.println(personaMapperCli.fromDomainToAdapterCli(creada));
	}

	public void editar(Integer cc, String nombre, String apellido, String generoStr, Integer edad) {
		try {
			Person p = new Person();
			p.setIdentification(cc);
			p.setFirstName(nombre);
			p.setLastName(apellido);
			p.setGender(parseGender(generoStr));
			p.setAge(edad);
			Person editada = personInputPort.edit(cc, p);
			System.out.println("Persona editada:");
			System.out.println(personaMapperCli.fromDomainToAdapterCli(editada));
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void eliminar(Integer cc) {
		try {
			Boolean borrada = personInputPort.drop(cc);
			System.out.println("Persona eliminada: " + borrada);
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void contar() {
		System.out.println("Total de personas: " + personInputPort.count());
	}

	private Gender parseGender(String sex) {
		if (sex == null) {
			return Gender.OTHER;
		}
		switch (sex.trim().toUpperCase()) {
			case "M":
			case "MALE":
				return Gender.MALE;
			case "F":
			case "FEMALE":
				return Gender.FEMALE;
			default:
				return Gender.OTHER;
		}
	}
}
