package com.mvs.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

//	@JsonIgnore //FIXME: must json ignore since it will cause infinite recursion
	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
//	@OrderBy("userName ASC") // FIXME: first consummer is the admin
	@JsonManagedReference
	private List<User> userList;

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
	@OrderBy("prodId ASC")
	@JsonManagedReference
	private List<Product> productList;

	// admin user
//	private User adminUser;

	// default constructor
	public Company() {
		userList = new ArrayList<>();
		productList = new ArrayList<>();
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


	//
//	public User getAdminUser() {
//		return adminUser;
//	}
//
//	public void setAdminUser(User adminUser) {
//		this.adminUser = adminUser;
//	}
}
