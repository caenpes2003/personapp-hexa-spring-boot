package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoWriteException;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.repository.PersonaRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.repository.ProfesionRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMongo")
public class StudyOutputAdapterMongo implements StudyOutputPort {

	@Autowired
	private EstudiosRepositoryMongo estudiosRepositoryMongo;

	@Autowired
	private PersonaRepositoryMongo personaRepositoryMongo;

	@Autowired
	private ProfesionRepositoryMongo profesionRepositoryMongo;

	@Autowired
	private EstudiosMapperMongo estudiosMapperMongo;

	private String buildId(Integer personCc, Integer professionId) {
		return personCc + "-" + professionId;
	}

	@Override
	public Study save(Study study) {
		log.debug("Into save on Adapter MongoDB");
		try {
			Integer personCc = study.getPerson().getIdentification();
			Integer professionId = study.getProfession().getIdentification();
			Optional<PersonaDocument> persona = personaRepositoryMongo.findById(personCc);
			Optional<ProfesionDocument> profesion = profesionRepositoryMongo.findById(professionId);
			if (persona.isEmpty() || profesion.isEmpty()) {
				log.warn("Persona {} o Profesion {} no encontradas, estudio no persistido", personCc, professionId);
				return study;
			}
			EstudiosDocument document = new EstudiosDocument();
			document.setId(buildId(personCc, professionId));
			document.setPrimaryPersona(persona.get());
			document.setPrimaryProfesion(profesion.get());
			document.setFecha(study.getGraduationDate());
			document.setUniver(study.getUniversityName());
			EstudiosDocument persisted = estudiosRepositoryMongo.save(document);
			return estudiosMapperMongo.fromAdapterToDomain(persisted);
		} catch (MongoWriteException e) {
			log.warn(e.getMessage());
			return study;
		}
	}

	@Override
	public Boolean delete(Integer personCc, Integer professionId) {
		log.debug("Into delete on Adapter MongoDB");
		String id = buildId(personCc, professionId);
		estudiosRepositoryMongo.deleteById(id);
		return estudiosRepositoryMongo.findById(id).isEmpty();
	}

	@Override
	public List<Study> find() {
		log.debug("Into find on Adapter MongoDB");
		return estudiosRepositoryMongo.findAll().stream()
				.map(estudiosMapperMongo::fromAdapterToDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Study findById(Integer personCc, Integer professionId) {
		log.debug("Into findById on Adapter MongoDB");
		String id = buildId(personCc, professionId);
		return estudiosRepositoryMongo.findById(id)
				.map(estudiosMapperMongo::fromAdapterToDomain)
				.orElse(null);
	}
}
