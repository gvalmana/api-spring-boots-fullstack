package com.micasa.backend.apirest.clients.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.micasa.backend.apirest.clients.dao.IUsersDao;
import com.micasa.backend.apirest.clients.models.entitys.User;

@Service
public class UserService implements UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private IUsersDao userDao;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userDao.findByUsername(username);
		if (user == null) {
			logger.error("Error at login: username not found");
			throw new UsernameNotFoundException("User " + username + " not found");
		}
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.peek(authority -> logger.info("role: ".concat(authority.getAuthority()))).collect(Collectors.toList());
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true,
				true, true, false, authorities);
	}

}
