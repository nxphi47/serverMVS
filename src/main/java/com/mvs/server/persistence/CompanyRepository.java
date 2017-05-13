package com.mvs.server.persistence;

import com.mvs.server.model.Company;
import com.mvs.server.model.Product;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nxphi on 2/24/2017.
 * repository to control company entity
 */

@Repository
@EnableSpringConfigured
public interface CompanyRepository extends JpaRepository<Company, Long> {
	Company findByCompanyName(String companyName);
	List<Company> findByCategory(String category);
	Company findByProductListContaining(List<Product> products);
//	FIXME: find way to find by user
}
