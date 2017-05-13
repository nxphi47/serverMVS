package com.mvs.server.aspect;

import com.mvs.server.persistence.CompanyRepository;
import com.mvs.server.persistence.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * Created by fi on 4/3/2017.
 * for testing
 */

@Component
//@Configuration
////@EnableAutoConfiguration
@ComponentScan
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class AppConfig {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CompanyRepository companyRepository;

//	@Bean
//	public ProductExcelAspect aspect () {
//		return new ProductExcelAspect(productRepository, companyRepository);
//	}

//	@Bean
//	@Autowired
//	ProductExcelAspect excelAspect;
//
//	@Bean
//	public ProductExcelAspect aspect() {
//		return new ProductExcelAspect(productRepository, companyRepository);
//	}
}
