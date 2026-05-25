package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mapper.TelefonoMapperRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterRest {

	@Autowired
	@Qualifier("phoneOutputAdapterMaria")
	private PhoneOutputPort phoneOutputPortMaria;

	@Autowired
	@Qualifier("phoneOutputAdapterMongo")
	private PhoneOutputPort phoneOutputPortMongo;

	@Autowired
	private TelefonoMapperRest telefonoMapperRest;

	PhoneInputPort phoneInputPort;

	private String setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
			return DatabaseOption.MONGO.toString();
		}
		throw new InvalidOptionException("Invalid database option: " + dbOption);
	}

	private TelefonoResponse buildResponse(Phone phone, String selectedDb) {
		if (selectedDb.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			return telefonoMapperRest.fromDomainToAdapterRestMaria(phone);
		}
		return telefonoMapperRest.fromDomainToAdapterRestMongo(phone);
	}

	public List<TelefonoResponse> historial(String database) {
		try {
			String selectedDb = setPhoneOutputPortInjection(database);
			return phoneInputPort.findAll().stream()
					.map(p -> buildResponse(p, selectedDb))
					.collect(Collectors.toList());
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<>();
		}
	}

	public TelefonoResponse buscarUno(String database, String number) {
		try {
			String selectedDb = setPhoneOutputPortInjection(database);
			Phone phone = phoneInputPort.findOne(number);
			return buildResponse(phone, selectedDb);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public TelefonoResponse crear(TelefonoRequest request) {
		try {
			String selectedDb = setPhoneOutputPortInjection(request.getDatabase());
			Phone phone = phoneInputPort.create(telefonoMapperRest.fromAdapterToDomain(request));
			return buildResponse(phone, selectedDb);
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public TelefonoResponse editar(String number, TelefonoRequest request) {
		try {
			String selectedDb = setPhoneOutputPortInjection(request.getDatabase());
			Phone phone = phoneInputPort.edit(number, telefonoMapperRest.fromAdapterToDomain(request));
			return buildResponse(phone, selectedDb);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public Boolean eliminar(String database, String number) {
		try {
			setPhoneOutputPortInjection(database);
			return phoneInputPort.drop(number);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return false;
		}
	}

	public Integer contar(String database) {
		try {
			setPhoneOutputPortInjection(database);
			return phoneInputPort.count();
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return 0;
		}
	}
}
