package com.mvs.server.utils.databasemanagement;

import com.mvs.server.model.User;
import com.mvs.server.persistence.CompanyRepository;
import com.mvs.server.persistence.ImageRepository;
import com.mvs.server.persistence.ProductRepository;
import com.mvs.server.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Observable;

/**
 * Created by fi on 4/4/2017.
 * Control USER jpa service
 */

@Service
public class UserJpaService implements JpaService {

	@Autowired
	private CompanyRepository companyRepo;

	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ImageRepository imageRepo;

	public UserJpaService () {

	}


	@Override
	public Object updateJPASingle(Long id, Object obj) {
		return null;
	}

	@Override
	public Object updateJPASingle(Object target, Object obj) {
		return null;
	}

	@Override
	public List updateJPAMultple(List targetList, List objList) {
		return null;
	}

	@Override
	public Object insertJPASingle(Object obj) {
		return userRepo.save((User) obj);
	}

	@Override
	public List insertJPAMultple(List objs) {
		return null;
	}

	@Override
	public void deleteJPASingle(Object obj) {

	}

	@Override
	public void deleteJPAMultiple(List objs) {

	}

	@Override
	public Object retrieveJPASingle(Object obj) {
		return null;
	}

	@Override
	public List retrieveJPAMultiple(List objs) {
		return null;
	}

	@Override
	public void update(Observable o, Object arg) {

	}
}
