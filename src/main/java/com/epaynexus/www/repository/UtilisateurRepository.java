package com.epaynexus.www.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.epaynexus.www.model.Utilisateur;


@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {
	Optional<Utilisateur> findByEmail(String email);
    @Query("SELECT u.id FROM Utilisateur u WHERE u.email = ?1")
    Optional<Long> findUserIdByEmail(String email);
}
