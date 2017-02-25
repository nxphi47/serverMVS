package com.mvs.server.persistence;

import com.mvs.server.model.Company;
import com.mvs.server.model.Image;
import com.mvs.server.model.Product;
import com.mvs.server.model.User;
import com.mvs.server.utils.filemanager.FileManager;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nxphi on 2/24/2017.
 * the command line runner to initialize the database, clear previous files...
 */

@Component
public class DatabaseSeeder implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);
	private UserRepository userRepository;
	private CompanyRepository companyRepository;
	private ProductRepository productRepository;
	private ImageRepository imageRepository;

//	@Autowired
//	private HttpServletRequest request;

	@Autowired
	private FileManager  fileManager;

	@Autowired
	public DatabaseSeeder(UserRepository userRepository, CompanyRepository companyRepository,
						  ProductRepository productRepository, ImageRepository imageRepository) {
		this.userRepository = userRepository;
		this.companyRepository = companyRepository;
		this.productRepository = productRepository;
		this.imageRepository = imageRepository;
	}

	//FIXME: this is a workaround to delete all files in uploads/ when the HttpServlet is not loaded on startup
	public void deleteAllFiles() {
		try {
			String path = new File("./src/main/webapp/uploads/").getCanonicalPath();
			logger.info("delete file: " + path);
			File file = new File(path);
			for (File subFile: file.listFiles()) {
				FileSystemUtils.deleteRecursively(subFile);
			}
		} catch (IOException io) {
			logger.info("DELETE FILES error: " + io.getMessage());
		}
	}

	@Override
	public void run(String... strings) throws Exception {
		List<User> users = new ArrayList<>();
		List<Company> companies = new ArrayList<>();
		List<Product> products = new ArrayList<>();
		List<Image> images = new ArrayList<>();

		// TODO: test database, to be removed in production
		users.add(new User("Nguyen Xuan Phi", "nxphi47", "123456", "nxphi47@gmail.com", true));
		users.add(new User("John david", "david", "123456", "david@gmail.com", true));
		users.add(new User("lara croft", "lara", "123456", "lara@gmail.com", true));

		companies.add(new Company("Bookstore", "Books", "NTU", "637720"));
		companies.add(new Company("Foodhunt", "Foods", "NTU", "637720"));

		products.add(new Product("prod_1", "Quantum Physics", "science", 12.4, 20, companies.get(0)));
		products.add(new Product("prod_2", "Computer science", "science", 12.224, 50, companies.get(0)));
		

//		users.get(0).setCompany(companies.get(0));
		// saving
		userRepository.save(users);
		companyRepository.save(companies);
		productRepository.save(products);
		imageRepository.save(images);

//
//		// set relationship, this will automatical add user to company userList
		Company comp = companyRepository.findByCompanyName("Bookstore");
		User user = userRepository.findByUserName("nxphi47");
		user.setCompany(comp);
		userRepository.save(user);

		//FIXME: be care full with this
		deleteAllFiles();

		logger.info("Database seeder initialized");
	}
}
