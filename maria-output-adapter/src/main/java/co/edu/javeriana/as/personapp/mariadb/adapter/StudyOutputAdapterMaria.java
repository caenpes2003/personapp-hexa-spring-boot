package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.EstudiosRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.PersonaRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfesionRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMaria")
@Transactional
public class StudyOutputAdapterMaria implements StudyOutputPort {

	@Autowired
	private EstudiosRepositoryMaria estudiosRepositoryMaria;

	@Autowired
	private PersonaRepositoryMaria personaRepositoryMaria;

	@Autowired
	private ProfesionRepositoryMaria profesionRepositoryMaria;

	@Autowired
	private EstudiosMapperMaria estudiosMapperMaria;

	@Override
	public Study save(Study study) {
		log.debug("Into save on Adapter MariaDB");
		Integer personCc = study.getPerson().getIdentification();
		Integer professionId = study.getProfession().getIdentification();

		Optional<PersonaEntity> persona = personaRepositoryMaria.findById(personCc);
		Optional<ProfesionEntity> profesion = profesionRepositoryMaria.findById(professionId);
		if (persona.isEmpty() || profesion.isEmpty()) {
			log.warn("Persona {} o Profesion {} no encontradas, estudio no persistido", personCc, professionId);
			return study;
		}

		EstudiosEntity entity = new EstudiosEntity();
		entity.setEstudiosPK(new EstudiosEntityPK(professionId, personCc));
		entity.setPersona(persona.get());
		entity.setProfesion(profesion.get());
		entity.setUniver(study.getUniversityName());
		if (study.getGraduationDate() != null) {
			entity.setFecha(java.util.Date.from(
					study.getGraduationDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		}
		EstudiosEntity persisted = estudiosRepositoryMaria.save(entity);
		return estudiosMapperMaria.fromAdapterToDomain(persisted);
	}

	@Override
	public Boolean delete(Integer personCc, Integer professionId) {
		log.debug("Into delete on Adapter MariaDB");
		EstudiosEntityPK pk = new EstudiosEntityPK(professionId, personCc);
		estudiosRepositoryMaria.deleteById(pk);
		return estudiosRepositoryMaria.findById(pk).isEmpty();
	}

	@Override
	public List<Study> find() {
		log.debug("Into find on Adapter MariaDB");
		return estudiosRepositoryMaria.findAll().stream()
				.map(estudiosMapperMaria::fromAdapterToDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Study findById(Integer personCc, Integer professionId) {
		log.debug("Into findById on Adapter MariaDB");
		EstudiosEntityPK pk = new EstudiosEntityPK(professionId, personCc);
		return estudiosRepositoryMaria.findById(pk)
				.map(estudiosMapperMaria::fromAdapterToDomain)
				.orElse(null);
	}
}
