package com.edu.spring.jpa;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class MemberService {
  
  @Autowired
  private MemberRepository memberRepository;

  public List<Member> selectMembers1(String search) {
    List<Member> members = memberRepository.findMembers(search);
    return members;
  }

  public List<Member> selectMembers2(String search, Sort sort) {
    List<Member> members = memberRepository.findMembers(search, sort);
    return members;
  }

  public Page<Member> selectMembers3(String search, Pageable pageable) {
    Page<Member> members = memberRepository.findMembers(search, pageable);
    return members;
  }

  public List<Member> selectMembers4(String search) {
    List<Member> members = memberRepository.findMembersNative(search);
    return members;
  }
}
