package com.micasa.backend.apirest.clients.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.micasa.backend.apirest.clients.models.entitys.Client;
import com.micasa.backend.apirest.clients.models.entitys.Region;

public interface IClientsDao extends JpaRepository<Client, Long> {
	@Query("from Region")
	List<Region> findAllRegions();
}
