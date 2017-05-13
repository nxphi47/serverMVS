package com.mvs.server.utils.validator;

import com.mvs.server.model.User;
import com.mvs.server.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by nxphi on 2/24/2017.
 * validate the user
 */

@Component
public class UserValidator {
	@Autowired
	private UserRepository userRepository;

	public UserValidator() {

	}

	public User validateUser(User user) {
		return this.userRepository.findByUserNameAndPassword(user.getUserName(), user.getPassword());
	}

	public boolean validateUserName(User user) {
		User retrieved = this.userRepository.findByUserName(user.getUserName());
		return (retrieved == null);
	}

	public boolean validatePassword(User user) {
		User retrieved = this.userRepository.findByUserNameAndPassword(user.getUserName(), user.getPassword());
		return (retrieved == null);

	}
}
