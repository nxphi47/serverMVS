package com.mvs.server;

import com.mvs.server.utils.filemanager.ExcelManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServerApplicationTests {

	@Autowired
	public ExcelManager manager;

	@Test
	public void contextLoads() {
		System.out.printf("Test start!");
		ArrayList<ArrayList> returnVal = (ArrayList<ArrayList>) manager.readExcel("test", "ProductTemplate.xlsx", 0);

	}

}
