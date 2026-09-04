package com.edu.springboot.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
  public String insert(Model model) {
    //레코드 10개입력
    memberService.insert();
    //View에서 출력할 내용을 Model에 저장 
    model.addAttribute("title", "Insert");
    model.addAttribute("result", "입력 완료");
    model.addAttribute("mode", 0);//0: 메세지만, 1: 단일행, 2: 다중행
    //통합 뷰 템플릿 사용 
    return "total_view";
  }
  
  //전체조회
  @GetMapping("/selectAll.do")
  public String selectAll(Model model) {
    //2개 이상의 레코드일 수 있으므로 List로 반환 
    List<Member> result = memberService.selectAll();
    model.addAttribute("title", "Select All");
    model.addAttribute("result", result);
    model.addAttribute("mode", 2);//0: 메세지만, 1: 단일행, 2: 다중행
    return "total_view";
  }

  //아이디로 검색
  @GetMapping("/selectById.do")
  //@RequestParam 어노테이션으로 파라미터를 받은 후 매개변수에 저장 
  public String selectId(@RequestParam("id") Long search, Model model) {
    //PK 컬럼에 대한 검색이므로 반환타입은 Optional<>로 정의 
    Optional<Member> result = memberService.selectId(search);
    model.addAttribute("title", "Select By Id");
    model.addAttribute("result", result.orElse(null));
    model.addAttribute("mode", 1);//0: 메세지만, 1: 단일행, 2: 다중행
    return "total_view";
  }

  //이름으로 검색
  @GetMapping("/selectByName.do")
  public String selectName(@RequestParam("name") String search, Model model) {
    model.addAttribute("title", "Select By Name");

    //검색결과가 단일행일때
    // Optional<Member> result = memberService.selectName(search);
    // model.addAttribute("result", result);
    // model.addAttribute("mode", 2);//0: 메세지만, 1: 단일행, 2: 다중행

    //검색결과가 다중행일때
    List<Member> result = memberService.selectName(search);
    model.addAttribute("result", result);
    model.addAttribute("mode", 2);//0: 메세지만, 1: 단일행, 2: 다중행
    return "total_view";
  }

  //이메일로 검색
  @GetMapping("/selectByEmail.do")
  public String selectEmail(@RequestParam("email") String search, Model model) {
    model.addAttribute("title", "Select By Email");

    // Optional<Member> result = memberService.selectEmail(search);
    // model.addAttribute("result", result.get());
    // model.addAttribute("mode", 1);//0: 메세지만, 1: 단일행, 2: 다중행

    List<Member> result = memberService.selectEmail(search);
    model.addAttribute("result", result);
    model.addAttribute("mode", 2);//0: 메세지만, 1: 단일행, 2: 다중행
    return "total_view";
  }

  //이름으로 검색(Like)
  @GetMapping("/selectByNameLike.do")
  public String selectByNameLike(@RequestParam("name") String search, Model model) {
    //Like 검색시 %는 개발자가 직접 추가해야한다. 
    //%가 양쪽에 있으면 부분검색, 좌측에 있으면 접두어 검색, 우측에 있으면 접미어 검색
    String name = "%" + search + "%";
    List<Member> result = memberService.selectNameLike(name);
    model.addAttribute("title", "Select By Like Name");
    model.addAttribute("result", result);
    model.addAttribute("mode", 2);//0: 메세지만, 1: 단일행, 2: 다중행
    return "total_view";
  }

  //이름으로 검색(Like) - 내림차순
  @GetMapping("/selectByNameLikeNameDesc.do")
  public String selectByNameLikeNameDesc(@RequestParam("name") String search, Model model) {
    //%가 뒤에 있으므로 검색어로 시작하는 문장을 검색 
    String name = search + "%";
    //이름으로 검색한 후 내림차순 정렬해서 인출 
    List<Member> result = memberService.selectNameLikeOrderByNameDesc(name);
    model.addAttribute("title", "Select By Like Name Desc");
    model.addAttribute("result", result);
    model.addAttribute("mode", 2);//0: 메세지만, 1: 단일행, 2: 다중행
    return "total_view";
  }

  //위와 동일하지만 Sort 사용
  @GetMapping("/selectByNameLikeOrder.do")
  public String selectByNameLikeOrder(@RequestParam("name") String search, Model model) {
    String name = search + "%";

    /*
    정렬 기능으로 인해 메서드명이 길어지는 것을 Sort를 통해 중일 수 있다. name 컬럼으로 내림차순 정렬로 설정
    */
    Sort sort = Sort.by(Sort.Order.desc("name"));
    /*
    2개 이상의 컬럼으로 정렬시에는 컴마로 구분하여 각각의 항목을 추가할 수 있다. 
    */

    //서비스의 메서드 호출시 Sort빈을 파라미터로 전달한다. 
    List<Member> result = memberService.selectNameLike(name, sort);
    model.addAttribute("title", "Select By" + search + " Like Name Desc(Sort사용)");
    model.addAttribute("result", result);
    model.addAttribute("mode", 2);//0: 메세지만, 1: 단일행, 2: 다중행

    return "total_view";
  }
}
