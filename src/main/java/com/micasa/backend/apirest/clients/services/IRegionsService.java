package com.micasa.backend.apirest.clients.services;

import java.util.List;

import com.micasa.backend.apirest.clients.models.entitys.Region;

public interface IRegionsService {

	List<Region> getAll();
}
