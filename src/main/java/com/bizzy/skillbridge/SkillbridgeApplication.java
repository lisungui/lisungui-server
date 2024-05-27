package com.bizzy.skillbridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication(scanBasePackages = {"com.bizzy.skillbridge"})
public class SkillbridgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillbridgeApplication.class, args);
	}

	@GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String helloWorld() {
		return "The application is running.";
	}

}
