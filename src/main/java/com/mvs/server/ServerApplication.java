package com.mvs.server;

import com.mvs.server.aspect.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;


/*
where the program start - the SpringBootApplication start here
 */

@Import(AppConfig.class)
@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ServerApplication.class, args);

	}


}
