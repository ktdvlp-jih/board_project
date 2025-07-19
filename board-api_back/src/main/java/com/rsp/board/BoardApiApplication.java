package com.rsp.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BoardApiApplication {
	public static void main(String[] args) {
		System.out.println("테스트입니다.");
		SpringApplication.run(BoardApiApplication.class, args);
	}
}