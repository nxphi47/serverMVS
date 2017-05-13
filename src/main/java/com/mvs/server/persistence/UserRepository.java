package com.mvs.server.persistence;

import com.mvs.server.model.Company;
import com.mvs.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nxphi on 2/24/2017.
 * Control User entity
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUserName(String username);
	User findByUserNameAndPassword(String username, String password);
	List<User> findByCompany(Company company);
}
