package co.edu.javeriana.as.personapp.terminal.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.DuplicateException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.mapper.TelefonoMapperCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterCli {

	@Autowired
	@Qualifier("phoneOutputAdapterMaria")
	private PhoneOutputPort phoneOutputPortMaria;

	@Autowired
	@Qualifier("phoneOutputAdapterMongo")
	private PhoneOutputPort phoneOutputPortMongo;

	@Autowired
	private TelefonoMapperCli telefonoMapperCli;

	PhoneInputPort phoneInputPort;

	public void setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Listando Telefonos");
		phoneInputPort.findAll().stream()
				.map(telefonoMapperCli::fromDomainToAdapterCli)
				.forEach(System.out::println);
	}

	public void buscarUno(String numero) {
		try {
			Phone p = phoneInputPort.findOne(numero);
			System.out.println(telefonoMapperCli.fromDomainToAdapterCli(p));
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void crear(String numero, String operador, Integer duenioCc) {
		try {
			Phone p = new Phone();
			p.setNumber(numero);
			p.setCompany(operador);
			Person owner = new Person();
			owner.setIdentification(duenioCc);
			p.setOwner(owner);
			Phone creado = phoneInputPort.create(p);
			System.out.println("Telefono creado:");
			System.out.println(telefonoMapperCli.fromDomainToAdapterCli(creado));
		} catch (DuplicateException e) {
			System.out.println(e.getMessage());
		}
	}

	public void editar(String numero, String operador, Integer duenioCc) {
		try {
			Phone p = new Phone();
			p.setNumber(numero);
			p.setCompany(operador);
			Person owner = new Person();
			owner.setIdentification(duenioCc);
			p.setOwner(owner);
			Phone editado = phoneInputPort.edit(numero, p);
			System.out.println("Telefono editado:");
			System.out.println(telefonoMapperCli.fromDomainToAdapterCli(editado));
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void eliminar(String numero) {
		try {
			Boolean borrado = phoneInputPort.drop(numero);
			System.out.println("Telefono eliminado: " + borrado);
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void contar() {
		System.out.println("Total de telefonos: " + phoneInputPort.count());
	}
}
