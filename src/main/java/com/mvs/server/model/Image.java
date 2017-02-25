package com.mvs.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by nxphi on 2/25/2017.
 * This Entity store information about products (many to one)
 */

@Entity
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String imgName;
	private String fileName; //filename + "_" + id + png
	private String folder;

	private String descript;

	private Date dateCreated;

	@ManyToOne
	@JsonBackReference
	private Product product;

	//default constructor
	public Image() {

	}

	public Image(String imgName, String fileName, String folder, String descript, Date dateCreated, Product product) {
		setImgName(imgName);
		setFileName(fileName);
		setFolder(folder);
		setDateCreated(dateCreated);
		setProduct(product);
		setDescript(descript);
	}

	public Image(String imgName, String fileName, String folder, String descript, Date dateCreated) {
		this(imgName, fileName, folder, descript, dateCreated, null);
	}

	public Image(String imgName, String fileName, String folder, String descript) {
		this(imgName, fileName, folder, descript, new Date());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}
}
