package com.epaynexus.www.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.epaynexus.www.dto.AuthentificationDto;
import com.epaynexus.www.security.JwtService;
import com.epaynexus.www.service.UtilisateurService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class UtilisateurController {
	private AuthenticationManager authenticationManager;
	private UtilisateurService utilisateurService;
	private JwtService jwtService;

	@PostMapping(path = "API/Connexion")
	public Map<String, String> connexion(@RequestBody AuthentificationDto authentificationDto) {
		final Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authentificationDto.username(), authentificationDto.password()));

		if (authenticate.isAuthenticated()) {
			return this.jwtService.generate(authentificationDto.username());
		}

		return Collections.emptyMap();
	}

	@PostMapping(path = "API/Deconnexion")
	public Map<String, String> deconnexion(@RequestBody String token) {
		if (token != null) {
			this.jwtService.invalidateToken(token);
		}
		return Collections.singletonMap("message", "Déconnexion réussie");
	}

	@GetMapping(path = "API/CheckEmail/{email}")
	public ResponseEntity<JSONObject> checkEmail(@PathVariable String email) {
		JSONObject jsonObject = new JSONObject();
		try {
			boolean emailExists = utilisateurService.checkEmailAvailability(email);
			jsonObject.put("emailDisponible", emailExists);
			return ResponseEntity.status(HttpStatus.OK).body(jsonObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObject.put("exception", ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
		}
	}
}
