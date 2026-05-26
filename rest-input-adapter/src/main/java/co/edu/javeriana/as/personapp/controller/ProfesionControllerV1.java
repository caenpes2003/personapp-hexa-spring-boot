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

import co.edu.javeriana.as.personapp.adapter.ProfesionInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.DuplicateException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/profesion")
public class ProfesionControllerV1 {

	@Autowired
	private ProfesionInputAdapterRest profesionInputAdapterRest;

	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProfesionResponse> listar(@PathVariable String database) throws InvalidOptionException {
		log.info("GET /api/v1/profesion/{}", database);
		return profesionInputAdapterRest.historial(database.toUpperCase());
	}

	@ResponseBody
	@GetMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ProfesionResponse buscar(@PathVariable String database, @PathVariable Integer id)
			throws InvalidOptionException, NoExistException {
		log.info("GET /api/v1/profesion/{}/{}", database, id);
		return profesionInputAdapterRest.buscarUna(database.toUpperCase(), id);
	}

	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProfesionResponse> crear(@RequestBody ProfesionRequest request)
			throws InvalidOptionException, DuplicateException {
		log.info("POST /api/v1/profesion");
		ProfesionResponse body = profesionInputAdapterRest.crear(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@ResponseBody
	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ProfesionResponse editar(@PathVariable Integer id, @RequestBody ProfesionRequest request)
			throws InvalidOptionException, NoExistException {
		log.info("PUT /api/v1/profesion/{}", id);
		return profesionInputAdapterRest.editar(id, request);
	}

	@ResponseBody
	@DeleteMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean eliminar(@PathVariable String database, @PathVariable Integer id)
			throws InvalidOptionException, NoExistException {
		log.info("DELETE /api/v1/profesion/{}/{}", database, id);
		return profesionInputAdapterRest.eliminar(database.toUpperCase(), id);
	}

	@ResponseBody
	@GetMapping(path = "/{database}/count/total", produces = MediaType.APPLICATION_JSON_VALUE)
	public Integer contar(@PathVariable String database) throws InvalidOptionException {
		log.info("GET /api/v1/profesion/{}/count/total", database);
		return profesionInputAdapterRest.contar(database.toUpperCase());
	}
}
