package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.DuplicateException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mapper.ProfesionMapperRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterRest {

	@Autowired
	@Qualifier("professionOutputAdapterMaria")
	private ProfessionOutputPort professionOutputPortMaria;

	@Autowired
	@Qualifier("professionOutputAdapterMongo")
	private ProfessionOutputPort professionOutputPortMongo;

	@Autowired
	private ProfesionMapperRest profesionMapperRest;

	ProfessionInputPort professionInputPort;

	private String setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
			return DatabaseOption.MONGO.toString();
		}
		throw new InvalidOptionException("Invalid database option: " + dbOption);
	}

	private ProfesionResponse buildResponse(Profession profession, String selectedDb) {
		if (selectedDb.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			return profesionMapperRest.fromDomainToAdapterRestMaria(profession);
		}
		return profesionMapperRest.fromDomainToAdapterRestMongo(profession);
	}

	public List<ProfesionResponse> historial(String database) throws InvalidOptionException {
		String selectedDb = setProfessionOutputPortInjection(database);
		return professionInputPort.findAll().stream()
				.map(p -> buildResponse(p, selectedDb))
				.collect(Collectors.toList());
	}

	public ProfesionResponse buscarUna(String database, Integer id)
			throws InvalidOptionException, NoExistException {
		String selectedDb = setProfessionOutputPortInjection(database);
		Profession profession = professionInputPort.findOne(id);
		return buildResponse(profession, selectedDb);
	}

	public ProfesionResponse crear(ProfesionRequest request)
			throws InvalidOptionException, DuplicateException {
		String selectedDb = setProfessionOutputPortInjection(request.getDatabase());
		Profession profession = professionInputPort.create(profesionMapperRest.fromAdapterToDomain(request));
		return buildResponse(profession, selectedDb);
	}

	public ProfesionResponse editar(Integer id, ProfesionRequest request)
			throws InvalidOptionException, NoExistException {
		String selectedDb = setProfessionOutputPortInjection(request.getDatabase());
		Profession profession = professionInputPort.edit(id, profesionMapperRest.fromAdapterToDomain(request));
		return buildResponse(profession, selectedDb);
	}

	public Boolean eliminar(String database, Integer id)
			throws InvalidOptionException, NoExistException {
		setProfessionOutputPortInjection(database);
		return professionInputPort.drop(id);
	}

	public Integer contar(String database) throws InvalidOptionException {
		setProfessionOutputPortInjection(database);
		return professionInputPort.count();
	}
}
