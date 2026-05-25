package co.edu.javeriana.as.personapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI personappOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("PersonApp API")
						.version("1.0.0")
						.description(
								"API REST del Laboratorio 2 de Arquitectura de Software. "
										+ "Expone CRUD sobre las entidades Persona, Profesion, Telefono y Estudio "
										+ "con persistencia dual contra MariaDB y MongoDB. "
										+ "El parametro database en cada endpoint acepta MARIA o MONGO.")
						.contact(new Contact()
								.name("Camilo Penuela")
								.email("caenpes2003@gmail.com"))
						.license(new License()
								.name("Apache 2.0")
								.url("https://www.apache.org/licenses/LICENSE-2.0")));
	}
}
