package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;

@Mapper
public class EstudiosMapperMaria {

	@Autowired
	private PersonaMapperMaria personaMapperMaria;

	@Autowired
	private ProfesionMapperMaria profesionMapperMaria;

	public EstudiosEntity fromDomainToAdapter(Study study) {
		EstudiosEntityPK estudioPK = new EstudiosEntityPK();
		estudioPK.setCcPer(study.getPerson().getIdentification());
		estudioPK.setIdProf(study.getProfession().getIdentification());
		EstudiosEntity estudio = new EstudiosEntity();
		estudio.setEstudiosPK(estudioPK);
		estudio.setFecha(validateFecha(study.getGraduationDate()));
		estudio.setUniver(validateUniver(study.getUniversityName()));
		return estudio;
	}

	private Date validateFecha(LocalDate graduationDate) {
		return graduationDate != null
				? Date.from(graduationDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
				: null;
	}

	private String validateUniver(String universityName) {
		return universityName != null ? universityName : "";
	}

	public Study fromAdapterToDomain(EstudiosEntity estudiosEntity) {
		Study study = new Study();
		study.setPerson(buildPersonStub(estudiosEntity));
		study.setProfession(buildProfessionStub(estudiosEntity));
		study.setGraduationDate(validateGraduationDate(estudiosEntity.getFecha()));
		study.setUniversityName(validateUniversityName(estudiosEntity.getUniver()));
		return study;
	}

	private co.edu.javeriana.as.personapp.domain.Person buildPersonStub(EstudiosEntity estudiosEntity) {
		co.edu.javeriana.as.personapp.domain.Person person = new co.edu.javeriana.as.personapp.domain.Person();
		if (estudiosEntity.getPersona() != null) {
			person.setIdentification(estudiosEntity.getPersona().getCc());
			person.setFirstName(estudiosEntity.getPersona().getNombre());
			person.setLastName(estudiosEntity.getPersona().getApellido());
		} else if (estudiosEntity.getEstudiosPK() != null) {
			person.setIdentification(estudiosEntity.getEstudiosPK().getCcPer());
		}
		return person;
	}

	private co.edu.javeriana.as.personapp.domain.Profession buildProfessionStub(EstudiosEntity estudiosEntity) {
		co.edu.javeriana.as.personapp.domain.Profession profession = new co.edu.javeriana.as.personapp.domain.Profession();
		if (estudiosEntity.getProfesion() != null) {
			profession.setIdentification(estudiosEntity.getProfesion().getId());
			profession.setName(estudiosEntity.getProfesion().getNom());
			profession.setDescription(estudiosEntity.getProfesion().getDes());
		} else if (estudiosEntity.getEstudiosPK() != null) {
			profession.setIdentification(estudiosEntity.getEstudiosPK().getIdProf());
		}
		return profession;
	}

	private LocalDate validateGraduationDate(Date fecha) {
		if (fecha == null) {
			return null;
		}
		if (fecha instanceof java.sql.Date) {
			return ((java.sql.Date) fecha).toLocalDate();
		}
		return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	private String validateUniversityName(String univer) {
		return univer != null ? univer : "";
	}
}