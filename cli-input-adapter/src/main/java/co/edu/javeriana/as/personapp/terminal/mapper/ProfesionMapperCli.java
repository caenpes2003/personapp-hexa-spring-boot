package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;

@Mapper
public class ProfesionMapperCli {

	public ProfesionModelCli fromDomainToAdapterCli(Profession profession) {
		ProfesionModelCli model = new ProfesionModelCli();
		model.setId(profession.getIdentification());
		model.setNombre(profession.getName());
		model.setDescripcion(profession.getDescription());
		return model;
	}
}
