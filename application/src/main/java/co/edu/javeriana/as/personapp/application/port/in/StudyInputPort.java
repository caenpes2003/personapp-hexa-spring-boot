package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.DuplicateException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;

@Port
public interface StudyInputPort {

	public void setPersintence(StudyOutputPort studyPersintence);

	public Study create(Study study) throws DuplicateException;

	public Study edit(Integer personCc, Integer professionId, Study study) throws NoExistException;

	public Boolean drop(Integer personCc, Integer professionId) throws NoExistException;

	public List<Study> findAll();

	public Study findOne(Integer personCc, Integer professionId) throws NoExistException;

	public Integer count();
}
