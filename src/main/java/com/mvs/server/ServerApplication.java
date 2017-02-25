package com.mvs.server;

import com.mvs.server.utils.filemanager.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.FileSystemUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;


@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);

		// remove files
	}
//
//	@Bean
//	CommandLineRunner init(FileManager fileManager) {
//		return (args) -> {
//			fileManager.deleteAllFiles();
//		};
//	}

}
