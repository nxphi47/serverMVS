package com.mvs.server.utils.databasemanagement;

import com.mvs.server.model.Product;
import com.mvs.server.model.Sale;
import com.mvs.server.persistence.CompanyRepository;
import com.mvs.server.persistence.ImageRepository;
import com.mvs.server.persistence.ProductRepository;
import com.mvs.server.persistence.UserRepository;
import com.mvs.server.utils.transaction.PurchaseTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

/**
 * Created by fi on 4/4/2017.
 * product jpa effect implementation
 */
@Service
public class ProductJpaService implements JpaService {

	@Autowired
	private CompanyRepository companyRepo;

	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ImageRepository imageRepo;

	public ProductJpaService() {

	}

	@Override
	public Object updateJPASingle(Long id, Object obj) {

		// the list is not affected

		return updateJPASingle(productRepo.findOne(id), obj);
	}

	@Override
	public Object updateJPASingle(Object target, Object obj) {
		Product targetProduct = (Product) target;
		Product objProduct = (Product) obj;
		targetProduct.setStock(objProduct.getStock());
		targetProduct.setProdId(objProduct.getProdId());
		targetProduct.setProdCategory(objProduct.getProdCategory());
		targetProduct.setPrice(objProduct.getPrice());
		targetProduct.setStock(objProduct.getStock());

		return productRepo.save(targetProduct);
	}

	@Override
	public List updateJPAMultple(List targetList, List objList) {
		ArrayList arrayList = new ArrayList();
		for (int i = 0; i < targetList.size() && i < objList.size(); i++) {
			// this must not get index out of bound
			arrayList.add(updateJPASingle(productRepo.findOne(((Product) targetList.get(i)).getId()), objList.get(i)));
		}
		return arrayList;
	}

	@Override
	public Object insertJPASingle(Object obj) {
		return productRepo.save((Product) obj);
	}

	@Override
	public List insertJPAMultple(List objs) {
		return productRepo.save((List<Product>) objs);
	}

	@Override
	public void deleteJPASingle(Object obj) {
		productRepo.delete(((Product) obj).getId());
	}

	@Override
	public void deleteJPAMultiple(List objs) {
		for (Object object: objs) {
			deleteJPASingle(object);
		}
	}

	@Override
	public Object retrieveJPASingle(Object obj) {
		return productRepo.findOne(((Product) obj).getId());
	}

	@Override
	public List retrieveJPAMultiple(List objs) {
		return null;
	}

//	when purchase transaction is made - this will update product information as this
	// update function will also activated other observable
	@Override
	public void update(Observable o, Object arg) {
		//FIXME: to be updated by some observant
		if (o instanceof PurchaseTransaction) {
			// args must be the sales
			System.out.printf("update in product jpa called\n");
			List<Sale> sales = (List<Sale>) arg;
			for (int i = 0; i < sales.size(); i++) {
				ArrayList<Sale> theSale = new ArrayList<>();
				theSale.add(sales.get(i));
				Product product = productRepo.findBySaleListContaining(theSale);
				if (product != null) {
					product.setStock(product.getStock() - sales.get(i).getQuantity());
					productRepo.save(product);
				}
				else {
					//FIXME: fix the error statemennt
					System.out.printf("Sale %s not found product!", i);
				}
			}
		}
	}
}
