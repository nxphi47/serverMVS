package com.mvs.server.persistence;

import com.mvs.server.model.Image;
import com.mvs.server.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nxphi on 2/25/2017.
 * repository for image class
 */

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findByProduct(Product product);
	List<Image> findByImgName(String imgName);
	List<Image> findByFileName(String fileName);
	List<Image> findByFolderAndFileName(String folder, String fileName);
}
