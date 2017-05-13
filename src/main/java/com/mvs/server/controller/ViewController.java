package com.mvs.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvs.server.model.Company;
import com.mvs.server.model.User;
import com.mvs.server.persistence.CompanyRepository;
import com.mvs.server.persistence.ProductRepository;
import com.mvs.server.persistence.UserRepository;
import com.mvs.server.utils.databasemanagement.JpaServiceFactory;
import com.mvs.server.utils.filemanager.ExcelManager;
import com.mvs.server.utils.filemanager.ProdAndImgExcelManager;
import com.mvs.server.utils.transaction.TransactionFactory;
import com.mvs.server.utils.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nxphi on 2/24/2017.
 * the view controller, use to inject HTML file to webpage
 */

@Controller
@SessionAttributes("currentUser")
public class ViewController {

	private static final Logger logger = LoggerFactory.getLogger(RestServiceController.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private ProductRepository productRepository;


	@Autowired
	private UserValidator userValidator;

	@Autowired
	private ExcelManager excelManager;

	@Autowired
	private ProdAndImgExcelManager prodAndImgExcelManager;

	@Autowired
	private JpaServiceFactory jpaServiceFactory;

	@Autowired
	private TransactionFactory transactionFactory;

	@Value("${app-mode}")
	private String appMode;

//	Convert java object into json format string
	public String objToJSONStr(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}

	// home page
	@RequestMapping(value = "")
	public String index(Model model) throws IOException {

		List<Company> companies = this.companyRepository.findAll();
		if (companies != null) {
			model.addAttribute("companies", objToJSONStr(companies));
		}
		else {
			model.addAttribute("user", "{}");
		}
		return "index";
	}

	@RequestMapping(value = "/company/{compName}")
	public String company(@PathVariable String compName, Model model) throws JsonProcessingException {
		if (!model.containsAttribute("currentUser")) {
			return "redirect:/login";
		}

		User user = (User) model.asMap().get("currentUser");

		Company company = this.companyRepository.findByCompanyName(compName);
		model.addAttribute("rootUser", objToJSONStr(user));
		if (company != null) {
			model.addAttribute("company", objToJSONStr(company));
		}
		else {
			model.addAttribute("company", "{}");
		}

		return "company";
	}

	@RequestMapping(value = "/company", method = RequestMethod.GET)
	public String companyNull(Model model) throws JsonProcessingException {

		if (model.containsAttribute("currentUser")) {
			User user = (User) model.asMap().get("currentUser");
			Company targetCompany = user.getCompany();
			if (targetCompany == null) {
				return "company_create";
			}
			Company company = this.companyRepository.findOne(targetCompany.getId());
			model.addAttribute("rootUser", objToJSONStr(user));
			if (company != null) {
				return "redirect:/company/" + company.getCompanyName();
			}
			else {
				return "company_create";
			}
		}
		else {
			return "redirect:/login";
		}
	}


	@RequestMapping(value = "/analysis", method = RequestMethod.GET)
	public String analysisNull(Model model) throws JsonProcessingException {
		if (model.containsAttribute("currentUser")){
			User user = (User) model.asMap().get("currentUser");
			Company targetCompany = user.getCompany();
			if (targetCompany == null) {
				return "company_create";
			}
			Company company = this.companyRepository.findOne(targetCompany.getId());
//			model.addAttribute("rootUser", objToJSONStr(user));
			if (company != null) {
				return "redirect:/analysis/" + company.getCompanyName();
			}
			else {
				return "company_create";
			}
		}
		else {
			return "redirect:/login";
		}
	}

	@RequestMapping(value = "/analysis/{compName}", method = RequestMethod.GET)
	public String analysis(Model model, @PathVariable String compName) throws JsonProcessingException {
		if (!model.containsAttribute("currentUser")) {
			return "redirect:/login";
		}

		User user = (User) model.asMap().get("currentUser");
		Company company = this.companyRepository.findByCompanyName(compName);
		model.addAttribute("rootUser", objToJSONStr(user));
		if (company != null) {
			model.addAttribute("company", objToJSONStr(company));
		}
		else {
			model.addAttribute("company", "{}");
		}

		return "analysis";
	}


	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public String demo(Model model) throws JsonProcessingException {
		if (model.containsAttribute("currentUser")) {
			User user = (User) model.asMap().get("currentUser");
//			Company targetCompany = user.getCompany();
			model.addAttribute("rootUser", objToJSONStr(user));

			return "demo";
		}
		else {
			return "redirect:/login";
		}
	}


	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String postLogin(@RequestParam("userName") String userName, @RequestParam String password, Model model) {
		User temp = new User("", userName, password, "");
		User validated = this.userValidator.validateUser(temp);
		if (validated != null) {
			model.addAttribute("currentUser", validated);
			return "redirect:/";
		}
		else {
			model.addAttribute("error", "Username or password incorrect!");
			return "login";
		}
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
//	public String postSignp(@RequestBody User user) {
	public String postSignup(@RequestParam("userName") String userName,
							 @RequestParam("fullName") String fullName,
							 @RequestParam("password") String password,
							 @RequestParam("email") String email,
							 @RequestParam("isOwner") boolean isOwner,
							 Model model){
		User user = new User(fullName, userName, password, email);
//		user.setOwner(isOwner);
		if (this.userValidator.validateUserName(user)) {
			// null, can create
			User validated = (User) jpaServiceFactory.getServiceInstance(JpaServiceFactory.ServiceType.USER).insertJPASingle(user);
			model.addAttribute("currentUser", validated);
			return "redirect:/";
		}
		else {
//			model.addAttribute("invalidSignup", "Username occupied, choose another");
			return "login";
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(Model model, SessionStatus status) {
		status.setComplete();
//		if (model.containsAttribute("currentUser")) {
//			model.asMap().remove("currentUser");
//		}

		return "redirect:/";
	}


//	@RequestMapping("/error")
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String error() {
		return "404";
	}

}
