package com.edu.springboot.bean2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Di2Controller {
	
	//단순히 타입만으로 자바빈을 자동주입 받는다. 
	@Autowired
	Student student;
	
	//타입과 이름까지 지정해서 자바빈을 자동주입 받는다. 
	@Autowired
	@Qualifier("macBook")
	Computer computer;
	
	@RequestMapping("/di2")
	@ResponseBody
	public String home() {
		System.out.println(student);
		return "Dependency Injection2 (의존주입2)"; 
	}
}
