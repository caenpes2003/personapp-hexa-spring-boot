package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;

@Mapper
public class TelefonoMapperCli {

	public TelefonoModelCli fromDomainToAdapterCli(Phone phone) {
		TelefonoModelCli model = new TelefonoModelCli();
		model.setNumero(phone.getNumber());
		model.setOperador(phone.getCompany());
		if (phone.getOwner() != null) {
			model.setDuenioCc(phone.getOwner().getIdentification());
			model.setDuenioNombre(phone.getOwner().getFirstName());
		}
		return model;
	}
}
