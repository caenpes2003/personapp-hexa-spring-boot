package co.edu.javeriana.as.personapp.terminal.adapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.mapper.EstudioMapperCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudioInputAdapterCli {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

	@Autowired
	@Qualifier("studyOutputAdapterMaria")
	private StudyOutputPort studyOutputPortMaria;

	@Autowired
	@Qualifier("studyOutputAdapterMongo")
	private StudyOutputPort studyOutputPortMongo;

	@Autowired
	private EstudioMapperCli estudioMapperCli;

	StudyInputPort studyInputPort;

	public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Listando Estudios");
		studyInputPort.findAll().stream()
				.map(estudioMapperCli::fromDomainToAdapterCli)
				.forEach(System.out::println);
	}

	public void buscarUno(Integer personCc, Integer professionId) {
		try {
			Study s = studyInputPort.findOne(personCc, professionId);
			System.out.println(estudioMapperCli.fromDomainToAdapterCli(s));
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void crear(Integer personCc, Integer professionId, String fechaIso, String universidad) {
		Study s = buildStudy(personCc, professionId, fechaIso, universidad);
		Study creado = studyInputPort.create(s);
		System.out.println("Estudio creado:");
		System.out.println(estudioMapperCli.fromDomainToAdapterCli(creado));
	}

	public void editar(Integer personCc, Integer professionId, String fechaIso, String universidad) {
		try {
			Study s = buildStudy(personCc, professionId, fechaIso, universidad);
			Study editado = studyInputPort.edit(personCc, professionId, s);
			System.out.println("Estudio editado:");
			System.out.println(estudioMapperCli.fromDomainToAdapterCli(editado));
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void eliminar(Integer personCc, Integer professionId) {
		try {
			Boolean borrado = studyInputPort.drop(personCc, professionId);
			System.out.println("Estudio eliminado: " + borrado);
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void contar() {
		System.out.println("Total de estudios: " + studyInputPort.count());
	}

	private Study buildStudy(Integer personCc, Integer professionId, String fechaIso, String universidad) {
		Study s = new Study();
		Person person = new Person();
		person.setIdentification(personCc);
		s.setPerson(person);
		Profession profession = new Profession();
		profession.setIdentification(professionId);
		s.setProfession(profession);
		if (fechaIso != null && !fechaIso.isBlank()) {
			s.setGraduationDate(LocalDate.parse(fechaIso, DATE_FORMAT));
		}
		s.setUniversityName(universidad != null ? universidad : "");
		return s;
	}
}
