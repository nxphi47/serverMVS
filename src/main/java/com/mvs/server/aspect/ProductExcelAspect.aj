package com.mvs.server.aspect;

import com.mvs.server.model.Company;
import com.mvs.server.model.Product;
import com.mvs.server.persistence.CompanyRepository;
import com.mvs.server.persistence.ProductRepository;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fi on 4/3/2017.
 * Aspect of product uploading file
 * Still don't know why the aspect is loaded
 */

@Aspect
@Component
@Configurable
public class ProductExcelAspect {

//	@Autowired
	private static final Logger logger = LoggerFactory.getLogger(ProductExcelAspect.class);

	@Autowired
//	@Resource(name = "com.mvs.server.persistence.ProductRepository")
	private ProductRepository productRepository;
//
	@Autowired
//	@Resource(name = "com.mvs.server.persistence.CompanyRepository")
	private CompanyRepository companyRepository;

	public ProductExcelAspect()  {

	}

//	@Autowired
	public ProductExcelAspect (ProductRepository productRepository, CompanyRepository companyRepository) {
		this.productRepository = productRepository;
		this.companyRepository = companyRepository;
		if (this.productRepository == null || this.companyRepository == null) {
			System.out.printf("Product or company repo null!\n");
		}
	}
//


	// TODO: the aspect use to config to excel file and return back to readExcel
	@AfterReturning(
			pointcut = "execution (java.util.List com.mvs.server.utils.filemanager.ProdAndImgExcelManager.readExcel(..))",
			returning = "result")
	public void productExcelAfterReturn(List result) {
		// FIXME: after return
		System.out.printf("After returning enter");
//		List excelResult = result;
		ArrayList<String> firstRow = (ArrayList<String>) result.get(0);
		ArrayList<Product> list = new ArrayList<>();
		for (int i = 0; i < result.size(); i++) {
			if (i == 0) {
				continue;
			}
			ArrayList<String> row = (ArrayList<String>) result.get(i);
			Product product = new Product();
			HashMap<String, String> map;
			for (int j = 0; j < row.size(); j++) {
				String val = row.get(j);
				switch (j) {
					case 0:
						product.setProdId(val);
						break;
					case 1:
						product.setProdName(val);
						break;
					case 2:
						product.setProdCategory(val);
						break;
					case 3:
						product.setPrice(Double.parseDouble(val));
						break;
					case 4:
						product.setStock((int) Double.parseDouble(val));
						break;
					case 5:
						//TODO: implement later
						System.out.printf("Image file: %s\n", val);
						map = product.getProdAttrs();
						map.put("imageFileName", val);
						product.setProdAttrs(map);
						break;
					default:
						map = product.getProdAttrs();
						map.put(firstRow.get(j), val);
						product.setProdAttrs(map);
						break;

				}

			}
			list.add(product);
		}
		result.clear();
		for (Product product: list) {
			result.add(product);
		}
	}

	// this base on the excel file
	public Company saveProducts(long id, ArrayList<Product> products) {
		// testing if
		if (companyRepository == null) {
			System.out.printf("Repo null!\n");
			return  null;
		}
		Company company = companyRepository.findByCompanyName("BookStore");
		if (company == null) {
			System.out.printf("\nThe id %d company invalid\n", id);
			return  null;
		}
//		for (Product product: products) {
//			if (productRepository.findByCompanyAndProdId(company, product.getProdId()) == null) {
//				// if not then insert
//
//			}
//		}
		for (Product prod: products) {
			prod.setCompany(company);
		}
		productRepository.save(products);
		return companyRepository.findByCompanyName("BookStore");
	}


	@Before("execution (* com.mvs.server.utils.filemanager.ExcelManager.readExcel(java.lang.String, java.lang.String, int)) && args(folderName, fileName, sheetNumber)")
	public void beforeReadExcel(String folderName, String fileName, int sheetNumber) {
		System.out.printf("before aspect: %s %s\n", folderName, fileName);
//		logger.info("before read excel");
	}

	@After("execution (* *.index(..))")
	public void beforeIndex() {
		logger.info("before read excelxxxxx\n");
//		System.out.printf("before index!");
	}
}
