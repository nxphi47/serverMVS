package com.mvs.server.persistence;

import com.mvs.server.model.*;
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
import java.util.Date;
import java.util.List;

//import javax.swing.text.Document;
import javax.xml.parsers.*;
import org.w3c.dom.*;
//import org.w3c.dom.Document;

/**
 * Created by nxphi on 2/24/2017.
 * the command line runner to initialize the database base on predefined_db, clear previous files...
 * the seeder is a component on spring application
 */

@Component
public class DatabaseSeeder implements CommandLineRunner {
	private static final String predefinedDB = "./src/main/webapp/uploads/";

	private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);
	private UserRepository userRepository;
	private CompanyRepository companyRepository;
	private ProductRepository productRepository;
	private ImageRepository imageRepository;

//	@Autowired
//	private HttpServletRequest request;

//	Filemanager, as like other service, will be injected by spring via Autowired annotation.
	@Autowired
	private FileManager  fileManager;


	@Autowired
	private SaleRepository saleRepository;

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
			String path = new File(predefinedDB).getCanonicalPath();
			logger.info("delete file: " + path);
			File file = new File(path);
			for (File subFile: file.listFiles()) {
				if (!subFile.getName().equals("placeholder.jpg")) {
					FileSystemUtils.deleteRecursively(subFile);
				}
			}
		} catch (IOException io) {
			logger.info("DELETE FILES error: " + io.getMessage());
		}
	}


	/*
	This method run automatically once each time the program start
	It read a XMl file to initialize the database and delete any images and file ever stored previously
	 */
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




				// for sale which required others ID

				NodeList saleList = doc.getElementsByTagName("sale");
				List<Sale> sales = new ArrayList<>();
				for (int i = 0; i < saleList.getLength(); i++) {
					Node node = saleList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element ele = (Element) node;
						int buyerId = Integer.parseInt(ele.getAttribute("buyerId"));
						int productId = Integer.parseInt(ele.getAttribute("productId"));
//						Product product = productRepository.findOne((long) productId);
//						if (product == null) {
//							System.out.printf("Cannot find product: %s", productId);
//						}
						Sale theSale = new Sale("",
								ele.getElementsByTagName("saleCategory").item(0).getTextContent(),
								ele.getElementsByTagName("descript").item(0).getTextContent(),
								products.get(productId),
								users.get(buyerId), null,
								products.get(productId).getPrice(),
								Integer.parseInt(ele.getElementsByTagName("quantity").item(0).getTextContent()),
								0, new Date());
						theSale.setTotal(theSale.getPrice() * theSale.getQuantity());
						sales.add(theSale);
					}
				}
				// saving
				companyRepository.save(companies);
				userRepository.save(users);
				productRepository.save(products);
				imageRepository.save(images);
				saleRepository.save(sales);
				// for sale
				logger.info("Finish import from predefined database", DatabaseSeeder.class);
			}
		} catch (FileNotFoundException notFound) {
			logger.error("UNABLE to load predefinedDB.xml", DatabaseSeeder.class);
		}

		//FIXME: be care full with this
		deleteAllFiles();

		logger.info("Database seeder initialized");
	}
}
