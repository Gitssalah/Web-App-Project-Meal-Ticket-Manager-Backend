package com.epaynexus.www.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.epaynexus.www.model.Facture;

@Repository
public interface FactureRepository extends CrudRepository<Facture, Long>{
	Optional<Facture> findByCommande_Reference(Long reference);
}
