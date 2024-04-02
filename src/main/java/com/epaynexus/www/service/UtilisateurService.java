package com.epaynexus.www.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.epaynexus.www.model.Utilisateur;
import com.epaynexus.www.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UtilisateurService implements UserDetailsService {
	private UtilisateurRepository utilisateurRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Utilisateur utilisateur = this.utilisateurRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur ne correspond à cet identifiant"));

		return User.builder().username(utilisateur.getEmail()).password(utilisateur.getMdp())
				.authorities(utilisateur.getAuthorities()).build();

	}

	public boolean checkEmailAvailability(String email) {
		return this.utilisateurRepository.findByEmail(email).isEmpty();
	}

	public Long extractUserIDByUsername(String username) {
		return this.utilisateurRepository.findUserIdByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur ne correspond à cet identifiant"));
	}
}
