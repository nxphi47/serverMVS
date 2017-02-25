package com.mvs.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.*;

/**
 * Created by nxphi on 2/24/2017.
 * product entity, sub of company, represent the product information and links to images
 */
@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String prodId;
	private String prodName;
	//FIXME: change to enum
	private String prodCategory;

	private HashMap<String, String> prodAttrs;
	private double price;
	private int stock;


	@ManyToOne
	@JsonBackReference
	private Company company;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Image> imageList;

	public Product()  {

	}

	public Product(String prodId, String prodName, String prodCategory, double price, int stock, Company company,
				   HashMap<String, String> prodAttrs, List<Image> imageList) {
		this.prodAttrs = prodAttrs;
		this.prodId = prodId;
		this.prodName = prodName;
		this.price = price;
		this.stock = stock;
		this.company = company;
		this.prodCategory = prodCategory;
		this.imageList = imageList;
	}

	public Product(String prodId, String prodName, String prodCategory, double price, int stock, Company company) {
		this(prodId, prodName, prodCategory,price, stock, company, new HashMap<>(), new ArrayList<>());
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getProdCategory() {
		return prodCategory;
	}

	public void setProdCategory(String prodCategory) {
		this.prodCategory = prodCategory;
	}

	public HashMap<String, String> getProdAttrs() {
		return prodAttrs;
	}

	public void setProdAttrs(HashMap<String, String> prodAttrs) {
		this.prodAttrs = prodAttrs;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}



	public List<Image> getImageList() {
		return imageList;
	}

	public void setImageList(List<Image> imageList) {
		this.imageList = imageList;
	}
}
