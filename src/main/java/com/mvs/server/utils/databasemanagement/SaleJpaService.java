package com.mvs.server.utils.databasemanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Observable;

/**
 * Created by fi on 4/4/2017.
 * sale JPA service will control sale database request
 */

@Service
public class SaleJpaService implements JpaService{

	@Autowired
	public JpaServiceFactory jpaServiceFactory;

	public SaleJpaService() {

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
