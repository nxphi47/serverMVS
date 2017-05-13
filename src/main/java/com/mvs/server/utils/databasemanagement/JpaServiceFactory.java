package com.mvs.server.utils.databasemanagement;

import com.mvs.server.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Observable;

/**
 * Created by fi on 4/4/2017.
 * The jpaservice factory will help retrieve any needed JPA service -
 * factory should be used in implementation code
 */

@Service
public class JpaServiceFactory implements JpaService {

	public static final Logger logger = LoggerFactory.getLogger(JpaServiceFactory.class);

	public enum ServiceType {
		COMPANY, PRODUCT, USER, IMAGE, SALE,
	}

	public enum RepoType {
		COMPANY, PRODUCT, USER, IMAGE, SALE,
	}

	@Autowired
	private CompanyRepository companyRepo;
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ImageRepository imageRepo;

	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private CompanyJpaService companyJpaService;
	@Autowired
	private ImageJpaService imageJpaService;
	@Autowired
	private ProductJpaService productJpaService;
	@Autowired
	private UserJpaService userJpaService;
	@Autowired
	private SaleJpaService saleJpaService;

	public JpaServiceFactory() {

	}

	public JpaService getServiceInstance(ServiceType type) {
		switch (type) {
			case USER:
				return userJpaService;
			case IMAGE:
				return imageJpaService;
			case COMPANY:
				return companyJpaService;
			case PRODUCT:
				return productJpaService;
			case SALE:
				return saleJpaService;
		}
		logger.debug("Unable to define service type!");
		return null;
	}


	public JpaRepository getRepoInstance(RepoType type) {
		switch (type) {
			case SALE:
				return saleRepository;
			case IMAGE:
				return imageRepo;
			case PRODUCT:
				return productRepo;
			case COMPANY:
				return companyRepo;
			case USER:
				return userRepo;
		}
		logger.debug("Unable to define repo type!");
		return null;
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
		return null;
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
