package com.mvs.server.persistence;

import com.mvs.server.model.Company;
import com.mvs.server.model.Image;
import com.mvs.server.model.Product;
import com.mvs.server.model.User;
import com.mvs.server.utils.filemanager.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//import javax.swing.text.Document;
import javax.xml.parsers.*;
import org.w3c.dom.*;
//import org.w3c.dom.Document;

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

		//TODO: get the XML parser factory to import predefine database
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
//		InputStream input= DatabaseSeeder.class.getClassLoader().getResourceAsStream("predefinedDB.xml");
		try {
			InputStream input = new ClassPathResource("static/predefinedDB.xml").getInputStream();
			if (input != null) {
				Document doc = builder.parse(input);
				doc.getDocumentElement().normalize(); // FIXME: don't know what this do
				// for company database
				NodeList compList = doc.getElementsByTagName("company");
				for (int i = 0; i < compList.getLength(); i++) {
					Node node = compList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element ele = (Element) node;
						companies.add(new Company(ele.getElementsByTagName("companyName").item(0).getTextContent(),
								ele.getElementsByTagName("category").item(0).getTextContent(),
								ele.getElementsByTagName("address").item(0).getTextContent(),
								ele.getElementsByTagName("postalCode").item(0).getTextContent()));
					}
				}

				// for users database
				NodeList userList = doc.getElementsByTagName("user");
				for (int i = 0; i < userList.getLength(); i++) {
					Node node = userList.item(i);

					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element ele = (Element) node;
						users.add(new User(ele.getElementsByTagName("fullName").item(0).getTextContent(),
								ele.getElementsByTagName("userName").item(0).getTextContent(),
								ele.getElementsByTagName("password").item(0).getTextContent(),
								ele.getElementsByTagName("email").item(0).getTextContent(),
								Boolean.parseBoolean(ele.getElementsByTagName("confirmed").item(0).getTextContent())));
						String compIdText = ele.getAttribute("compId");
						if (!compIdText.equals("")) {
							users.get(users.size() - 1).setCompany(companies.get(Integer.parseInt(compIdText)));
						}
					}
				}

				NodeList prodList = doc.getElementsByTagName("product");
				for (int i = 0; i < prodList.getLength(); i++) {
					Node node = prodList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element ele = (Element) node;
						int compId = Integer.parseInt(ele.getAttribute("compId"));
						products.add(new Product(ele.getElementsByTagName("prodId").item(0).getTextContent(),
								ele.getElementsByTagName("prodName").item(0).getTextContent(),
								ele.getElementsByTagName("prodCategory").item(0).getTextContent(),
								Double.parseDouble(ele.getElementsByTagName("price").item(0).getTextContent()),
								Integer.parseInt(ele.getElementsByTagName("stock").item(0).getTextContent()),
								companies.get(compId)));
					}
				}
				logger.info("Finish import from predefined database", DatabaseSeeder.class);
			}
		} catch (FileNotFoundException notFound) {
			logger.error("UNABLE to load predefinedDB.xml", DatabaseSeeder.class);
		}

		// saving
		companyRepository.save(companies);
		userRepository.save(users);
		productRepository.save(products);
		imageRepository.save(images);

		//FIXME: be care full with this
		deleteAllFiles();

		logger.info("Database seeder initialized");
	}
}
