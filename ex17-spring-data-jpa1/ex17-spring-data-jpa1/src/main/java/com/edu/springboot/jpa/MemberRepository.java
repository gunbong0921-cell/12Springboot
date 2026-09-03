package com.edu.springboot.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
  //제네릭 타입매개변수 : long이 아니라 Long으로 작성
  //기본적인 Create, Read, Update, Delete 자동으로 생성  
}
