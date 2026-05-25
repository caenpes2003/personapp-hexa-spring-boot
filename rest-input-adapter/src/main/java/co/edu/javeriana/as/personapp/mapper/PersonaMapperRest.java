package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;

@Mapper
public class PersonaMapperRest {

	public PersonaResponse fromDomainToAdapterRestMaria(Person person) {
		return fromDomainToAdapterRest(person, "MariaDB");
	}

	public PersonaResponse fromDomainToAdapterRestMongo(Person person) {
		return fromDomainToAdapterRest(person, "MongoDB");
	}

	public PersonaResponse fromDomainToAdapterRest(Person person, String database) {
		return new PersonaResponse(
				person.getIdentification() + "",
				person.getFirstName(),
				person.getLastName(),
				person.getAge() != null ? person.getAge() + "" : "",
				person.getGender() != null ? person.getGender().toString() : "",
				database,
				"OK");
	}

	public Person fromAdapterToDomain(PersonaRequest request) {
		Person person = new Person();
		person.setIdentification(Integer.parseInt(request.getDni()));
		person.setFirstName(request.getFirstName());
		person.setLastName(request.getLastName());
		person.setGender(parseGender(request.getSex()));
		if (request.getAge() != null && !request.getAge().isBlank()) {
			person.setAge(Integer.parseInt(request.getAge()));
		}
		return person;
	}

	private Gender parseGender(String sex) {
		if (sex == null) {
			return Gender.OTHER;
		}
		switch (sex.trim().toUpperCase()) {
			case "M":
			case "MALE":
				return Gender.MALE;
			case "F":
			case "FEMALE":
				return Gender.FEMALE;
			default:
				return Gender.OTHER;
		}
	}
}
