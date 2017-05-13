package com.mvs.server.utils.transaction;

import com.mvs.server.model.Product;
import com.mvs.server.model.Sale;
import com.mvs.server.model.User;
import com.mvs.server.persistence.ProductRepository;
import com.mvs.server.persistence.SaleRepository;
import com.mvs.server.persistence.UserRepository;
import com.mvs.server.utils.databasemanagement.JpaService;
import com.mvs.server.utils.databasemanagement.JpaServiceFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fi on 4/5/2017.
 * inherit from Transaction class
 * this service is created when ever a purchase is request from front end
 * It is an observable that once execute, it will notify its observers to do their parts
 */

@Service
public class PurchaseTransaction extends Transaction {

	private User buyer;
	private List<Sale> sales;
	private List<Product> products;


	public PurchaseTransaction() {

	}

	// import will take a buyer and a list of sales at index 0
	@Override
	public void importData(Object target, List... objs) {
		try {

			// FIXME: set this as the observer
			JpaService productService = getJpaServiceFactory().getServiceInstance(JpaServiceFactory.ServiceType.PRODUCT);
			JpaService compService = getJpaServiceFactory().getServiceInstance(JpaServiceFactory.ServiceType.COMPANY);
			if (productService == null || compService == null || objs.length < 2) {
				System.out.printf("service unvailable or input obj < 2");
			}
			User buyer = (User) target;
			setBuyer(buyer);
			List<Sale> sales = (ArrayList<Sale>) objs[0];
			setSales(sales);
			List<Product> products = (ArrayList<Product>) objs[1];
			setProducts(products);
			System.out.printf("in import product: %s", getProducts());

			addObserver(getJpaServiceFactory().getServiceInstance(JpaServiceFactory.ServiceType.PRODUCT));
			addObserver(getJpaServiceFactory().getServiceInstance(JpaServiceFactory.ServiceType.COMPANY));
		} catch (IndexOutOfBoundsException indexExcep) {
			System.out.printf("Index out of bound!!!!");
			indexExcep.printStackTrace();
		}
	}

	@Override
	public Object execute() {
		// add buy to sale, add sale to product
		if (getBuyer() == null || getSales() == null || getSales().size() == 0) {
			System.out.printf("No buyer or sales in the transaction!");
			return null;
		}
		UserRepository userRepository = (UserRepository) getJpaServiceFactory().getRepoInstance(JpaServiceFactory.RepoType.USER);
		ProductRepository productRepository = (ProductRepository) getJpaServiceFactory().getRepoInstance(JpaServiceFactory.RepoType.PRODUCT);
		SaleRepository saleRepository = (SaleRepository) getJpaServiceFactory().getRepoInstance(JpaServiceFactory.RepoType.SALE);


		User buyer = userRepository.findOne(getBuyer().getId());
		// inserting sales
		for (int i = 0; i < getSales().size() && i < getProducts().size(); i++) {
			Sale sale = getSales().get(i);
			sale.setBuyer(buyer);
			System.out.printf("The sale: %s\n", sale);
			System.out.printf("The product: %s\n", getProducts().get(i));
//			if (sale.getProduct() == null) {
//				System.out.printf("product in sale %s null\n",i);
//				return null;
//			}
			Product product = productRepository.findOne(getProducts().get(i).getId());


			if (product != null) {
				double price = (sale.getPrice() == 0) ? product.getPrice() : sale.getPrice();
				double total = price * sale.getQuantity() - sale.getDiscount();
				sale.setTotal(total);
				sale.setProduct(product);
			}
			else {
				// TODO: change to another!
				System.out.printf("Product is not available to sale %s %s %s\n", sale.getDescript(), sale.getSaleCategory(), sale.getSaleName());
				getSales().remove(i);

				i--;
			}
		}
		List<Sale> afterSale = saleRepository.save(getSales());

		// insert buyer to targeted list in company

		// propagate to observer!
		setChanged();
		notifyObservers(afterSale);

		// clear
		setSales(null);
		setBuyer(null);
		return afterSale;
	}

	@Override
	public Object execute(List[] objs) {
		return null;
	}

	public User getBuyer() {
		return buyer;
	}

	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}

	public List<Sale> getSales() {
		return sales;
	}

	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
