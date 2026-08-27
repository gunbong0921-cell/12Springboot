package com.edu.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Root 경로에 대한 매핑 처리 
@Controller
public class MainController {
	//Root경로에 대해 요청이 들어오면 처리 후 templates 폴더 하위 main.html로
	//처리된 내용으 전달한 후 웹브라우저에 랜더링한다. 
	@GetMapping("/")
	public String main() {
		return "main";
	}
}
