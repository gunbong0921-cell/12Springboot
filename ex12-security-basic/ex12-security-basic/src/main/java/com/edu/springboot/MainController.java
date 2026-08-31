package com.edu.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MainController {
  
  //Root 경로 매핑
  @GetMapping("/")
  public String main() {
    return "main";
  }

  //guest 하위 경로도 누구나 접근할 수 있도록 매핑 
  @GetMapping("/guest/index.do")
  public String welcome1() {
    return "guest";
  }

  //USER 혹은 ADMIN 둘 중 하나의 권한을 획득해야 접근 가능
  @GetMapping("/member/index.do")
  public String welcome2() {
    return "member";
  }

  //ADMIN 권한만 접근 가능 
  @GetMapping("/admin/index.do")
  public String welcome3() {
    return "admin";
  }
}

