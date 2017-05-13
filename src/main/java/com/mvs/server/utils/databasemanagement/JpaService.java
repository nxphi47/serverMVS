package com.mvs.server.utils.databasemanagement;

import com.mvs.server.persistence.CompanyRepository;
import com.mvs.server.utils.MVSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Observer;

/**
 * Created by fi on 4/4/2017.
 *
 * this interface will affect the database with the following method that other concrete JPAserver must follow
 */

@Service
public interface JpaService extends MVSService, Observer{


	// this act as an observer
	Object updateJPASingle(Long id, Object obj);
	Object updateJPASingle(Object target, Object obj);
	List updateJPAMultple(List targetList, List objList);

	// insert
	Object insertJPASingle(Object obj);
	List insertJPAMultple(List objs);

	// delete
	void deleteJPASingle(Object obj);
	void deleteJPAMultiple(List objs);

	// retieve
	Object retrieveJPASingle(Object obj);
	List retrieveJPAMultiple(List objs);
}
