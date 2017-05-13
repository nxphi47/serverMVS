package com.mvs.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by nxphi on 2/24/2017.
 * User class  = entity to store information of user (username, password, email, company...)
 */

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "email"})})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	// intrinsic attributes - belong to core of the user
	private String fullName;

	@NotNull
	private String userName;

	private String password;

	@NotNull
	private String email;

	private boolean confirmed;

	private boolean isOwner;

	// extrinsic attributes - to be added when combining object, ->comes with @Transient tag
	@ManyToOne
//	@JsonBackReference
	@JsonIgnore
	private Company company;

	@ManyToMany(cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Company> targetingCompany;

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JsonManagedReference
	private List<Sale> saleAsOwnerList;

	@OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JsonManagedReference
	private List<Sale> saleAsBuyerList;


	public User() {
		this.isOwner = false;
		this.confirmed = false;
		this.fullName = "";
		this.password = "";
		this.email = "";
		this.userName = "";
		setSaleAsBuyerList(new ArrayList<>());
		setSaleAsOwnerList(new ArrayList<>());
		setTargetingCompany(new HashSet<>());
	}

	public User(String fullName, String userName, String password, String email, boolean confirmed, Company company) {
		this.fullName = fullName;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.confirmed = confirmed;
		this.company = company;
		this.isOwner = false;
		setSaleAsBuyerList(new ArrayList<>());
		setSaleAsOwnerList(new ArrayList<>());
		setTargetingCompany(new HashSet<>());
	}

	public User(String fullName, String userName, String password, String email, boolean confirmed) {
		this(fullName, userName, password, email, confirmed, null);
	}

	public User(String fullName, String userName, String password,  String email) {
		this(fullName, userName, password, email, false);
	}

	// to string
	@Override
	public String toString() {
		return String.format("USER[username=%s, fullName=%s, password=%s, email=%s]", userName, fullName, password, email);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

//	@JsonIgnore
	public Company getCompany() {
		return company;
	}

//	@JsonIgnore
	public void setCompany(Company company) {
		this.company = company;
	}

	public boolean isOwner() {
		return isOwner;
	}

	public void setOwner(boolean owner) {
		isOwner = owner;
	}

	public List<Sale> getSaleAsOwnerList() {
		return saleAsOwnerList;
	}

	public void setSaleAsOwnerList(List<Sale> saleAsOwnerList) {
		this.saleAsOwnerList = saleAsOwnerList;
	}

	public List<Sale> getSaleAsBuyerList() {
		return saleAsBuyerList;
	}

	public void setSaleAsBuyerList(List<Sale> saleAsBuyerList) {
		this.saleAsBuyerList = saleAsBuyerList;
	}

	public Set<Company> getTargetingCompany() {
		return targetingCompany;
	}

	public void setTargetingCompany(Set<Company> targetingCompany) {
		this.targetingCompany = targetingCompany;
	}
}
