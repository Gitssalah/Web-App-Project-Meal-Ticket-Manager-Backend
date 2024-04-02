package com.epaynexus.www.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.epaynexus.www.model.Salarie;


@Repository
public interface SalarieRepository extends CrudRepository<Salarie, Long> {
	Salarie findByEmail(String email);
}
