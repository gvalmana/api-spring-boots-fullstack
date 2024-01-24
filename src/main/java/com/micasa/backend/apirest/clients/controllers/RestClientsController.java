package com.micasa.backend.apirest.clients.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.micasa.backend.apirest.clients.models.entitys.Client;
import com.micasa.backend.apirest.clients.models.entitys.Region;
import com.micasa.backend.apirest.clients.services.IClientsService;
import com.micasa.backend.apirest.clients.services.IRegionsService;
import com.micasa.backend.apirest.clients.services.IUploadFileService;

import jakarta.validation.Valid;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping(path = "/api/clients")
public class RestClientsController {

	@Autowired
	private IClientsService clienteService;

	@Autowired
	private IUploadFileService uploadService;

	@Autowired
	private IRegionsService regionsService;

	private final Logger logger = LoggerFactory.getLogger(RestClientsController.class);

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> index() {
		Map<String, Object> response = new HashMap<>();
		try {
			response.put("data", this.clienteService.findAll());
			response.put("message", "OK");
			response.put("success", true);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("success", false);
			response.put("message", "Errors getting the clients lists");
			response.put("error", e.getMostSpecificCause() + ": " + e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("search")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> index(@RequestParam Integer page) {
		Map<String, Object> response = new HashMap<>();
		try {
			Pageable paginate = PageRequest.of(page, 15);
			return new ResponseEntity<Page<Client>>(this.clienteService.findAll(paginate), HttpStatus.OK);
		} catch (DataAccessException e) {
			response.put("success", false);
			response.put("message", "Errors getting the clients lists");
			response.put("error", e.getMostSpecificCause() + ": " + e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> show(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		Client client = null;
		try {
			client = this.clienteService.findById(id);
		} catch (DataAccessException e) {
			response.put("success", false);
			response.put("message", "Client with ID: " + id + " not found!");
			response.put("error", e.getMostSpecificCause() + ": " + e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (client == null) {
			response.put("success", false);
			response.put("message", "Client with ID: " + id + " not found!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		response.put("success", true);
		response.put("message", "OK");
		response.put("data", client);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> save(@Valid @RequestBody Client client, BindingResult bindingResult) {
		Client clientNew = null;
		Map<String, Object> response = new HashMap<>();
		if (bindingResult.hasErrors()) {
			response.put("success", false);
			List<String> errors = bindingResult.getFieldErrors().stream()
					.map(err -> err.getField() + ": " + err.getDefaultMessage()).collect(Collectors.toList());
			response.put("errors", errors);
			response.put("message", "Validation errors");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			clientNew = this.clienteService.save(client);
		} catch (DataAccessException e) {
			response.put("success", false);
			response.put("message", "Error saving the client!");
			response.put("error", e.getMostSpecificCause() + ": " + e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("success", true);
		response.put("message", "Validation errors");
		response.put("data", clientNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> update(@Valid @RequestBody Client entity, BindingResult result, @PathVariable Long id) {
		Client clientNew = null;
		Client clientActual = this.clienteService.findById(id);
		Map<String, Object> response = new HashMap<>();
		if (result.hasErrors()) {
			response.put("success", false);
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> err.getField() + ": " + err.getDefaultMessage()).collect(Collectors.toList());
			response.put("message", "Error data validation!");
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		if (clientActual == null) {
			response.put("success", false);
			response.put("message", "Client with ID: " + id + " not found!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			clientNew = this.clienteService.updateById(id, entity);
		} catch (DataAccessException e) {
			response.put("success", false);
			response.put("message", "Error saving the client!");
			response.put("error", e.getMostSpecificCause() + ": " + e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("success", true);
		response.put("message", "Client with ID: " + id + " updated succesfully");
		response.put("data", clientNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		Client clientActual = this.clienteService.findById(id);
		if (clientActual == null) {
			response.put("success", false);
			response.put("message", "Client with ID: " + id + " not found!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			String namePhoto = clientActual.getPhoto();
			this.clienteService.deleteById(id);
			this.uploadService.delete(namePhoto);
		} catch (DataAccessException e) {
			response.put("success", false);
			response.put("message", "Error deleting the client!");
			response.put("error", e.getMostSpecificCause() + ": " + e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("success", true);
		response.put("message", "Client deleted successfully");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@PostMapping("upload")
	public ResponseEntity<?> upload(@RequestParam(name = "file") MultipartFile file, @RequestParam("id") Long id) {
		logger.debug("CARGANDO IMAGEN");
		Map<String, Object> response = new HashMap<>();
		Client client = this.clienteService.findById(id);
		if (client == null) {
			response.put("success", false);
			response.put("message", "Client with ID: " + id + " not found!");
			response.put("error", "Client not found to atach the image");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		if (!file.isEmpty()) {
			String finalFileName = null;
			try {
				finalFileName = this.uploadService.copy(file);
				logger.debug(finalFileName);
			} catch (IOException e) {
				logger.error("Hay un error al intentar subir la imagen");
				e.printStackTrace();
				response.put("success", false);
				response.put("message", "Error loading image!");
				response.put("error", e.getMessage());
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			this.uploadService.delete(client.getPhoto());
			client.setPhoto(finalFileName);
			this.clienteService.save(client);
			response.put("success", true);
			response.put("cliente", client);
			response.put("message", "File loaded successfully: " + finalFileName);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping("uploads/img/{picname:.+}")
	public ResponseEntity<?> getUserPic(@PathVariable String picname) {
		Resource resource = null;
		try {
			resource = this.uploadService.load(picname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}

	@GetMapping("regions")
	public ResponseEntity<?> getRegions() {
		List<Region> regions = this.regionsService.getAll();
		return ResponseEntity.ok(regions);
	}

}
