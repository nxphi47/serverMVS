package com.mvs.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by fi on 4/4/2017.
 * sale is an entity of that handle the purchase transaction
 */

@Entity
public class Sale {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String saleName;
	private String saleCategory; //????

	@ManyToOne
//	@JsonBackReference
	@JsonIgnore
	private User owner;

	@ManyToOne
//	@JsonBackReference
	@JsonIgnore
	private User buyer;


	@ManyToOne
//	@JsonBackReference
	@JsonIgnore
	private Product product;

	private Date date;
	private String descript;

	private double price;
	private int quantity;
	private double discount;

	private double total;

	public Sale() {
		saleName = "";
		saleCategory = "";
		price = 0.0;
		quantity = 0;
		discount = 0;
		total = 0;
		setDate(new Date());
	}

	public Sale(String saleName, String category, String descript,
				Product product, User buyer, User owner, double price, int quantity, double discount, Date date) {
		this();
		setSaleName(saleName);
		setSaleCategory(category);
		setDescript(descript);
		setProduct(product);
		setBuyer(buyer);
		setOwner(owner);
		setPrice(price);
		setQuantity(quantity);
		setDiscount(discount);
		setDate(date);
	}

	public Sale(String descript,
				Product product, User buyer, User owner, double price, int quantity, double discount) {
		this("", "", descript, product, buyer, owner, price, quantity, discount, new Date());
	}

	public Sale(Product product, User buyer, int quantity, double discount) {
		this("",  product, buyer, null, product.getPrice(), quantity, discount);
	}

	public Sale(Product product, User buyer, int quantity) {
		this(product, buyer, quantity, 0);
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}

	public String getSaleCategory() {
		return saleCategory;
	}

	public void setSaleCategory(String saleCategory) {
		this.saleCategory = saleCategory;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public User getBuyer() {
		return buyer;
	}

	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}


	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {

		this.product = product;
	}

	@Override
	public String toString() {
		return String.format("sale[price=%s, quantity=%s, buyer=%s, product=%s]", getPrice(), getQuantity(), getBuyer(), getProduct());
	}
}
