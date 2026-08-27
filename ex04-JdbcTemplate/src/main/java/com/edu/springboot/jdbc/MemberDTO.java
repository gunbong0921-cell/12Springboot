package com.edu.springboot.jdbc;

import lombok.Data;

//member테이블의 컬럼과 일치하도록 멤버변수를 정의. 
@Data
public class MemberDTO {
	private String id;
	private String pass;
	private String name;
	private String regidate;
}
