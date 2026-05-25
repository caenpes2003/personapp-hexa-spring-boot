package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.EstudioMapperRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudioInputAdapterRest {

	@Autowired
	@Qualifier("studyOutputAdapterMaria")
	private StudyOutputPort studyOutputPortMaria;

	@Autowired
	@Qualifier("studyOutputAdapterMongo")
	private StudyOutputPort studyOutputPortMongo;

	@Autowired
	private EstudioMapperRest estudioMapperRest;

	StudyInputPort studyInputPort;

	private String setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMongo);
			return DatabaseOption.MONGO.toString();
		}
		throw new InvalidOptionException("Invalid database option: " + dbOption);
	}

	private EstudioResponse buildResponse(Study study, String selectedDb) {
		if (selectedDb.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			return estudioMapperRest.fromDomainToAdapterRestMaria(study);
		}
		return estudioMapperRest.fromDomainToAdapterRestMongo(study);
	}

	public List<EstudioResponse> historial(String database) {
		try {
			String selectedDb = setStudyOutputPortInjection(database);
			return studyInputPort.findAll().stream()
					.map(s -> buildResponse(s, selectedDb))
					.collect(Collectors.toList());
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<>();
		}
	}

	public EstudioResponse buscarUno(String database, Integer personCc, Integer professionId) {
		try {
			String selectedDb = setStudyOutputPortInjection(database);
			Study study = studyInputPort.findOne(personCc, professionId);
			return buildResponse(study, selectedDb);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public EstudioResponse crear(EstudioRequest request) {
		try {
			String selectedDb = setStudyOutputPortInjection(request.getDatabase());
			Study study = studyInputPort.create(estudioMapperRest.fromAdapterToDomain(request));
			return buildResponse(study, selectedDb);
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public EstudioResponse editar(Integer personCc, Integer professionId, EstudioRequest request) {
		try {
			String selectedDb = setStudyOutputPortInjection(request.getDatabase());
			Study study = studyInputPort.edit(personCc, professionId,
					estudioMapperRest.fromAdapterToDomain(request));
			return buildResponse(study, selectedDb);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public Boolean eliminar(String database, Integer personCc, Integer professionId) {
		try {
			setStudyOutputPortInjection(database);
			return studyInputPort.drop(personCc, professionId);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return false;
		}
	}

	public Integer contar(String database) {
		try {
			setStudyOutputPortInjection(database);
			return studyInputPort.count();
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return 0;
		}
	}
}
