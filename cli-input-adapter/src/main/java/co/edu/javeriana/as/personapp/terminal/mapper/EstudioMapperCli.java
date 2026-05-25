package co.edu.javeriana.as.personapp.terminal.mapper;

import java.time.format.DateTimeFormatter;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.EstudioModelCli;

@Mapper
public class EstudioMapperCli {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

	public EstudioModelCli fromDomainToAdapterCli(Study study) {
		EstudioModelCli model = new EstudioModelCli();
		if (study.getPerson() != null) {
			model.setPersonaCc(study.getPerson().getIdentification());
			model.setPersonaNombre(study.getPerson().getFirstName());
		}
		if (study.getProfession() != null) {
			model.setProfesionId(study.getProfession().getIdentification());
			model.setProfesionNombre(study.getProfession().getName());
		}
		model.setFechaGraduacion(study.getGraduationDate() != null
				? study.getGraduationDate().format(DATE_FORMAT) : "");
		model.setUniversidad(study.getUniversityName() != null ? study.getUniversityName() : "");
		return model;
	}
}
