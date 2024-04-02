package com.epaynexus.www.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.epaynexus.www.model.CarteVirtuelle;

@Repository
public interface CarteVirtuelleRepository extends CrudRepository<CarteVirtuelle, String>{
	Optional<CarteVirtuelle> findBySalarie_Id(Long salarieId);
}
