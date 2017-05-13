package com.mvs.server.model;

import java.util.List;
import java.util.Map;

/**
 * Created by fi on 4/5/2017.
 * model wrapper for all model
 */
public class ModelWrapper {
	private Company company;
	private List<Company> companyList;
	private User user;
	private List<User> userList;
	private Product product;
	private List<Product> productList;
	private Sale sale;
	private List<Sale> saleList;
	private Image image;
	private List<Image> imageList;

	private ModelWrapper wrapper;

	// other attributes
	private Map<String, String> mapString;
	private Map<String, Double> mapNumber;

	public ModelWrapper() {

	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<Company> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<Company> companyList) {
		this.companyList = companyList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public List<Sale> getSaleList() {
		return saleList;
	}

	public void setSaleList(List<Sale> saleList) {
		this.saleList = saleList;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public List<Image> getImageList() {
		return imageList;
	}

	public void setImageList(List<Image> imageList) {
		this.imageList = imageList;
	}

	public Map<String, String> getMapString() {
		return mapString;
	}

	public void setMapString(Map<String, String> mapString) {
		this.mapString = mapString;
	}

	public Map<String, Double> getMapNumber() {
		return mapNumber;
	}

	public void setMapNumber(Map<String, Double> mapNumber) {
		this.mapNumber = mapNumber;
	}

	public ModelWrapper getWrapper() {
		return wrapper;
	}

	public void setWrapper(ModelWrapper wrapper) {
		this.wrapper = wrapper;
	}
}
