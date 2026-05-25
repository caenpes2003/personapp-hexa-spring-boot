package co.edu.javeriana.as.personapp.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;

@Mapper
public class EstudioMapperRest {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

	public EstudioResponse fromDomainToAdapterRestMaria(Study study) {
		return fromDomainToAdapterRest(study, "MariaDB");
	}

	public EstudioResponse fromDomainToAdapterRestMongo(Study study) {
		return fromDomainToAdapterRest(study, "MongoDB");
	}

	public EstudioResponse fromDomainToAdapterRest(Study study, String database) {
		String personCc = "";
		String personName = "";
		String professionId = "";
		String professionName = "";
		if (study.getPerson() != null) {
			if (study.getPerson().getIdentification() != null) {
				personCc = study.getPerson().getIdentification() + "";
			}
			if (study.getPerson().getFirstName() != null) {
				personName = study.getPerson().getFirstName();
			}
		}
		if (study.getProfession() != null) {
			if (study.getProfession().getIdentification() != null) {
				professionId = study.getProfession().getIdentification() + "";
			}
			if (study.getProfession().getName() != null) {
				professionName = study.getProfession().getName();
			}
		}
		return new EstudioResponse(
				personCc,
				personName,
				professionId,
				professionName,
				study.getGraduationDate() != null ? study.getGraduationDate().format(DATE_FORMAT) : "",
				study.getUniversityName() != null ? study.getUniversityName() : "",
				database,
				"OK");
	}

	public Study fromAdapterToDomain(EstudioRequest request) {
		Study study = new Study();
		Person person = new Person();
		person.setIdentification(Integer.parseInt(request.getPersonCc()));
		study.setPerson(person);
		Profession profession = new Profession();
		profession.setIdentification(Integer.parseInt(request.getProfessionId()));
		study.setProfession(profession);
		if (request.getGraduationDate() != null && !request.getGraduationDate().isBlank()) {
			study.setGraduationDate(LocalDate.parse(request.getGraduationDate(), DATE_FORMAT));
		}
		study.setUniversityName(request.getUniversityName() != null ? request.getUniversityName() : "");
		return study;
	}
}
