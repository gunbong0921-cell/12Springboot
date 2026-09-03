package com.edu.springboot.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import java.time.LocalDateTime;
import lombok.AccessLevel;

//게터, 세터, toString 등을 추가
@Data
//인수생성자. Spring MVC가 이 생성자로 바인딩하지 않도록 private로 둔다.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
//기본생성자. /update.do 커맨드 객체 바인딩은 no-args + setter를 사용한다.
@NoArgsConstructor
/* 디자인 패턴 중 빌더패턴을 적용하여 매서드 체이닝을 통해 멤버변수를 초기화 할 수 있는 기능을 제공한다.
메서드를 닷(점)으로 연결해서 여러개를 한꺼번에 호출할 수 있는 문법을 말한다. */
@Builder
/* 엔티티 설정. 즉 테이블을 name 속성에 지정한 이름으로 생성한다. 오라클은 대소문자 구분이 없으므로 
카멜케이스 형식으로 작성하면 언더바(_)로 변환된다. */
@Entity(name = "JpaMember01")
public class Member {
  //컬럼을 기본키(PK)로 설정
  @Id
  //시퀀스 생성(기본값으로 증가치는 50으로 설정됨)  
  @GeneratedValue
  /* 숫자형인 경우 long이 아닌 Long을 사용행한다. 즉 기본자료형이 아닌 Wrapper 클래스를 사용한다. */
  private Long id;
  //문자형으로 컬럼 생성 
  private String username;
  //name 속성에 지정한 이름으로 컬럼 생성 
  @Column(name = "create_date")
  private LocalDateTime createDate;
}
