package com.mvs.server.controller;

import com.mvs.server.persistence.CompanyRepository;
import com.mvs.server.persistence.ProductRepository;
import com.mvs.server.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by nxphi on 2/24/2017.
 * the view controller, use to inject HTML file
 */

@Controller
//@RequestMapping(value = "")
public class ViewController {

	private static final Logger logger = LoggerFactory.getLogger(User_CompanyController.class);
	private UserRepository userRepository;
	private CompanyRepository companyRepository;
	private ProductRepository productRepository;

	@Autowired
	private HttpServletRequest httpServletRequest;


	@Value("${app-mode}")
	private String appMode;

	@RequestMapping(value = "")
	public String index(Model model) throws IOException {
		// testing

		return "index";
	}
}
