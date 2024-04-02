package com.epaynexus.www.controller;

import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.epaynexus.www.dto.InscriptionSalarieRequest;
import com.epaynexus.www.service.SalarieService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "API/Salarie")
public class SalarieController {

	private SalarieService salarieService;
	private final ResourceLoader resourceLoader;

	@GetMapping(path = "/Consultation", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> consulterComptePerso() {
		log.info("Consultation du compte personnel");
		Map<String, String> infos = this.salarieService.consulterInfosPersos();
		return ResponseEntity.ok().body(infos);
	}

	@PostMapping(path = "/creer", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> creationSalarie(@RequestBody InscriptionSalarieRequest inscriptionSalarieRequest) {
		JSONObject jsonObject = new JSONObject();
		try {
			log.info("Creation du compte salarié");
			this.salarieService.creerSalarie(inscriptionSalarieRequest);
			jsonObject.put("message", "Salarié est crée avec succès");
			return ResponseEntity.status(HttpStatus.CREATED).body(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("exception", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
		}
	}

	@GetMapping("/telechargerTemplateListeSalaries")
	public ResponseEntity<Object> telechargerTemplateListeSalaries(String fileName, HttpServletResponse response) {

		try {
			Resource resource = resourceLoader.getResource("classpath:TemplateListeSalaries.xlsx");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
			headers.setContentDispositionFormData("attachment", "TemplateListeSalaries.xlsx");
			return new ResponseEntity<>(resource, headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("exception", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
		}
	}
}
