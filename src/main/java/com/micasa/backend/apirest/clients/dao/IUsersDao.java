package com.micasa.backend.apirest.clients.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.micasa.backend.apirest.clients.models.entitys.User;

@Repository
public interface IUsersDao extends CrudRepository<User, Long> {

	public User findByUsername(String username);

	public User findByUsernameOrEmail(String data, String email);
}
