package com.mvs.server.utils.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fi on 4/4/2017.
 * factory to retrieve concrete transaction objects
 */

@Service
public class TransactionFactory extends Transaction{

	@Autowired
	private PurchaseTransaction purchaseTransaction;

	public enum Type {
		PURCHASE
	}


	//FIXME: getServiceInstance of the transfaction factory
	public Transaction getInstance(Type type) {
		switch (type) {
			case PURCHASE:
				return purchaseTransaction;
		}
		return null;
	}

	@Override
	public void importData(Object target, List... objs) {

	}

	@Override
	public Object execute() {
		return null;
	}

	@Override
	public Object execute(List[] objs) {
		return null;
	}
}
