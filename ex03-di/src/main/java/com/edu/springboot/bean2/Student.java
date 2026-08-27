package com.edu.springboot.bean2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/*
@Component : 스프링 컨테이너 시작시 자동으로 빈 생성. 이름을 별도로 지정하지 않으면 클래스명의
첫글자를 소문자로 변경한 student라는 이름으로 생성하게된다. */
@Component
public class Student {

	//@Value : 지정한 값으로 멤버변수 초기화 
	@Value("이순신")
	private String name;
	
	@Value("30")
	private int age;
	
	/*
	@Autowired : 컨테이너에 생성된 빈을 자동으로 주입받음
	@Qualifier : 자동주입을 받을때 빈의 이름까지 지정해서 받을 수 있다. 별도의 지정이 없다면
	동일한 타입인지 확인 후 빈을 중비받게 된다. */
	@Autowired
	@Qualifier("macBook")
	private Computer notebook;
	
	@Override
	public String toString() {
		return "Student [name=" + name + ", age=" + age + ", notebook=" + notebook + "]";
	}
}
