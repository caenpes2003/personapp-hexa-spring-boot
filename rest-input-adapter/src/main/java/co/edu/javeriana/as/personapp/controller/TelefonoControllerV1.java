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

import co.edu.javeriana.as.personapp.adapter.TelefonoInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/telefono")
public class TelefonoControllerV1 {

	@Autowired
	private TelefonoInputAdapterRest telefonoInputAdapterRest;

	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TelefonoResponse> listar(@PathVariable String database) {
		log.info("GET /api/v1/telefono/{}", database);
		return telefonoInputAdapterRest.historial(database.toUpperCase());
	}

	@ResponseBody
	@GetMapping(path = "/{database}/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TelefonoResponse buscar(@PathVariable String database, @PathVariable String number) {
		log.info("GET /api/v1/telefono/{}/{}", database, number);
		return telefonoInputAdapterRest.buscarUno(database.toUpperCase(), number);
	}

	@ResponseBody
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public TelefonoResponse crear(@RequestBody TelefonoRequest request) {
		log.info("POST /api/v1/telefono");
		return telefonoInputAdapterRest.crear(request);
	}

	@ResponseBody
	@PutMapping(path = "/{number}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public TelefonoResponse editar(@PathVariable String number, @RequestBody TelefonoRequest request) {
		log.info("PUT /api/v1/telefono/{}", number);
		return telefonoInputAdapterRest.editar(number, request);
	}

	@ResponseBody
	@DeleteMapping(path = "/{database}/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean eliminar(@PathVariable String database, @PathVariable String number) {
		log.info("DELETE /api/v1/telefono/{}/{}", database, number);
		return telefonoInputAdapterRest.eliminar(database.toUpperCase(), number);
	}

	@ResponseBody
	@GetMapping(path = "/{database}/count/total", produces = MediaType.APPLICATION_JSON_VALUE)
	public Integer contar(@PathVariable String database) {
		return telefonoInputAdapterRest.contar(database.toUpperCase());
	}
}
