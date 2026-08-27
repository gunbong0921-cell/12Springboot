package com.edu.springboot.exam1;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
lombok에서 제공하는 어노테이션으로 멤버변수에 대한 getter, setter 생성자와 Object 클래스에서 제공하는 equals(), toString()까지 
오버라이딩 해준다. */
@Data
//인자생성자를 자동으로 생성한다.   
@AllArgsConstructor
public class UserDTO {
	private String name;
	private int age;
}
