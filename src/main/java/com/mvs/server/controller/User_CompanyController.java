package com.mvs.server.controller;

import com.mvs.server.model.Company;
import com.mvs.server.model.Image;
import com.mvs.server.model.Product;
import com.mvs.server.model.User;
import com.mvs.server.persistence.CompanyRepository;
import com.mvs.server.persistence.ImageRepository;
import com.mvs.server.persistence.ProductRepository;
import com.mvs.server.persistence.UserRepository;
import com.mvs.server.utils.filemanager.FileManager;
import com.mvs.server.utils.filemanager.ImageFileManager;
import com.mvs.server.utils.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Created by nxphi on 2/24/2017.
 * Rest controller to control user and company information
 */

@RestController
@RequestMapping(value = "/rest") // Indicate the REST path for JSON and file inject
public class User_CompanyController {

	private static final Logger logger = LoggerFactory.getLogger(User_CompanyController.class);
	private UserRepository userRepository;
	private CompanyRepository companyRepository;
	private ProductRepository productRepository;
	private ImageRepository imageRepository;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private FileManager fileManager;

	@Autowired
	private ImageFileManager imageFileManager;

	@Autowired
	private UserValidator userValidator;

	@Autowired
	public User_CompanyController(UserRepository userRepository, CompanyRepository companyRepository,
								  ProductRepository productRepository, ImageRepository imageRepository ) {
		this.userRepository = userRepository;
		this.companyRepository = companyRepository;
		this.productRepository = productRepository;
		this.imageRepository = imageRepository;
	}

	// ---------------- formal retrieval mapping -------------------------

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> getUsers() {
		return this.userRepository.findAll();
	}

	@RequestMapping(value = "/companies", method = RequestMethod.GET)
	public List<Company> getCompanies() {
		return this.companyRepository.findAll();

	}

	@RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
	public User getUserByUserName(@PathVariable String username) {
		return this.userRepository.findByUserName(username);
	}

	@RequestMapping(value = "/company/{id}/users", method = RequestMethod.GET)
	public List<User> getUserFromCompany(@PathVariable long id) {
		Company comp = this.companyRepository.findOne(id);
		if (comp != null) {
			return comp.getUserList();
		}
		else {
			logger.info(String.format("ERROR retrieve company id %s", id));
			return new ArrayList<>();
		}
	}
	@RequestMapping(value = "/company/{id}", method = RequestMethod.GET)
	public Company getCompany(@PathVariable long id) {
		return this.companyRepository.findOne(id);
	}

	// retrieving any file in uploads
	@RequestMapping(value = "/file", method = RequestMethod.GET)
	public ResponseEntity<Resource> getFile(@RequestParam(value = "folder") String folder, @RequestParam(value = "filename") String filename) throws MalformedURLException {
		return fileManager.getFileAsHttpBody(folder, filename);
	}

	@RequestMapping(value = "/images", method = RequestMethod.GET)
	public List<Image> getImages() {
		return imageRepository.findAll();
	}

	// ----------------- formal POST submision mapping ---------------------
	@RequestMapping(value = "/user/create", method = RequestMethod.POST)
	public User createUser(@RequestBody User user) {
		if (userValidator.validateUserName(user)) {
			// create
			if (user.getCompany() != null) {
				Company company = this.companyRepository.findOne(user.getCompany().getId());
				if (company != null) {
					user.setCompany(company);
				}
				else {
					logger.info("Create user: found company but not from database");
				}
			}
			else {
				logger.info("Create user: get company is null");
			}
			//Save without confirming
			User afterSave = this.userRepository.save(user);
			return afterSave;
		}
		else {
			return null;
		}
		// if same username, return null
	}

	// add to an existing user
	@RequestMapping(value = "/company/{id}/add/user", method = RequestMethod.POST)
	public Company addUserToCompany(@PathVariable long id, @RequestBody User user) {
		// look for username
		if (user != null) {
			User target = this.userRepository.findByUserName(user.getUserName());
			target.setCompany(this.companyRepository.findOne(id));
			this.userRepository.save(target);
			return this.companyRepository.getOne(id);
		}
		else {
			logger.info(String.format("Company %s add user %s error", id, user));
			return null;
		}
	}

	// add new product to company
	@RequestMapping(value = "/company/{id}/add/product", method = RequestMethod.POST)
	public Company addProdToCompany(@PathVariable long id, @RequestBody Product product) {
		if (product != null) {
			Company company = this.companyRepository.findOne(id);
			if (this.productRepository.findByCompanyAndProdId(company, product.getProdId()) == null) {
				// not found replicate prodId
				product.setCompany(company);
				this.productRepository.save(product);
				return this.companyRepository.findOne(id);
			}
			else {
				logger.info(String.format("Add product duplicate: prodId %s, company %s", product.getProdId(), id));
				return null;
			}
		}
		else {
			logger.info(String.format("Add prodcut to company %s failed as product null", id));
			return null;
		}
	}

	// upload image to product - form file
	@RequestMapping(value = "/product/{id}/uploadImg", method = RequestMethod.POST)
	public Product uploadImgToProd(@PathVariable long id,
								   @RequestParam(value = "file") MultipartFile file,
								   @RequestParam(value = "companyId") long companyId,
								   @RequestParam(value = "imgName") String imgName,
								   @RequestParam(value = "dateCreated") @DateTimeFormat(pattern = "yyyy-MM-d") Date date,
								   @RequestParam(value = "descript") String descript) throws IOException {

		Company company = this.companyRepository.findOne(companyId);
		Product product = this.productRepository.findOne(id);
//		Image image =  new Image(imgName, file.getOriginalFilename())
		if (company != null && product != null) {
			String folder = String.valueOf(company.getId());
			String filename = file.getOriginalFilename();
			Image image = new Image(imgName, file.getOriginalFilename(), folder, descript, date, product);

			Image newImg = imageFileManager.uploadImageAsImage(file, image);
//			List<Image> images = product.getImageList();
//			images.add(newImg);
//			product.setImageList(images);
			logger.info(String.format("Save image %s in folder %s", filename, folder));
			return this.productRepository.findOne(product.getId());
		}
		else {
			// when company null
			logger.info(String.format("product %s upload img error", id));
			return null;
		}
	}


	// controller
	@RequestMapping(value = "")
	public String getDefault() {
		return "hello world";
	}


	@RequestMapping(value = "/companies/addUser", method = RequestMethod.GET)
	public List<Company> addUSer(@RequestParam(value = "username") String userName, @RequestParam(value = "companyName") String companyName) {
		User user = this.userRepository.findByUserName(userName);
		Company company = this.companyRepository.findByCompanyName(companyName);
		if (user != null && company != null) {
			//FIXME: only need to set user.setCompany(), the reverse is automatically added!!!!!!!!
			user.setCompany(company);
			this.userRepository.save(user);
//			logger.info("add user found and save!: ");
		}
		else {
			logger.info("add user found null!");
		}
		return this.companyRepository.findAll();
	}


	@RequestMapping(value = "/testFile", method = RequestMethod.GET)
	public ResponseEntity<Resource> getTestFile() throws MalformedURLException {
		return fileManager.getFileAsHttpBody("test", "testFile.png");
	}
}
