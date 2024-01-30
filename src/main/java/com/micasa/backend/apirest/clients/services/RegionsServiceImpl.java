package com.micasa.backend.apirest.clients.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.micasa.backend.apirest.clients.dao.IRegionsDao;
import com.micasa.backend.apirest.clients.models.entitys.Region;

@Service
public class RegionsServiceImpl implements RegionsService {

	@Autowired
	IRegionsDao daoRegions;

	@Override
	@Transactional(readOnly = true)
	public List<Region> getAll() {
		return this.daoRegions.findAll();
	}

}
