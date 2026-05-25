package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;

@Mapper
public class TelefonoMapperRest {

	public TelefonoResponse fromDomainToAdapterRestMaria(Phone phone) {
		return fromDomainToAdapterRest(phone, "MariaDB");
	}

	public TelefonoResponse fromDomainToAdapterRestMongo(Phone phone) {
		return fromDomainToAdapterRest(phone, "MongoDB");
	}

	public TelefonoResponse fromDomainToAdapterRest(Phone phone, String database) {
		String ownerCc = "";
		String ownerName = "";
		if (phone.getOwner() != null) {
			if (phone.getOwner().getIdentification() != null) {
				ownerCc = phone.getOwner().getIdentification() + "";
			}
			if (phone.getOwner().getFirstName() != null) {
				ownerName = phone.getOwner().getFirstName();
			}
		}
		return new TelefonoResponse(
				phone.getNumber(),
				phone.getCompany(),
				ownerCc,
				ownerName,
				database,
				"OK");
	}

	public Phone fromAdapterToDomain(TelefonoRequest request) {
		Phone phone = new Phone();
		phone.setNumber(request.getNumber());
		phone.setCompany(request.getCompany());
		Person owner = new Person();
		owner.setIdentification(Integer.parseInt(request.getOwnerCc()));
		phone.setOwner(owner);
		return phone;
	}
}
