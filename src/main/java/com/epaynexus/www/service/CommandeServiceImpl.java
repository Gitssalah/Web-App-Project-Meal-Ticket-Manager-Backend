package com.epaynexus.www.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epaynexus.www.dto.CommandeDTO;
import com.epaynexus.www.dto.CreationCommandeRequest;
import com.epaynexus.www.enumeration.EtatCommandeEnum;
import com.epaynexus.www.exception.NotFoundException;
import com.epaynexus.www.mapper.CommandeMapper;
import com.epaynexus.www.mapper.SalarieMapper;
import com.epaynexus.www.model.CarteVirtuelle;
import com.epaynexus.www.model.Commande;
import com.epaynexus.www.model.Employeur;
import com.epaynexus.www.model.Facture;
import com.epaynexus.www.model.Salarie;
import com.epaynexus.www.repository.CommandeRepository;

@Service
public class CommandeServiceImpl implements CommandeService {
	private final CommandeRepository commandeRepository;
	private final FactureService factureService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final EmployeurService employeurService;
	private final CommandeMapper commandeMapper;
	private final SalarieMapper salarieMapper;

	public CommandeServiceImpl(CommandeRepository commandeRepository, FactureService factureService,
			BCryptPasswordEncoder bCryptPasswordEncoder, EmployeurService employeurService,
			CommandeMapper commandeMapper, SalarieMapper salarieMapper) {
		this.commandeRepository = commandeRepository;
		this.factureService = factureService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.employeurService = employeurService;
		this.commandeMapper = commandeMapper;
		this.salarieMapper = salarieMapper;
	}

	@Transactional
	@Override
	public Commande creerCommande(CreationCommandeRequest creationCommandeRequest) {
		Employeur employeur = employeurService.chercherEmployeur(creationCommandeRequest.idEmployeur());
		Commande commande = Commande.builder().dateCreation(new Date()).etat(EtatCommandeEnum.ENCOURSDETRAITEMENT)
				.plafondQuotidien(creationCommandeRequest.plafondQuotidien())
				.nombreSalaries(creationCommandeRequest.salaries().size()).employeur(employeur).build();
		List<Salarie> salaries = creationCommandeRequest.salaries().stream()
				.map(dto -> salarieMapper.mapToSalarieInactif(dto, commande, bCryptPasswordEncoder)).toList();
		commande.setSalaries(salaries);
		return commandeRepository.save(commande);
	}

	@Override
	public List<CommandeDTO> extraireToutesCommandes() {
		return ((List<Commande>) commandeRepository.findAll()).stream().map(commandeMapper::mapToCommandeDTO).toList();
	}

	@Transactional
	@Override
	public void executerCommande(Long reference) {
		Commande commande = chercherDetailsCommande(reference);
		if (commande.getEtat().equals(EtatCommandeEnum.ENCOURSDETRAITEMENT)) {
			Date dateCreation = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateCreation);
			calendar.add(Calendar.YEAR, 5);
			Date expirationDate = calendar.getTime();
			commande.getSalaries().forEach(salarie -> {
				salarie.setActif(true);
				CarteVirtuelle carteVirtuelle = CarteVirtuelle.builder().active(true).dateCreation(dateCreation)
						.dateExpiration(expirationDate).numero(generateUniqueNumber())
						.plafondQuotidien(commande.getPlafondQuotidien()).solde(salarie.getForfaitJournalier()).build();
				salarie.setCarteVirtuelle(carteVirtuelle);
				carteVirtuelle.setSalarie(salarie);
			});
			commande.setEtat(EtatCommandeEnum.TRAITEE);
			Commande cmd = commandeRepository.save(commande);
			Facture facture = factureService.genererFacture(cmd);
			commande.setFacture(facture);
			commandeRepository.save(commande);
		}
	}

	@Override
	public Commande chercherDetailsCommande(Long reference) {
		Commande commande = commandeRepository.findById(reference).orElse(null);
		if (commande == null)
			throw new NotFoundException("Commande non existante");
		return commande;
	}

	private String generateUniqueNumber() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		String formattedDateTime = now.format(formatter);
		int randomSuffix = getRandom().nextInt(1000);
		return formattedDateTime + String.format("%03d", randomSuffix);
	}

	private Random getRandom() {
		return new Random();
	}

	@Override
	public List<CommandeDTO> extraireToutesCommandesByIdEmployeur(Long idEmployeur) {
		Employeur employeur = employeurService.chercherEmployeur(idEmployeur);
		return employeur.getCommandes().stream().map(commandeMapper::mapToCommandeDTO).toList();
	}

}
