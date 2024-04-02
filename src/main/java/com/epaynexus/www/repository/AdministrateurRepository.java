package com.epaynexus.www.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.epaynexus.www.model.Administrateur;


@Repository
public interface AdministrateurRepository extends CrudRepository<Administrateur, Long> {
	Administrateur findByEmail(String email);
}
