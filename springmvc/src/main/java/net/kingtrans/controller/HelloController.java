package net.kingtrans.controller;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {
	@RequestMapping("/index")
	public String hello(ServletContext sc) {
		 
		return "index";
	}
	@RequestMapping("/")
	public String hello1() {
		return "index";
	}
	
}
