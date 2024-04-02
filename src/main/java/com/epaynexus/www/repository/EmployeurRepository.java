package com.epaynexus.www.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.epaynexus.www.model.Employeur;


@Repository
public interface EmployeurRepository extends CrudRepository<Employeur, Long> {
	Employeur findByEmail(String email);
}
