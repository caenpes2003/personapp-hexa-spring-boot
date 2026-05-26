package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.DuplicateException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterRest {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperRest personaMapperRest;

	PersonInputPort personInputPort;

	private String setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
			return  DatabaseOption.MONGO.toString();
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	private PersonaResponse buildResponse(Person person, String selectedDb) {
		if (selectedDb.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			return personaMapperRest.fromDomainToAdapterRestMaria(person);
		}
		return personaMapperRest.fromDomainToAdapterRestMongo(person);
	}

	public List<PersonaResponse> historial(String database) throws InvalidOptionException {
		String selectedDb = setPersonOutputPortInjection(database);
		return personInputPort.findAll().stream()
				.map(p -> buildResponse(p, selectedDb))
				.collect(Collectors.toList());
	}

	public PersonaResponse crearPersona(PersonaRequest request)
			throws InvalidOptionException, DuplicateException {
		String selectedDb = setPersonOutputPortInjection(request.getDatabase());
		Person person = personInputPort.create(personaMapperRest.fromAdapterToDomain(request));
		return buildResponse(person, selectedDb);
	}

	public PersonaResponse buscarUna(String database, Integer cc)
			throws InvalidOptionException, NoExistException {
		String selectedDb = setPersonOutputPortInjection(database);
		Person person = personInputPort.findOne(cc);
		return buildResponse(person, selectedDb);
	}

	public PersonaResponse editar(Integer cc, PersonaRequest request)
			throws InvalidOptionException, NoExistException {
		String selectedDb = setPersonOutputPortInjection(request.getDatabase());
		Person incoming = personaMapperRest.fromAdapterToDomain(request);
		incoming.setIdentification(cc);
		Person person = personInputPort.edit(cc, incoming);
		return buildResponse(person, selectedDb);
	}

	public Boolean eliminar(String database, Integer cc)
			throws InvalidOptionException, NoExistException {
		setPersonOutputPortInjection(database);
		return personInputPort.drop(cc);
	}

	public Integer contar(String database) throws InvalidOptionException {
		setPersonOutputPortInjection(database);
		return personInputPort.count();
	}
}
