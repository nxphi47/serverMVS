package com.mvs.server.utils.databasemanagement;

import com.mvs.server.model.Company;
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
import java.util.List;
import java.util.Observable;

/**
 * Created by fi on 4/4/2017.
 * general company JPA service to control company data
 * further change or specific operation on company data must be pass to its sub-class service to perform
 */

@Service
public class CompanyJpaService implements JpaService {
	@Autowired
	private CompanyRepository companyRepo;

	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ImageRepository imageRepo;

	public CompanyJpaService() {

	}

//	update single company entity
	@Override
	public Object updateJPASingle(Long id, Object obj) {
		Company company = (Company) obj;
		Company target = companyRepo.findOne(id);

		return updateJPASingle(target, company);
	}

	@Override
	public Object updateJPASingle(Object target, Object obj) {
		Company compTarget = (Company) target;
		Company company = (Company) obj;
		compTarget.setCompanyName(company.getCompanyName());
		compTarget.setCategory(company.getCategory());
		compTarget.setAddress(company.getAddress());
		compTarget.setPostalCode(company.getPostalCode());

		// revenue - profit - capital is control by the system and user with strict limitation

		return companyRepo.save(compTarget);
	}

	@Override
	public List updateJPAMultple(List targetList, List objList) {
		ArrayList<Object> output = new ArrayList<>();
		for (int i = 0; i < targetList.size(); i++) {
			Company target = (Company) targetList.get(i);
//			Company realTarget = companyRepo.findOne(target.getId());
			Company realTarget = (Company) updateJPASingle(target.getId(), objList.get(i));
			output.add(realTarget);
		}
		return output;
	}

	// some of company field are require more security than other, it must be strictly update if want
	// hence this only affect certain variable but not replace the whole company entity
	public Object updateJPASingleStrict(Object objTarget, Object obj) {
		Company company = (Company) obj;
		Company target = (Company) objTarget;
		target.setCompanyName(company.getCompanyName());
		target.setCategory(company.getCategory());
		target.setAddress(company.getAddress());
		target.setPostalCode(company.getPostalCode());

		// revenue - profit - capital is control by the system and user with strict limitation
		target.setRevenue(company.getRevenue());
		target.setProfit(company.getProfit());
		target.setCapital(company.getCapital());
		return companyRepo.save(target);
	}

	public Object updateJPASingleStrict(Long id, Object obj) {
		return updateJPASingleStrict(companyRepo.findOne(id), obj);
	}

	@Override
	public Object insertJPASingle(Object obj) {
		Company company = (Company) obj;

		return companyRepo.save(company);
	}

	@Override
	public List insertJPAMultple(List objs) {
		return companyRepo.save((List<Company>) objs);
	}

	@Override
	public void deleteJPASingle(Object obj) {
		companyRepo.delete(((Company) obj).getId());
	}

	@Override
	public void deleteJPAMultiple(List objs) {
		for (Object obj: objs) {
			deleteJPASingle(obj);
		}
	}

	@Override
	public Object retrieveJPASingle(Object obj) {

		return companyRepo.findOne(((Company) obj).getId());
	}

	public Object retrieveJPASingle(long id) {
		return companyRepo.findOne(id);
	}

	@Override
	public List retrieveJPAMultiple(List objs) {
		return null;
	}

//	This is notify when a purchase is observed from other part of the system and it has to update the revenue
	// stock and other related field with the company
	@Override
	public void update(Observable o, Object arg) {
//		FIXME: observe the transaction and update accordingly
		if (o instanceof PurchaseTransaction) {
			System.out.printf("update in company jpa called\n");
			List<Sale> sales = (List<Sale>) arg;
//			using this for loop make the system access the DB many times and reduce the processing efficient
//			think of another method
			for (int i = 0; i < sales.size(); i++) {
//				ArrayList<Sale> theSale = new ArrayList<>();
//				theSale.add(sales.get(i));
//				Company company = productRepo.findBySaleListContaining(theSale);
				ArrayList<Product> theProduct = new ArrayList<>();
				theProduct.add(sales.get(i).getProduct());
				Company company = companyRepo.findByProductListContaining(theProduct);
				if (company != null) {
//					product.setStock(product.getStock() - sales.get(i).getQuantity());
//					productRepo.save(product);
					company.setRevenue(company.getRevenue() + sales.get(i).getTotal());
//					FIXME: this is a wrong algorithm - if do not know the import price and assumed it to be the price it self, we get no profit
//					company.setProfit(company.getProfit() + sales.get(i).getTotal() - sales.get(i).getPrice() * sales.get(i).getQuantity());
					company.setProfit(company.getProfit() + sales.get(i).getTotal());
					System.out.printf("Sale made - total: %s, price %s, quantity: %s", sales.get(i).getTotal(), sales.get(i).getPrice(), sales.get(i).getQuantity());
					// SET THE CAPITAL.....
					companyRepo.save(company);
				}
				else {
					//FIXME: fix the error statemennt
					System.out.printf("Sale %s not found product and company!\n", i);
				}
			}
		}
	}
}
