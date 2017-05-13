package com.mvs.server.utils.databasemanagement;

import com.mvs.server.model.Image;
import com.mvs.server.model.Product;
import com.mvs.server.persistence.CompanyRepository;
import com.mvs.server.persistence.ImageRepository;
import com.mvs.server.persistence.ProductRepository;
import com.mvs.server.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by fi on 4/4/2017.
 *
 * controll Image jpa service - update image database
 */
@Service
public class ImageJpaService implements JpaService {

	@Autowired
	private CompanyRepository companyRepo;

	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ImageRepository imageRepo;


	public ImageJpaService() {

	}


	@Override
	public Object updateJPASingle(Long id, Object obj) {
		return updateJPASingle(imageRepo.getOne(id), obj);
	}

	@Override
	public Object updateJPASingle(Object target, Object obj) {
		Image imageTarget = (Image) target;
		Image imageObj = (Image) obj;
		((Image) target).setDescript(((Image) obj).getDescript());
		((Image) target).setImgName(((Image) obj).getImgName());

		// product and other things is strict
		// filename and folder will affect the things not to create
//		((Image) target).setProduct();
		return imageRepo.save((Image) target);
	}

	@Override
	public List updateJPAMultple(List targetList, List objList) {
		ArrayList arrayList = new ArrayList();
		for (int i = 0; i < targetList.size() && i < objList.size(); i++) {
			// this must not get index out of bound
			arrayList.add(updateJPASingle(imageRepo.findOne(((Image) targetList.get(i)).getId()), objList.get(i)));
		}
		return arrayList;
	}

	@Override
	public Object insertJPASingle(Object obj) {
		return imageRepo.save((Image) obj);
	}

	@Override
	public List insertJPAMultple(List objs) {
		return imageRepo.save((List<Image>) objs);
	}

	@Override
	public void deleteJPASingle(Object obj) {
		imageRepo.delete(((Image) obj).getId());
	}

	@Override
	public void deleteJPAMultiple(List objs) {
		for (Object object: objs) {
			deleteJPASingle(object);
		}
	}

	@Override
	public Object retrieveJPASingle(Object obj) {
		return null;
	}

	@Override
	public List retrieveJPAMultiple(List objs) {
		return null;
	}

	@Override
	public void update(Observable o, Object arg) {
		// listener!!!!
	}
}
