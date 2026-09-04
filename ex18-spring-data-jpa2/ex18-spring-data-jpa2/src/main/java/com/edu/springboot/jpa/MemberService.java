package com.edu.springboot.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/* 컨트롤러가 받은 요청을 전달받아 실제 비즈니스 로직을 수행하는 역할을 하는 서비스 클래스를 어노테이션으로 명시 */
@Service
public class MemberService {
  
  //DAO 인터페이스를 자동주입받아 사용 
  @Autowired
  private MemberRepository memberRepository;

  //입력
  public Member insert() {
    Member member;

    member = Member.builder().name("이순신").email("test1@test.com").build();
    memberRepository.save(member);
    member = Member.builder().name("이순신").email("test1@test.com").build();
    memberRepository.save(member);
    member = Member.builder().name("강감찬").email("test2@test.com").build();
    memberRepository.save(member);
    member = Member.builder().name("을지문덕").email("test3@test.com").build();
    memberRepository.save(member);
    member = Member.builder().name("계백").email("test4@test.com").build();
    memberRepository.save(member);
    member = Member.builder().name("김유신").email("test5@test.com").build();
    memberRepository.save(member);
    member = Member.builder().name("연개소문").email("test6@test.com").build();
    memberRepository.save(member);
    member = Member.builder().name("양만춘").email("test7@test.com").build();
    memberRepository.save(member);
    member = Member.builder().name("김종서").email("test8@test.com").build();
    memberRepository.save(member);
    member = Member.builder().name("최영").email("test9@test.com").build();
    memberRepository.save(member);

    return member;
  }
  //전체조회
  public List<Member> selectAll() {
    //조건 없이 전체 레코드를 select한 후 반환 
    return memberRepository.findAll();
  }

  //아이디로 검색
  public Optional<Member> selectId(Long search) {
    Optional<Member> member = memberRepository.findById(search);
    return member;
  }

  //이름으로 검색
  public List<Member> selectName(String search) {
    // Optional<Member> member = memberRepository.findByName(search);
    List<Member> member = memberRepository.findByName(search);
    return member;
  }

  //이메일로 검색
  public List<Member> selectEmail(String search) {
    // Optional<Member> member = memberRepository.findByEmail(search);
    List<Member> member = memberRepository.findByEmail(search);
    return member;
  }

  //이름으로 검색(Like)
  public List<Member> selectNameLike(String search) {
    List<Member> member = memberRepository.findByNameLike(search);
    return member;
  }

  //이름으로 검색(Like) - 내림차순
  public List<Member> selectNameLikeOrderByNameDesc(String search) {
    List<Member> member = memberRepository.findByNameLikeOrderByNameDesc(search);
    return member;
  }

  //sort 빈을 통해 정렬
  public List<Member> selectNameLike(String search, Sort sort) {
    List<Member> member = memberRepository.findByNameLike(search, sort);
    return member;
  }
}
