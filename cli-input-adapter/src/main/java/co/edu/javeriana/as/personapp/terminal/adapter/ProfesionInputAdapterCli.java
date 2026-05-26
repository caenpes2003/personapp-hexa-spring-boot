package co.edu.javeriana.as.personapp.terminal.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfesionMapperCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterCli {

	@Autowired
	@Qualifier("professionOutputAdapterMaria")
	private ProfessionOutputPort professionOutputPortMaria;

	@Autowired
	@Qualifier("professionOutputAdapterMongo")
	private ProfessionOutputPort professionOutputPortMongo;

	@Autowired
	private ProfesionMapperCli profesionMapperCli;

	ProfessionInputPort professionInputPort;

	public void setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Listando Profesiones");
		professionInputPort.findAll().stream()
				.map(profesionMapperCli::fromDomainToAdapterCli)
				.forEach(System.out::println);
	}

	public void buscarUno(Integer id) {
		try {
			Profession p = professionInputPort.findOne(id);
			System.out.println(profesionMapperCli.fromDomainToAdapterCli(p));
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void crear(Integer id, String nombre, String descripcion) {
		Profession p = new Profession();
		p.setIdentification(id);
		p.setName(nombre);
		p.setDescription(descripcion);
		Profession creada = professionInputPort.create(p);
		System.out.println("Profesion creada:");
		System.out.println(profesionMapperCli.fromDomainToAdapterCli(creada));
	}

	public void editar(Integer id, String nombre, String descripcion) {
		try {
			Profession p = new Profession();
			p.setIdentification(id);
			p.setName(nombre);
			p.setDescription(descripcion);
			Profession editada = professionInputPort.edit(id, p);
			System.out.println("Profesion editada:");
			System.out.println(profesionMapperCli.fromDomainToAdapterCli(editada));
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void eliminar(Integer id) {
		try {
			Boolean borrada = professionInputPort.drop(id);
			System.out.println("Profesion eliminada: " + borrada);
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void contar() {
		System.out.println("Total de profesiones: " + professionInputPort.count());
	}
}
