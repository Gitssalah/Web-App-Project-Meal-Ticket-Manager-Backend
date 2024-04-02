package com.epaynexus.www.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.epaynexus.www.model.Commercant;


@Repository
public interface CommercantRepository extends CrudRepository<Commercant, Long> {
	Commercant findByEmail(String email);
}
