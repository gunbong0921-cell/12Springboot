package com.edu.springboot.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class JpaController {
  
  @Autowired
  MemberService memberService;

  //입력
  @GetMapping("/insert.do")
  public String insert(@RequestParam("username") String name, Model model) {
    Member member = Member.builder()
      .username(name)
      .createDate(LocalDateTime.now())
      .build();
    Member result = memberService.insert(member);
    model.addAttribute("member", result);
    return "insert";
  }
  
  //조회
  @GetMapping("/select.do")
  public String select(@RequestParam("id") Long p_id, Model model) {
    Optional<Member> result = memberService.select(p_id);
    if(result.isPresent()) {
      model.addAttribute("member", result.get());
    } 
    else {
      model.addAttribute("member", null);
    }
    return "select";
  }

  //전체조회
  @GetMapping("/selectAll.do")
  public String selectAll(Model model) {
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
