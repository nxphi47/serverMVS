package com.mvs.server.utils.transaction;

import com.mvs.server.utils.databasemanagement.JpaServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by fi on 4/4/2017.
 * transaction is used directly from the controller!
 * Transaction is an abstract class and must be inherited
 *
 * importData method just import neccessary data
 * execute method will execute the transaction request
 */
@Service
public abstract class Transaction extends Observable{

	@Autowired
	private JpaServiceFactory jpaServiceFactory;


	public Transaction() {
		super();
	}

	public abstract void importData(Object target, List... objs);
	public abstract Object execute();
	public abstract Object execute(List... objs);


	public JpaServiceFactory getJpaServiceFactory() {
		return jpaServiceFactory;
	}

	public void setJpaServiceFactory(JpaServiceFactory jpaServiceFactory) {
		this.jpaServiceFactory = jpaServiceFactory;
	}
}
