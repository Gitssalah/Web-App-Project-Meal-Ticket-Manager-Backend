package com.epaynexus.www.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ConfigurationSecurite {

	private JwtFilter jwtFilter;
	private static final String ADMIN = "ADMINISTRATEUR";

	public ConfigurationSecurite(JwtFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authorize -> authorize.requestMatchers("API/Administrateur/*/*/*")
						.hasRole(ADMIN).requestMatchers("API/Administrateur/*/").permitAll()//hasRole(ADMIN)
						.requestMatchers("API/Administrateur/*/*").permitAll()//.hasRole(ADMIN)
						.requestMatchers("API/Connexion").permitAll()
						.requestMatchers("API/Commercant/Modification/ModifPerso").hasRole("COMMERCANT")
						.requestMatchers("API/Employeur/Modification/ModifSalarie").hasRole("EMPLOYEUR")
						.requestMatchers("API/Employeur/Modification/ModifPerso").hasRole("EMPLOYEUR")
						.requestMatchers("API/Salarie/Consultation").hasRole("SALARIE")
						.requestMatchers("API/DemandeCreationCompte/creerDemande").permitAll().anyRequest().permitAll()
				// .authenticated()

				)

				.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build();

	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
		return daoAuthenticationProvider;
	}
}
