package com.micasa.backend.apirest.commons.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class IndexRestController {

	@Autowired
	private SessionRegistry sessionRegistry;
	
	@GetMapping("/")
	public String healthcheck() {
		return "Ok, I'm Alive";
	}
	
	@GetMapping("session")
	public ResponseEntity<?> getSessionDetail(){
		String sessionId = "";
		User userObjet = null ;
		List<Object> sessions = sessionRegistry.getAllPrincipals();
		for (Object session : sessions) {
			if (session instanceof User) {
				userObjet = (User) session;
			}
			
			List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(session, false);
			for (SessionInformation sessionInformation : sessionInformations) {
				sessionId = sessionInformation.getSessionId();
			}
		}
		
		Map<String, Object> response = new HashMap<>();
		response.put("response", "Hello world");
		response.put("sessionId", sessionId);
		response.put("sessionUser", userObjet);
		return ResponseEntity.ok(response);
	}
}
