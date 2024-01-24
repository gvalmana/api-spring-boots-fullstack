package com.micasa.backend.apirest.clients.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micasa.backend.apirest.clients.models.entitys.Region;

public interface IRegionsDao extends JpaRepository<Region, Long> {

}
