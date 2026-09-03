package com.edu.springboot.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
클라이언트로부터 요청을 받은 후 분석하고, 이를 처리할 서비스를 호출하는 역할을 한다. 
*/
@Controller
public class JpaController {
  
  //서비스 빈 자동주입 
  @Autowired
  MemberService memberService;

  //입력.
  @GetMapping("/insert.do")
  public String insert(@RequestParam("username") String name, Model model) {
    /* 매개변수 username으로 전달된 파라미터를 name으로 받은 후 엔티티의 빌더 패턴을 통해 인스턴스를
    초기화한다. */
    Member member = Member.builder()
      .username(name)
      .createDate(LocalDateTime.now())
      .build();
    //영속성 컨텍스트에 입력 처리 
    Member result = memberService.insert(member);
    //모델객체에 저장 후 View로 전달 
    model.addAttribute("member", result);
    return "insert";
  }
  
  //개별조회
  @GetMapping("/select.do")
  public String select(@RequestParam("id") Long p_id, Model model) {
    //파라미터로 전달되는 id를 받은 후 서비스 메서드 호출 
    Optional<Member> result = memberService.select(p_id);
    //조회된 결과가 있으면 모델객체에 저장 후 View로 전달 
    if(result.isPresent()) {
      model.addAttribute("member", result.get());
    } 
    else {
      //없으면 모델객체에 null 저장 후 View로 전달 
      model.addAttribute("member", null);
    }
    return "select";
  }

  //전체조회
  @GetMapping("/selectAll.do")
  public String selectAll(Model model) {
    //2개 이상의 레코드일 수 있으므로 List로 반환 
    List<Member> result = memberService.selectAll();
    model.addAttribute("members", result);
    return "selectAll";
  }

  //삭제
  @GetMapping("/delete.do")
  public String delete(@RequestParam("id") Long pid) {
    memberService.delete(pid);
    return "delete";
  }

  //수정
  @GetMapping("/update.do")
  public String update(Member member, Model model) {
    member.setCreateDate(LocalDateTime.now());
    Member result = memberService.update(member);
    model.addAttribute("member", result);
    return "update";
  }
}
