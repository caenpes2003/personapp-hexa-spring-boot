package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.EstudioInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/estudio")
public class EstudioControllerV1 {

	@Autowired
	private EstudioInputAdapterRest estudioInputAdapterRest;

	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<EstudioResponse> listar(@PathVariable String database) {
		log.info("GET /api/v1/estudio/{}", database);
		return estudioInputAdapterRest.historial(database.toUpperCase());
	}

	@ResponseBody
	@GetMapping(path = "/{database}/{personCc}/{professionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public EstudioResponse buscar(@PathVariable String database, @PathVariable Integer personCc,
			@PathVariable Integer professionId) {
		log.info("GET /api/v1/estudio/{}/{}/{}", database, personCc, professionId);
		return estudioInputAdapterRest.buscarUno(database.toUpperCase(), personCc, professionId);
	}

	@ResponseBody
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public EstudioResponse crear(@RequestBody EstudioRequest request) {
		log.info("POST /api/v1/estudio");
		return estudioInputAdapterRest.crear(request);
	}

	@ResponseBody
	@PutMapping(path = "/{personCc}/{professionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public EstudioResponse editar(@PathVariable Integer personCc, @PathVariable Integer professionId,
			@RequestBody EstudioRequest request) {
		log.info("PUT /api/v1/estudio/{}/{}", personCc, professionId);
		return estudioInputAdapterRest.editar(personCc, professionId, request);
	}

	@ResponseBody
	@DeleteMapping(path = "/{database}/{personCc}/{professionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean eliminar(@PathVariable String database, @PathVariable Integer personCc,
			@PathVariable Integer professionId) {
		log.info("DELETE /api/v1/estudio/{}/{}/{}", database, personCc, professionId);
		return estudioInputAdapterRest.eliminar(database.toUpperCase(), personCc, professionId);
	}

	@ResponseBody
	@GetMapping(path = "/{database}/count/total", produces = MediaType.APPLICATION_JSON_VALUE)
	public Integer contar(@PathVariable String database) {
		return estudioInputAdapterRest.contar(database.toUpperCase());
	}
}
