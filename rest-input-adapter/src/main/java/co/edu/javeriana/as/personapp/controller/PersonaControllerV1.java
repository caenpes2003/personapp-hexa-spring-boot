package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.PersonaInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.DuplicateException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/persona")
public class PersonaControllerV1 {

	@Autowired
	private PersonaInputAdapterRest personaInputAdapterRest;

	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PersonaResponse> listar(@PathVariable String database) throws InvalidOptionException {
		log.info("GET /api/v1/persona/{}", database);
		return personaInputAdapterRest.historial(database.toUpperCase());
	}

	@ResponseBody
	@GetMapping(path = "/{database}/{cc}", produces = MediaType.APPLICATION_JSON_VALUE)
	public PersonaResponse buscar(@PathVariable String database, @PathVariable Integer cc)
			throws InvalidOptionException, NoExistException {
		log.info("GET /api/v1/persona/{}/{}", database, cc);
		return personaInputAdapterRest.buscarUna(database.toUpperCase(), cc);
	}

	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonaResponse> crearPersona(@RequestBody PersonaRequest request)
			throws InvalidOptionException, DuplicateException {
		log.info("POST /api/v1/persona");
		PersonaResponse body = personaInputAdapterRest.crearPersona(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@ResponseBody
	@PutMapping(path = "/{cc}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public PersonaResponse editar(@PathVariable Integer cc, @RequestBody PersonaRequest request)
			throws InvalidOptionException, NoExistException {
		log.info("PUT /api/v1/persona/{}", cc);
		return personaInputAdapterRest.editar(cc, request);
	}

	@ResponseBody
	@DeleteMapping(path = "/{database}/{cc}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean eliminar(@PathVariable String database, @PathVariable Integer cc)
			throws InvalidOptionException, NoExistException {
		log.info("DELETE /api/v1/persona/{}/{}", database, cc);
		return personaInputAdapterRest.eliminar(database.toUpperCase(), cc);
	}

	@ResponseBody
	@GetMapping(path = "/{database}/count/total", produces = MediaType.APPLICATION_JSON_VALUE)
	public Integer contar(@PathVariable String database) throws InvalidOptionException {
		return personaInputAdapterRest.contar(database.toUpperCase());
	}
}
