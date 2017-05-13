package com.mvs.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.*;

/**
 * Created by nxphi on 2/24/2017.
 * Company represent the company information
 * links to product DB, one To Many
 * links to user DB, one to Many
 */

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"companyName"})})
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	// intrinsic attributes
	private String companyName;
	// FIXME: change to enum
	private String category;
	private String address;
	private String postalCode;

	private double revenue;
	private double profit;
	private double capital;

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
//	@OrderBy("userName ASC") // FIXME: first consummer is the admin
//	@JsonManagedReference
	private List<User> userList;

	@ManyToMany(mappedBy = "targetingCompany", cascade = CascadeType.ALL)
	private Set<User> targetedUserList;

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
//	@OrderBy("prodId ASC")
//	@JsonManagedReference
	private List<Product> productList;

	// admin user
//	private User adminUser;

	// default constructor
	public Company() {
		userList = new ArrayList<>();
		productList = new ArrayList<>();
		targetedUserList = new HashSet<>();
		revenue = 0.0;
		profit = 0.0;
		capital = 0.0;
	}

	// the core constructor, combine all value


	public Company(String companyName, String category, String address, String postalCode) {
		this();
		this.companyName = companyName;
		this.category = category;
		this.address  = address;
		this.postalCode = postalCode;

	}

//	public Company(String companyName, String category, String address, String postalCode) {
//		this(companyName, category, address, postalCode, null);
//	}

	// to string


	@Override
	public String toString() {
		return String.format("company[name=%s, category=%s, address=%s]", companyName, category, address);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}


	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public void addUserList(User user) {
		this.userList.add(user);
	}

	//FIXME: need to test this function!
	public void removeUserFromList(User user) {
		this.userList.remove(user);
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public double getRevenue() {
		return revenue;
	}

	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public double getCapital() {
		return capital;
	}

	public void setCapital(double capital) {
		this.capital = capital;
	}

	public Set<User> getTargetedUserList() {
		return targetedUserList;
	}

	public void setTargetedUserList(Set<User> targetedUserList) {
		this.targetedUserList = targetedUserList;
	}


	//
//	public User getAdminUser() {
//		return adminUser;
//	}
//
//	public void setAdminUser(User adminUser) {
//		this.adminUser = adminUser;
//	}
}
