package com.epaynexus.www.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.epaynexus.www.dto.CarteVirtuelleDTO;
import com.epaynexus.www.exception.CarteVirtuelleNotFoundException;
import com.epaynexus.www.exception.NotFoundException;
import com.epaynexus.www.mapper.CarteVirtuelleMapper;
import com.epaynexus.www.model.CarteVirtuelle;
import com.epaynexus.www.model.Transaction;
import com.epaynexus.www.repository.CarteVirtuelleRepository;
import com.epaynexus.www.repository.TransactionRepository;

import net.minidev.json.JSONObject;

@Service
public class CarteVirtuelleServiceImpl implements CarteVirtuelleService {
	private final CarteVirtuelleRepository carteVirtuelleRepository;
	private final TransactionRepository transactionRepository;
	private final CarteVirtuelleMapper carteVirtuelleMapper;
	private static final String INCORRECT = "Numéro de carte incorrect : ";

	@Autowired
	public CarteVirtuelleServiceImpl(CarteVirtuelleRepository carteVirtuelleRepository,
			TransactionRepository transactionRepository, CarteVirtuelleMapper carteVirtuelleMapper) {
		this.carteVirtuelleRepository = carteVirtuelleRepository;
		this.transactionRepository = transactionRepository;
		this.carteVirtuelleMapper = carteVirtuelleMapper;
	}

	@Override
	public void activerCarteVirtuelle(String numero) {
		CarteVirtuelle carteVirtuelle = carteVirtuelleRepository.findById(numero)
				.orElseThrow(() -> new CarteVirtuelleNotFoundException(INCORRECT + numero));
		carteVirtuelle.setActive(true);
		carteVirtuelleRepository.save(carteVirtuelle);
	}

	@Override
	public void desactiverCarteVirtuelle(String numero) {
		CarteVirtuelle carteVirtuelle = carteVirtuelleRepository.findById(numero)
				.orElseThrow(() -> new CarteVirtuelleNotFoundException(INCORRECT + numero));
		carteVirtuelle.setActive(false);
		carteVirtuelleRepository.save(carteVirtuelle);
	}

	@Override
	public CarteVirtuelle getCarteVirtuelleByNum(String numero) {
		return carteVirtuelleRepository.findById(numero)
				.orElseThrow(() -> new CarteVirtuelleNotFoundException(INCORRECT + numero));

	}

	@Override
	public Boolean verifierCarte(String numCarte, Double montant, Double depenseJournalier) {
		Double somme = montant + depenseJournalier;
		Optional<CarteVirtuelle> carteVirtuelle = carteVirtuelleRepository.findById(numCarte);
		return carteVirtuelle.isPresent() && carteVirtuelle.get().getActive().booleanValue()
				&& carteVirtuelle.get().getSolde() >= montant && carteVirtuelle.get().getPlafondQuotidien() >= somme;
	}

	@Override
	public List<CarteVirtuelleDTO> extraireToutesCartes() {
		return ((List<CarteVirtuelle>) carteVirtuelleRepository.findAll()).stream()
				.map(c -> carteVirtuelleMapper.mapToCarteVirtuelleDTO(c)).toList();
	}

	@Scheduled(cron = "0 30 0 * * ?") //@Scheduled(cron = "seconds minutes hours dayOfMonth Month dayOfWeek")
	@Override
	public void alimenterCartesJOB() {
		((List<CarteVirtuelle>) carteVirtuelleRepository.findAll()).stream().filter(c -> c.getActive()).forEach(c -> {
			c.setSolde(c.getSolde() + c.getSalarie().getForfaitJournalier());
			carteVirtuelleRepository.save(c);
		});
	}

	@Override
	public JSONObject getSoldeCarteVirtuelleBySalarieId(Long salarieId) {
		CarteVirtuelle carteVirtuelle = carteVirtuelleRepository.findBySalarie_Id(salarieId).orElseThrow(
				() -> new NotFoundException("Carte virtuelle non trouvée pour le salarié avec l'ID : " + salarieId));
		JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "Solde total calculé avec succès");
        jsonObject.put("numero", carteVirtuelle.getNumero());
        jsonObject.put("solde", carteVirtuelle.getSolde());
		return jsonObject;
	}

	@Override
	public Double calculerSoldeJournalier(Long salarieId) {
		// Récupérer la carte virtuelle du salarié
		CarteVirtuelle carteVirtuelle = carteVirtuelleRepository.findBySalarie_Id(salarieId).orElseThrow(
				() -> new NotFoundException("Carte virtuelle non trouvée pour le salarié avec l'ID : " + salarieId));

		// Calculer le solde journalier
		Double soldeJournalier = carteVirtuelle.getSalarie().getForfaitJournalier();
		List<Transaction> transactions = transactionRepository.findByNumCarteAndDateCreationBetween(
				carteVirtuelle.getNumero(),
				// Début de la journée
				Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()),
				// Fin de la journée
				Date.from(LocalDate.now().atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()));
		for (Transaction tr : transactions) {
			soldeJournalier -= tr.getMontant();
		}

		return soldeJournalier;
	}

	@Override
	public void modifierSolde(String numCarte, Double montant) {
		Optional<CarteVirtuelle> carteVirtuelle = carteVirtuelleRepository.findById(numCarte);
		if (carteVirtuelle.isPresent()) {
			Double nouveauSolde = carteVirtuelle.get().getSolde() - montant;
			carteVirtuelle.get().setSolde(nouveauSolde);
			carteVirtuelleRepository.save(carteVirtuelle.get());
		}
	}

	@Override
	public CarteVirtuelle getCarteVirtuelleBySalarieId(Long salarieId) {
		return carteVirtuelleRepository.findBySalarie_Id(salarieId)
                .orElseThrow(() -> new NotFoundException("Carte virtuelle non trouvée pour le salarié avec l'ID : " + salarieId));
    }
}