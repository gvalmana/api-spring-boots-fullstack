package com.micasa.backend.apirest.clients.services;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.micasa.backend.apirest.clients.dao.IClientsDao;
import com.micasa.backend.apirest.clients.models.entitys.Client;

@Service
public class ClientsServiceImpl implements ClientService {

	@Autowired
	private IClientsDao clientsDao;

	@Override
	@Transactional(readOnly = true)
	public List<Client> findAll() {
		return this.clientsDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Client> findAll(Pageable page) {
		return this.clientsDao.findAll(page);
	}

	@Override
	@Transactional(readOnly = true)
	public Client findById(Long id) {
		return this.clientsDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Client save(Client entity) {
		return this.clientsDao.save(entity);
	}

	@Override
	@Transactional
	public Client updateById(Long id, Client entity) {
		entity.setId(id);
		return this.clientsDao.save(entity);
	}

	@Override
	public void deleteById(Long id) {
		this.clientsDao.deleteById(id);
	}

	@Override
	public void deleteClientPic(Long id) {
		System.out.println("ELIMINADO FOTO DEL CLIENTE");
		Optional<Client> client = this.clientsDao.findById(id);
		System.out.println(client.isPresent());
		if (!client.isPresent()) {
			System.out.println("CLIENTE NO ENCONTRADO");
			return;
		}
		String oldPhoto = client.get().getPhoto();
		System.out.println(oldPhoto);
		if (oldPhoto != null && oldPhoto.length() > 0) {
			Path oldPhotoPath = Paths.get("uploads").resolve(oldPhoto).toAbsolutePath();
			File oldPhotoFile = oldPhotoPath.toFile();
			if (oldPhotoFile.exists() && oldPhotoFile.canRead()) {
				oldPhotoFile.delete();
			}
		}
	}

	@Override
	public void deleteClientPic(Client client) {
		String oldPhoto = client.getPhoto();
		if (oldPhoto != null && oldPhoto.length() > 0) {
			Path oldPhotoPath = Paths.get("uploads").resolve(oldPhoto).toAbsolutePath();
			File oldPhotoFile = oldPhotoPath.toFile();
			if (oldPhotoFile.exists() && oldPhotoFile.canRead()) {
				oldPhotoFile.delete();
			}
		}
	}
}
