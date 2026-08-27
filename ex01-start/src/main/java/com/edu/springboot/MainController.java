package com.edu.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
사용자가 요청하면 제일 먼저 컨트롤러가 받아 처리하게된다. 이 역할을 위해 class에 @Controller 이노테이션을 부착한다. 
*/
@Controller
public class MainController {
	/*
	root 경로로 요청이 들어오면 자동으로 호출되는 함수로, 전송방식에 따라 get/post로 구분할 수 있다.
	*/
	@GetMapping("/")
	public String main() {
		/*
		요청을 처리한 후 반환되는 문자열은 View의 경로가 된다. 우리는 타임리프 템플릿 엔진을 사용하게 되므로 여기서 반환
		된값은 resources 하위 templates에 있는 HTML파일을 가리키게 된다. */
		return "main";
	}
}
