package com.edu.springboot.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/* 컨트롤러가 받은 요청을 전달받아 실제 비즈니스 로직을 수행하는 역할을 하는 서비스 클래스를 어노테이션으로 명시 */
@Service
public class MemberService {
  
  //DAO 인터페이스를 자동주입받아 사용 
  @Autowired
  private MemberRepository dao;

  //입력
  public Member insert(Member member) {
    //DAO의 save() 메서드를 호출하여 insert 처리 
    Member resultMember = dao.save(member);
    return resultMember;
  }

  //개별조회
  public Optional<Member> select(Long id) {
    //findById() 를 통해 하나의 레코드를 select한 후 반환 
    Optional<Member> member = dao.findById(id);
    return member;
  }

  //전체조회
  public List<Member> selectAll() {
    //조건 없이 전체 레코드를 select한 후 반환 
    return dao.findAll();
  }

  //삭제
  public void delete(Long id) {
    //인수로 전달된 id에 해당하는 레코드를 delete 처리
    dao.deleteById(id);
  }

  //수정
  public Member update(Member member) {
    /*
    insert와 동일한 메서드 사용. 동일한 키값이 있으면 update, 없으면 insert 처리 
    */
    Member resultMember = dao.save(member);
    return resultMember;
  }
}
