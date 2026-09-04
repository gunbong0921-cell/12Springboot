package com.edu.springboot.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;

@Controller 
public class JpaController {
  
  @Autowired
  MemberService memberService;

  @GetMapping("/selectByNameLike.do")
  public String selectByNameLike(
    @RequestParam("name") String pname,
    @RequestParam("page") String page,
    Model model){
    //파라미터로 전달된 검색어와 페이지번호를 콘솔에 출력 
    System.out.println("검색어: " + pname);
    System.out.println("페이지: " + page);

    //pname으로 시작하는 문자열 검색 
    String name = pname + "%";
    //Sort를 이용해서 id를 내림차순으로 정렬 
    Sort sort = Sort.by(Sort.Direction.DESC, "id");
    //Pageble에서는 페이지번호가 0부터 시작이므로 -1해서 넘긴다. 
    int pagenum = Integer.parseInt(page) - 1;
    //현재 페이지에 출력할 레코드를 5개로 설정하고 정렬 적용 
    Pageable pageable = PageRequest.ofSize(5)
      .withPage(pagenum).withSort(sort);
      //페이지에 대한 설정과 검색어를 인수로 전달해서 쿼리문 실행 
    Page<Member> result = memberService.findByNameLike(name, pageable);
    //출력할 레코드를 List로 인출 
    List<Member> content = result.getContent();
    //전체 페이지 수
    long totalElements = result.getTotalElements();
    //한 페이지당 출력개수
    int totalPages = result.getTotalPages();
    //현재 페이지 번호. 0부터 시작이므로 +1해서 넘긴다. 
    int size = result.getSize(); 
    int pageNumber = result.getNumber() + 1;
    int numberOfElements = result.getNumberOfElements();

    model.addAttribute("members", content);
    model.addAttribute("totalElements", totalElements);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("size", size);
    model.addAttribute("pageNumber", pageNumber);
    model.addAttribute("numberOfElements", numberOfElements);

    return "member_list";
  }
}
