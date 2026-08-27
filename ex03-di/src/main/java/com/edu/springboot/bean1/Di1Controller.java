package com.edu.springboot.bean1;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Di1Controller {

	//get/post방식을 한껍너에 매핑할 수 있는 어노테이션으로 현재는 사용을 권장하지 않는다. 
	//단 호환성을 위해 사용할 수 있는 상태로 남겨져 있다.
	@RequestMapping("/di1")
	/*
	컨트롤러에서 처리된 내용을 View로 전달하지 않고, 웹브라우저에 직접 출력할때 사용하는 어노테이션.
	String을 반환하면 JSON 객체 또는 배열형식으로 출력된다. Map or List를 반환하면 JSON
	객체 또는 배열형식으로 출력된다. */
	@ResponseBody
	public String home() {
		
		//Java 설정파일을 기반으로 스프링 컨테이너의 인스턴스 생성 
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(BeanConfig.class);
		
		//주입1 : 컨테이너에 미리 생성된 빈을 주입받는다. 타입을 명시하지 않았으므로 사용할때는 형변환을 해야한다.  
		Person person1 = (Person) context.getBean("person1");
		//참조변수를 즉시 출려하면 toString()을 통해 결과가 출력된다. 
		System.out.println(person1);
		
		//주입2 : 두번째 인수를 통해 타입을 명시하면 형변환 없이 사용가능하다. 
		Person person2 = context.getBean("person2", Person.class);
		System.out.println(person2);
		
		// Context 자원 해제 (수동 생성 시 권장)
		context.close();
		
		/*
		여기서 출력되는 문자열은 View(템플릿)의 경로가 아니라, 웹브라우저에 직접 출력되는 문자열이다.
		즉 View가 별도로 필요없는 경우에 사용한다. */
		return "Dependency Injection1 (의존주입1)";
	}
}
