package com.micasa.backend.apirest.clients.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.micasa.backend.apirest.clients.dao.IUsersDao;
import com.micasa.backend.apirest.clients.models.entitys.User;

public class UserService {

	private Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private IUsersDao userDao;
}
