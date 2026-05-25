package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.PersonaRepositoryMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("phoneOutputAdapterMaria")
@Transactional
public class PhoneOutputAdapterMaria implements PhoneOutputPort {

	@Autowired
	private TelefonoRepositoryMaria telefonoRepositoryMaria;

	@Autowired
	private PersonaRepositoryMaria personaRepositoryMaria;

	@Autowired
	private TelefonoMapperMaria telefonoMapperMaria;

	@Override
	public Phone save(Phone phone) {
		log.debug("Into save on Adapter MariaDB");
		TelefonoEntity entity = new TelefonoEntity();
		entity.setNum(phone.getNumber());
		entity.setOper(phone.getCompany());
		if (phone.getOwner() != null && phone.getOwner().getIdentification() != null) {
			Optional<PersonaEntity> managedOwner = personaRepositoryMaria
					.findById(phone.getOwner().getIdentification());
			if (managedOwner.isEmpty()) {
				log.warn("Owner persona with cc {} not found, telefono not persisted",
						phone.getOwner().getIdentification());
				return phone;
			}
			entity.setDuenio(managedOwner.get());
		}
		TelefonoEntity persisted = telefonoRepositoryMaria.save(entity);
		return telefonoMapperMaria.fromAdapterToDomain(persisted);
	}

	@Override
	public Boolean delete(String number) {
		log.debug("Into delete on Adapter MariaDB");
		telefonoRepositoryMaria.deleteById(number);
		return telefonoRepositoryMaria.findById(number).isEmpty();
	}

	@Override
	public List<Phone> find() {
		log.debug("Into find on Adapter MariaDB");
		return telefonoRepositoryMaria.findAll().stream()
				.map(telefonoMapperMaria::fromAdapterToDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Phone findById(String number) {
		log.debug("Into findById on Adapter MariaDB");
		return telefonoRepositoryMaria.findById(number)
				.map(telefonoMapperMaria::fromAdapterToDomain)
				.orElse(null);
	}
}
