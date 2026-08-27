package com.edu.springboot;

//VO(value Object) : 데이터(값)만 저장하기 위한 용도의 클래스
class Persons {
	//멤버변수
	String name;
	int age;
	
	//생성자가 public이면 외부접근이 가능하므로 new 연산자를 통해 인스턴스를 생성할 수 있다. 
	public Persons() {
		System.out.println("public 생성자 호출됨");
	}
	
//	private Persons() {
//		System.out.println("public 생성자 호출됨");
//	}
}

public class DI_Test {
	
	/*
	강한결합(독립성 낮음) : new를 통해 직접 인스턴스를 생성한다. 이 경우 객체간의 결합도가 높기 때문에 Persons클래스의
	변화에  직접적인 영향을 받게된다. */
	public static void aPersons() {
		Persons persons1 = new Persons();
		persons1.name = "홍길동";
		persons1.age = 12;
	}
	/*
	약한결합(독립성 높음) : 미리 생성된 객체를 주입(Injection)받아 사용한다. 결핮도가 낮아지기 때문에 Persons
	클래스에 변화가 생기더라도 직접적인 영향을 받지 않는다. 또한 코드도 간결하다. */
	public static void bPersons(Persons persons2) {
		persons2.name = "전우치";
		persons2.age = 22;
	}
	
	/*
	따라서 D1(의존성주입)의 목적은 객체간의 독립성을 높이고, 결합도를 낮춰서 프로그램 전체를 간결하게 만드는것에 있다.
	개발자가 직접 인스턴스를 만들지 않고, Spring 컨테이너에 미리 생성되어 있는 빈(Bean)을 가져다 사용한다. */
}
