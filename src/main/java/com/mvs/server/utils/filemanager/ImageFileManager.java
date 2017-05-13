package com.mvs.server.utils.filemanager;

import com.mvs.server.model.Image;
import com.mvs.server.persistence.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;

/**
 * Created by nxphi on 2/25/2017.
 * Class control img file and upload and download img file
 * we will work with image through this class instead of the repository it self
 */
@Service
public class ImageFileManager extends FileManager {

	@Autowired
	private ImageRepository imageRepository;

	public ImageFileManager() {
		super();
	}

	// retrieving methods
	public File getImageAsFile(Image image) {
		return getFile(image.getFolder(), image.getFileName());
	}

	public File getImageAsFile(String folder, String fileName) {
		return getFile(folder, fileName);
	}

	public ResponseEntity<Resource> getImageAsHttpBody(Image image) throws MalformedURLException {
		return getFileAsHttpBody(image.getFolder(), image.getFileName());
	}

	public ResponseEntity<Resource> getImageAsHttpBody(String folder, String fileName) throws MalformedURLException {
		return getFileAsHttpBody(folder, fileName);
	}

	public ResponseEntity<Resource> getImageAsHttpBody(String path) throws MalformedURLException {
		String[] splits = path.split("/");
		return getFileAsHttpBody(splits[splits.length - 2], splits[splits.length - 1]);
	}

	public ResponseEntity<Resource> getImageAsHttpBody(long id) throws MalformedURLException {
		Image image = this.imageRepository.findOne(id);
		return getImageAsHttpBody(image);
	}

	// uploading functions
	// this return the image it self that is save to database
	public Image uploadImageAsImage(MultipartFile file, Image image) throws IOException {
		Image newImg = this.imageRepository.save(image);
		System.out.printf("filename: %s, %s, %s\n", file.getOriginalFilename(), file.getName(), file.getSize());
		newImg.setFileName(ImgFileNameconcat(file.getOriginalFilename(), newImg.getId()));
		newImg = this.imageRepository.save(newImg);
		// uploading
		uploadMultipartAsFile(file, newImg.getFolder(), newImg.getFileName());
		return image;
	}

	public String uploadImageAsFileName(MultipartFile file, Image image) throws IOException {
		Image newImg = this.imageRepository.save(image);
		newImg.setFileName(ImgFileNameconcat(file.getOriginalFilename(), newImg.getId()));
		newImg = this.imageRepository.save(newImg);
		// uploading
//		uploadMultipartAsFile(file, newImg.getFolder(), newImg.getFileName());
		return uploadMultipartAsFileName(file, newImg.getFolder(), newImg.getFileName());
	}



	// this add abc.jpg -> abc_1.jpg
	public static String ImgFileNameconcat(String fileName, long id) {
		System.out.printf("file name %s ", fileName);
		fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + id + fileName.substring(fileName.lastIndexOf("."));
		System.out.printf("filename after %s\n", fileName);
		return fileName;
	}

	//FIXME: need validation for image file

}
