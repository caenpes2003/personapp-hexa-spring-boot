package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.DuplicateException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class StudyUseCase implements StudyInputPort {

	private StudyOutputPort studyPersintence;

	public StudyUseCase(@Qualifier("studyOutputAdapterMaria") StudyOutputPort studyPersintence) {
		this.studyPersintence = studyPersintence;
	}

	@Override
	public void setPersintence(StudyOutputPort studyPersintence) {
		this.studyPersintence = studyPersintence;
	}

	@Override
	public Study create(Study study) throws DuplicateException {
		log.debug("Into create on Application Domain");
		Integer personCc = study.getPerson().getIdentification();
		Integer professionId = study.getProfession().getIdentification();
		if (studyPersintence.findById(personCc, professionId) != null) {
			throw new DuplicateException("The study with person " + personCc + " and profession "
					+ professionId + " already exists in db");
		}
		return studyPersintence.save(study);
	}

	@Override
	public Study edit(Integer personCc, Integer professionId, Study study) throws NoExistException {
		Study oldStudy = studyPersintence.findById(personCc, professionId);
		if (oldStudy != null) {
			Person person = new Person();
			person.setIdentification(personCc);
			Profession profession = new Profession();
			profession.setIdentification(professionId);
			study.setPerson(person);
			study.setProfession(profession);
			return studyPersintence.save(study);
		}
		throw new NoExistException("The study with person " + personCc + " and profession " + professionId
				+ " does not exist into db, cannot be edited");
	}

	@Override
	public Boolean drop(Integer personCc, Integer professionId) throws NoExistException {
		Study oldStudy = studyPersintence.findById(personCc, professionId);
		if (oldStudy != null)
			return studyPersintence.delete(personCc, professionId);
		throw new NoExistException("The study with person " + personCc + " and profession " + professionId
				+ " does not exist into db, cannot be dropped");
	}

	@Override
	public List<Study> findAll() {
		log.info("Output: " + studyPersintence.getClass());
		return studyPersintence.find();
	}

	@Override
	public Study findOne(Integer personCc, Integer professionId) throws NoExistException {
		Study oldStudy = studyPersintence.findById(personCc, professionId);
		if (oldStudy != null)
			return oldStudy;
		throw new NoExistException("The study with person " + personCc + " and profession " + professionId
				+ " does not exist into db, cannot be found");
	}

	@Override
	public Integer count() {
		return findAll().size();
	}
}
