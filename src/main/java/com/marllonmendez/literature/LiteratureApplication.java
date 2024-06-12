package com.marllonmendez.literature;

import com.marllonmendez.literature.display.Display;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteratureApplication implements CommandLineRunner {

	@Autowired
	private Display display;

	public LiteratureApplication(Display display) {
		this.display = display;
	}

	public static void main(String[] args) {
		SpringApplication.run(LiteratureApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		display.options();
	}

}
