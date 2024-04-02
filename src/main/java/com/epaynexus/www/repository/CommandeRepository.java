package com.epaynexus.www.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.epaynexus.www.model.Commande;

@Repository
public interface CommandeRepository extends CrudRepository<Commande, Long> {
	List<Commande> findByEmployeurId(Long id);
}
