package com.mvs.server.controller;

import com.mvs.server.model.*;
import com.mvs.server.persistence.CompanyRepository;
import com.mvs.server.persistence.ImageRepository;
import com.mvs.server.persistence.ProductRepository;
import com.mvs.server.persistence.UserRepository;
import com.mvs.server.utils.databasemanagement.JpaServiceFactory;
import com.mvs.server.utils.filemanager.FileManager;
import com.mvs.server.utils.filemanager.ImageFileManager;
import com.mvs.server.utils.filemanager.ProdAndImgExcelManager;
import com.mvs.server.utils.transaction.Transaction;
import com.mvs.server.utils.transaction.TransactionFactory;
import com.mvs.server.utils.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Created by nxphi on 2/24/2017.
 * Rest controller to control user and company information
 * Only handle ajax or rest request from front end - no view request here.
 */

@RestController
@RequestMapping(value = "/rest") // Indicate the REST path for JSON and file inject
public class RestServiceController {

	private static final Logger logger = LoggerFactory.getLogger(RestServiceController.class);
	private UserRepository userRepository;
	private CompanyRepository companyRepository;
	private ProductRepository productRepository;
	private ImageRepository imageRepository;

	@Autowired
	private JpaServiceFactory jpaServiceFactory;

	@Autowired
	private TransactionFactory transactionFactory;

	@Autowired
	private FileManager fileManager;

	@Autowired
	private ImageFileManager imageFileManager;

	@Autowired
	private UserValidator userValidator;

	@Autowired
	private ProdAndImgExcelManager prodAndImgExcelManager;

	@Autowired
	public RestServiceController(UserRepository userRepository, CompanyRepository companyRepository,
								 ProductRepository productRepository, ImageRepository imageRepository ) {
		this.userRepository = userRepository;
		this.companyRepository = companyRepository;
		this.productRepository = productRepository;
		this.imageRepository = imageRepository;
	}

	// ---------------- formal retrieval mapping -------------------------

//	this group handle any request regarding retrieving some information
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
	// these request is post request relating submiing a POST form from front end
	@RequestMapping(value = "/user/create", method = RequestMethod.POST)
	public User createUser(@RequestBody User user) {
		if (userValidator.validateUserName(user)) {
			// create the user if username not exists
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

//			pushing to DB
			return this.userRepository.save(user);
		}
		else {
			return null;
		}
		// if same username, return null
	}

	@RequestMapping(value = "/company/create", method = RequestMethod.POST)
//	public Company createCompany(@RequestParam(value = "companyName") String companyName,
//								 @RequestParam(value = "category") String category,
//								 @RequestParam(value = "address") String address,
//								 @RequestParam(value = "postalCode") String postalCode) {
//		Company company = new Company(companyName, category, address, postalCode);
	public Company createCompany(@RequestBody Company company) {
		if (companyRepository.findByCompanyName(company.getCompanyName()) == null) {
			return companyRepository.save(company);
		}
		else {
			return null;
		}
	}

	@RequestMapping(value = "/test/create", method = RequestMethod.POST)
	public Company test(@RequestBody Map<String, Image> map){
		Image image = map.get("img");
		if (image == null) {
			System.out.printf("image null\n");
		}
		else {
			System.out.printf("%s %s %s\n", image.getDescript(), image.getFileName(), image.getImgName());
		}
		return companyRepository.findOne((long) 1);
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
			logger.info(String.format("Company %s add user null error", id));
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

	// add multiple products with company
	@RequestMapping(value = "/company/{id}/add/product/multiple")
	public Company uploadProductImgViaExcel(@PathVariable long id,
													   @RequestParam(value = "excelFile") MultipartFile excelFile,
													   @RequestParam(value = "imgFile[]") MultipartFile[] imgFiles) throws IOException {
//		change this one to factory

		File excel = fileManager.uploadMultipartAsFile(excelFile, String.format("%s", id), excelFile.getOriginalFilename());
		ArrayList<MultipartFile> imgArrayList = new ArrayList<>(Arrays.asList(imgFiles));
		prodAndImgExcelManager.setMyExtraMultipart(imgArrayList);
		prodAndImgExcelManager.processExcel(id, String.format("%s", id), excelFile.getOriginalFilename(), 0);

		return companyRepository.findOne(id);
	}

	// upload image to product - form file
	@RequestMapping(value = "/product/{id}/uploadImg", method = RequestMethod.POST)
	public Product uploadImgToProd(@PathVariable long id,
								   @RequestParam(value = "companyId") long companyId,
								   @RequestParam(value = "imgName") String imgName,
								   @RequestParam(value = "dateCreated") @DateTimeFormat(pattern = "yyyy-MM-d") Date date,
								   @RequestParam(value = "descript") String descript,
								   @RequestParam(value = "file[]") MultipartFile[] files) throws IOException {

		Company company = this.companyRepository.findOne(companyId);
		Product product = this.productRepository.findOne(id);
		if (company != null && product != null) {
			String folder = String.valueOf(company.getId());
			if (files == null || files.length == 0) {
				logger.error("Not file found!");
				return null;
			}
			for (MultipartFile file: files) {
				String filename = file.getOriginalFilename();
				Image image = new Image(imgName, file.getOriginalFilename(), folder, descript, date, product);

				Image newImg = imageFileManager.uploadImageAsImage(file, image);
				logger.info(String.format("Save image %s in folder %s", filename, folder));
			}
			return this.productRepository.findOne(product.getId());
		}
		else {
			// when company null
			logger.info(String.format("product %s upload img error", id));
			return null;
		}
	}

	// ___________________-- execution--_____________________
	// when a purchase is created.
	@RequestMapping(value = "/company/{compId}/user/{userId}/purchase", method = RequestMethod.POST)
	public ModelWrapper purchase(@PathVariable long userId, @PathVariable long compId, @RequestBody ModelWrapper modelWrapper) {
		User user = modelWrapper.getUser();
		List<Sale> sales = modelWrapper.getSaleList();
		List<Product> products = modelWrapper.getProductList();
		System.out.printf("Users: %s\n", user);
		System.out.printf("Sale: %s\n", sales);
		System.out.printf("prods: %s\n", products);

//		get the transaction type
		Transaction purchase = transactionFactory.getInstance(TransactionFactory.Type.PURCHASE);
		purchase.importData(user, sales, products);
		List<Sale> afterSale = (List<Sale>) purchase.execute();
		ModelWrapper returnVal = new ModelWrapper();
		returnVal.setSaleList(afterSale);
		returnVal.setCompany(companyRepository.findOne(compId));
		return returnVal;
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

	//404
}
