package com.mvs.server.persistence;

import com.mvs.server.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nxphi on 2/24/2017.
 * repository to control company entity
 */

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
	Company findByCompanyName(String companyName);
	List<Company> findByCategory(String category);
//	FIXME: find way to find by user
}
