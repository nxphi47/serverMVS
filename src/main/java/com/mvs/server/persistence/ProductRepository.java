package com.mvs.server.persistence;

import com.mvs.server.model.Company;
import com.mvs.server.model.Product;
import com.mvs.server.model.Sale;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sun.rmi.runtime.Log;

import java.util.Collection;
import java.util.List;

/**
 * Created by nxphi on 2/24/2017.
 * repo for product class
 */

@Repository
@EnableSpringConfigured
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByCompany(Company company);
	List<Product> findByProdId(String prodId);
	List<Product> findByProdName(String prodName);
	List<Product> findByCompanyAndProdName(Company company, String prodName);
	Product findByCompanyAndProdId(Company company, String prodId);
	List<Product> findByCompanyAndProdCategory(Company company, String prodCategory);
	Product findBySaleListContaining(List<Sale> sale);
	List<Product> findAllByOrderByProdId();
	List<Product> findByIdIn(Collection<Long> ids);

//	Product findOne(int productId);
}
