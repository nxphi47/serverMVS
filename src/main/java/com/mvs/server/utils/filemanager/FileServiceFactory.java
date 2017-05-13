package com.mvs.server.utils.filemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by fi on 4/4/2017.
 * service to create file management
 */

@Service
public class FileServiceFactory extends FileManager {

	@Autowired
	private ExcelManager excelManager;
	@Autowired
	private ImageFileManager  imageFileManager;
	@Autowired
	private FileManager fileManager;
	@Autowired
	private ProdAndImgExcelManager prodAndImgExcelManager;


	public enum Type {
		FILE, EXCEL, IMAGE, PRODUCT_IMAGE,
	}

	public FileServiceFactory() {

	}

	public FileManager getInstance(Type type) {
		switch (type) {
			case IMAGE:
				return imageFileManager;
			case FILE:
				return fileManager;
			case EXCEL:
				return excelManager;
			case PRODUCT_IMAGE:
				return prodAndImgExcelManager;

		}
		return null;
	}
}
