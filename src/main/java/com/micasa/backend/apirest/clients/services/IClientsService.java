package com.micasa.backend.apirest.clients.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.micasa.backend.apirest.clients.models.entitys.Client;

public interface IClientsService {

	List<Client> findAll();

	Page<Client> findAll(Pageable page);

	Client findById(Long id);

	Client save(Client entity);

	Client updateById(Long id, Client entity);

	void deleteById(Long id);

	void deleteClientPic(Long id);

	void deleteClientPic(Client id);
}
