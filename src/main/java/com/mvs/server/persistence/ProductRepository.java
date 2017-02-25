package com.mvs.server.persistence;

import com.mvs.server.model.Company;
import com.mvs.server.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nxphi on 2/24/2017.
 * repo for product class
 */

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByCompany(Company company);
	List<Product> findByProdId(String prodId);
	List<Product> findByProdName(String prodName);
	List<Product> findByCompanyAndProdName(Company company, String prodName);
	Product findByCompanyAndProdId(Company company, String prodId);
	List<Product> findByCompanyAndProdCategory(Company company, String prodCategory);
}
