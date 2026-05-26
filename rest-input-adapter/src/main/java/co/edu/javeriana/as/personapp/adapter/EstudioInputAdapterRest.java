package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.DuplicateException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.exceptions.UnprocessableEntityException;
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
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	@Qualifier("professionOutputAdapterMaria")
	private ProfessionOutputPort professionOutputPortMaria;

	@Autowired
	@Qualifier("professionOutputAdapterMongo")
	private ProfessionOutputPort professionOutputPortMongo;

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

	private void validateForeignKeys(String database, Study study) throws UnprocessableEntityException {
		Integer personCc = study.getPerson() != null ? study.getPerson().getIdentification() : null;
		Integer professionId = study.getProfession() != null ? study.getProfession().getIdentification() : null;
		PersonOutputPort personPort;
		ProfessionOutputPort professionPort;
		if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personPort = personOutputPortMaria;
			professionPort = professionOutputPortMaria;
		} else {
			personPort = personOutputPortMongo;
			professionPort = professionOutputPortMongo;
		}
		if (personCc == null || personPort.findById(personCc) == null) {
			throw new UnprocessableEntityException(
					"La persona con cc " + personCc + " no existe en " + database);
		}
		if (professionId == null || professionPort.findById(professionId) == null) {
			throw new UnprocessableEntityException(
					"La profesion con id " + professionId + " no existe en " + database);
		}
	}

	public List<EstudioResponse> historial(String database) throws InvalidOptionException {
		String selectedDb = setStudyOutputPortInjection(database);
		return studyInputPort.findAll().stream()
				.map(s -> buildResponse(s, selectedDb))
				.collect(Collectors.toList());
	}

	public EstudioResponse buscarUno(String database, Integer personCc, Integer professionId)
			throws InvalidOptionException, NoExistException {
		String selectedDb = setStudyOutputPortInjection(database);
		Study study = studyInputPort.findOne(personCc, professionId);
		return buildResponse(study, selectedDb);
	}

	public EstudioResponse crear(EstudioRequest request)
			throws InvalidOptionException, DuplicateException, UnprocessableEntityException {
		String selectedDb = setStudyOutputPortInjection(request.getDatabase());
		Study domain = estudioMapperRest.fromAdapterToDomain(request);
		validateForeignKeys(request.getDatabase(), domain);
		Study study = studyInputPort.create(domain);
		return buildResponse(study, selectedDb);
	}

	public EstudioResponse editar(Integer personCc, Integer professionId, EstudioRequest request)
			throws InvalidOptionException, NoExistException, UnprocessableEntityException {
		String selectedDb = setStudyOutputPortInjection(request.getDatabase());
		Study domain = estudioMapperRest.fromAdapterToDomain(request);
		validateForeignKeys(request.getDatabase(), domain);
		Study study = studyInputPort.edit(personCc, professionId, domain);
		return buildResponse(study, selectedDb);
	}

	public Boolean eliminar(String database, Integer personCc, Integer professionId)
			throws InvalidOptionException, NoExistException {
		setStudyOutputPortInjection(database);
		return studyInputPort.drop(personCc, professionId);
	}

	public Integer contar(String database) throws InvalidOptionException {
		setStudyOutputPortInjection(database);
		return studyInputPort.count();
	}
}
