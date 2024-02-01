package com.micasa.backend.apirest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
	
	//Configuration One
	/*
	 * @Bean public SecurityFilterChain filterChain(HttpSecurity httpSecurity)
	 * throws Exception{ return httpSecurity.authorizeHttpRequests()
	 * .requestMatchers("/api/").permitAll() .anyRequest().authenticated() .and()
	 * .formLogin().permitAll() .and() .build(); }
	 */
	
	//Configuration Two
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.authorizeRequests(auth -> {
			auth.requestMatchers("api/").permitAll();
			auth.anyRequest().authenticated();
		})
				.formLogin()
					.successHandler(susccesHandler()) //URL TO REDIRECT LOGIN
					.permitAll()
				.and()
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.ALWAYS) //ALWAYS - IF_REQUERD - NEVER - STATELESS
					.invalidSessionUrl("/login")
					.maximumSessions(1)
					.expiredUrl("/login")
					.sessionRegistry(sessionRegistry())
				.and()
				.sessionFixation()
					.migrateSession() //migrateSession() -> genera otro ID de sesion apenas se detecte un intento de robar sesion, newSession() crea una sesione completamente, none() -> deshabilita la configuracion
				.and()
				.build();
	}
	
	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}
	
	public AuthenticationSuccessHandler susccesHandler() {
		return ((request, response, authentication) -> {
			response.sendRedirect("/api/session");
		});
	}
}
