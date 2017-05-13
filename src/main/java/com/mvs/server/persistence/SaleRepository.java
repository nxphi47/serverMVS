package com.mvs.server.persistence;

import com.mvs.server.model.Company;
import com.mvs.server.model.Product;
import com.mvs.server.model.Sale;
import com.mvs.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Created by fi on 4/4/2017.
 */

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
	List<Sale> findByProduct(Product product);
	List<Sale> findByProductAndBuyer(Product product, User user);
	List<Sale> findByBuyerIn(Collection<User> users);
	List<Sale> findByProductIn(Collection<Product> products);
	List<Sale> findByProductAndBuyerIn(Product product, Collection<User> users);
	List<Sale> findBySaleName(String saleName);
}
