package com.edu.springboot.bean2;

import org.springframework.beans.factory.annotation.Value; // Spring의 Value import
import org.springframework.stereotype.Component;

/*
스프링 컨테이너를 구동할때 이름을 지정해서 빈을 생성한다. Computer macBook = new macBook()와 동일함. */
@Component("macBook")
public class Computer {

	//멤버변수를 지정한 값으로 초기화 
	@Value("M1")
	private String cpu;
	
	@Override
	public String toString() {
		return "Notebook [cpu=" + cpu + "]";
	}
}
