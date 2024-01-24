package com.micasa.backend.apirest.commons.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class IndexRestController {

	@GetMapping("/")
	public String healthcheck() {

		return "Ok, I'm Alive";
	}
}
