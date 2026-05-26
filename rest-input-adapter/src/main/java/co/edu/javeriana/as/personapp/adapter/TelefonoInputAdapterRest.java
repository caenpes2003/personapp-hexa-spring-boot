package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.DuplicateException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.exceptions.UnprocessableEntityException;
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
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

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

	private PersonOutputPort resolvePersonPort(String database) {
		if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			return personOutputPortMaria;
		}
		return personOutputPortMongo;
	}

	public List<TelefonoResponse> historial(String database) throws InvalidOptionException {
		String selectedDb = setPhoneOutputPortInjection(database);
		return phoneInputPort.findAll().stream()
				.map(p -> buildResponse(p, selectedDb))
				.collect(Collectors.toList());
	}

	public TelefonoResponse buscarUno(String database, String number)
			throws InvalidOptionException, NoExistException {
		String selectedDb = setPhoneOutputPortInjection(database);
		Phone phone = phoneInputPort.findOne(number);
		return buildResponse(phone, selectedDb);
	}

	public TelefonoResponse crear(TelefonoRequest request)
			throws InvalidOptionException, DuplicateException, UnprocessableEntityException {
		String selectedDb = setPhoneOutputPortInjection(request.getDatabase());
		Phone domainPhone = telefonoMapperRest.fromAdapterToDomain(request);
		Integer ownerCc = domainPhone.getOwner() != null ? domainPhone.getOwner().getIdentification() : null;
		if (ownerCc == null || resolvePersonPort(request.getDatabase()).findById(ownerCc) == null) {
			throw new UnprocessableEntityException(
					"La persona con cc " + ownerCc + " no existe en " + request.getDatabase()
							+ ", no se puede crear el telefono.");
		}
		Phone phone = phoneInputPort.create(domainPhone);
		return buildResponse(phone, selectedDb);
	}

	public TelefonoResponse editar(String number, TelefonoRequest request)
			throws InvalidOptionException, NoExistException, UnprocessableEntityException {
		String selectedDb = setPhoneOutputPortInjection(request.getDatabase());
		Phone domainPhone = telefonoMapperRest.fromAdapterToDomain(request);
		Integer ownerCc = domainPhone.getOwner() != null ? domainPhone.getOwner().getIdentification() : null;
		if (ownerCc == null || resolvePersonPort(request.getDatabase()).findById(ownerCc) == null) {
			throw new UnprocessableEntityException(
					"La persona con cc " + ownerCc + " no existe en " + request.getDatabase()
							+ ", no se puede editar el telefono.");
		}
		Phone phone = phoneInputPort.edit(number, domainPhone);
		return buildResponse(phone, selectedDb);
	}

	public Boolean eliminar(String database, String number)
			throws InvalidOptionException, NoExistException {
		setPhoneOutputPortInjection(database);
		return phoneInputPort.drop(number);
	}

	public Integer contar(String database) throws InvalidOptionException {
		setPhoneOutputPortInjection(database);
		return phoneInputPort.count();
	}
}
