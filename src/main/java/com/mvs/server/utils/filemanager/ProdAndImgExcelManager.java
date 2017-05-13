package com.mvs.server.utils.filemanager;

import com.mvs.server.model.Company;
import com.mvs.server.model.Image;
import com.mvs.server.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fi on 4/3/2017.
 * this is subclass of excell manager handle excel files
 * this follow the product template
 *
 * this manager tend to handle excel files alone with multiple of files
 */
@Service
public class ProdAndImgExcelManager extends ExcelManager {

	private Company myCompany;
	private Product myProduct;
	private List<Product> productList;

	@Autowired
	private ImageFileManager imageFileManager;

//	public ProdAndImgExcelManager(Company myCompany) {
//		super();
//		this.myCompany = myCompany;
//	}
	public ProdAndImgExcelManager() {
		super();
	}

	@Override
	public List readExcel(String folderName, String fileName, int sheetNumber) {
		List list = super.readExcel(folderName, fileName, sheetNumber);
		return list;
	}

	public List processForProducts(Company company, String folderName, String fileName, int sheetNumber) {
		ArrayList<ArrayList> excelResult = (ArrayList<ArrayList>) readExcel(folderName, fileName, sheetNumber);
		if (company != null) {
			ArrayList<String> firstRow = excelResult.get(0);
			ArrayList<Product> list = new ArrayList<>();
			for (int i = 0; i < excelResult.size(); i++) {
				if (i == 0) {
					continue;
				}
				ArrayList<String> row = excelResult.get(i);
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
				product.setCompany(company);
				list.add(product);
			}
			// saving
//			productRepository.save(list);
//			return companyRepository.findOne(company.getId());
			return list;
		}
		else {
			logger.info(String.format("Company is null!"));
			return new ArrayList();
		}
	}

	@Override
	public List processExcel(long attrId, String folderName, String fileName, int sheetNumber) {
//		super.processExcel(folderName, fileName, sheetNumber);
		setMyCompany(companyRepository.findOne(attrId));
		//TODO: To be handle by the aspect!
		List<Product> products = readExcel(folderName, fileName, sheetNumber);
		for (Product product: products) {
			product.setCompany(getMyCompany());
		}
		productRepository.save(products);
		setProductList(products);
		try {
			processMultipartFiles();
			return products;
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("process multiparts failed!");
			return new ArrayList();
		}
	}


	// it is images
	@Override
	public List processMultipartFiles() throws IOException {
		// FIX the algorithm, which required n^2 while it only need O(n)
		List<Image> images = new ArrayList<>();
		for (int i = 0; i < myExtraMultipart.size(); i++) {
			MultipartFile  multipartFile = (MultipartFile) myExtraMultipart.get(i);
//			String[] parts = multipartFile.getOriginalFilename().split(".");
			if (multipartFile == null) {
				System.out.printf("File null?");
				continue;
			}
			for (int j = 0; j < getProductList().size(); j++) {
				Product product = getProductList().get(j);
				if (multipartFile.getOriginalFilename().equals(product.getProdAttrs().get("imageFileName"))) {
					Image image = new Image(multipartFile.getOriginalFilename(), multipartFile.getOriginalFilename(),
							String.valueOf(getMyCompany().getId()), "product image", new Date(), product);
					Image newImg = imageFileManager.uploadImageAsImage(multipartFile, image);
					images.add(newImg);
					continue;
				}
			}
		}

		return images;
	}

	public Company getMyCompany() {
		return myCompany;
	}

	public void setMyCompany(Company myCompany) {
		this.myCompany = myCompany;
	}

	public Product getMyProduct() {
		return myProduct;
	}

	public void setMyProduct(Product myProduct) {
		this.myProduct = myProduct;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List productList) {
		this.productList = productList;
	}
}
