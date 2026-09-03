package com.edu.springboot.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/*
DAO 역할의 인터페이스
타입 매개변수는 테이블(엔티티)로 사용할 member와 기본키로 사용할 필드의 타입을 명시한다. long은 기본자료형이므로 
Wrapper 클래스인 Long으로 작성한다.
*/
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
  //제네릭 타입매개변수 : long이 아니라 Long으로 작성
  //기본적인 Create, Read, Update, Delete 자동으로 생성  
}
