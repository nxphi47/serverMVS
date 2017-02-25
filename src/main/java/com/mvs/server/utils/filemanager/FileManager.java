package com.mvs.server.utils.filemanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by nxphi on 2/24/2017.
 * File manager upload, retrieve, update, delete files in /uploads
 */

@Service
public class FileManager {
	protected static final String rootPath = "/uploads";
	protected static final Logger logger = LoggerFactory.getLogger(FileManager.class);

	@Autowired
	private HttpServletRequest request;

	public FileManager() {

	}

	public void deleteAllFiles() {
		// this is a work around to find uploads/ when servletContext is not loaded on startup
		try {
			String path = new File("./src/main/webapp/uploads/").getCanonicalPath();
			FileSystemUtils.deleteRecursively(new File(path + "/"));
		} catch (IOException io) {
			logger.info("DELETE FILES error: " + io.getMessage());
		}
	}

	// upload multipart file, return file TODO: folder name, without "/",
	public File uploadMultipartAsFile(MultipartFile file, String targetFolder, String targetName) throws IOException {
		if (!file.isEmpty()) {
			try {
				String pathFolder = rootPath + "/" + targetFolder + "/";
				String realPathFolder = request.getServletContext().getRealPath(pathFolder);
				if (! new File(realPathFolder).exists()) {
					new File(realPathFolder).mkdir();
				}
				// transfering
				String realPath = realPathFolder + targetName;
				// check for exsistecel
				if (! new File(realPath).exists()) {
					file.transferTo(new File(realPath));
					return new File(realPath);
				}
				else {
					//FIXME: throw exception
					throw new IOException(String.format("Uploading file name existed: %s", targetFolder + "/" + targetName));
				}
			}
			catch (Exception e) {
				throw e;
			}
		}
		else {
			throw new IOException(String.format("Uploading file name not found: %s", targetFolder + "/" + targetName));
//			return null;
		}
	}
	// as path string
	public String uploadMultipartAsString(MultipartFile file, String targetFolder, String targetName) throws IOException {
		File uploadedFile = uploadMultipartAsFile(file, targetFolder, targetName);
		return uploadedFile.getAbsolutePath();
	}
	public String uploadMultipartAsFileName(MultipartFile file, String targetFolder, String targetName) throws IOException {
		File uploadedFile = uploadMultipartAsFile(file, targetFolder, targetName);
		return uploadedFile.getName();
	}

	// ----- retrieving files -------------
	public File getFile(String folderName, String filename) {
		// path after uploads/

		return new File(request.getServletContext().getRealPath(rootPath + "/" + folderName + "/" + filename));
	}

	// spring resource -> direct to response
	public ResponseEntity<Resource> getFileAsHttpBody (String folderName, String filename) throws MalformedURLException {
		Path location = Paths.get(request.getServletContext().getRealPath(rootPath + "/" + folderName));
		Path file = location.resolve(filename);

		Resource resource = new UrlResource(file.toUri());
		if (resource.exists() && resource.isReadable()) {
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		}
		else {
			logger.info(String.format("Unable to find file: %s/%s", folderName, filename));
			return null;
		}
	}
}
