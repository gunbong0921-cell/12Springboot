package com.edu.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;
import org.springframework.ui.Model;


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

  //커스텀 로그인 페이지 매핑
  @GetMapping("/myLogin.do")
  public String login1(Principal principal, Model model) {
    /*
    시큐리티를 이용한 인증(로그인) 후 사용자 정보를 얻어올때는 principal 객체를 통해 얻어온다.
    */
    try {
      //로그인 아이디 인출
      String user_id = principal.getName();
      //Model 객체에 저장 
      model.addAttribute("user_id", user_id);
    } 
    catch (Exception e) {
      /*
      최초 접근시에는 로그인 정보가 없으므로 NullPointException이 발생한다. 따라서 예외처리를 해야한다. 
      */
      System.out.println("로그인 전입니다.");
    }
    return "auth/login";
  }

  //로그인 시도 중 에러가 발생하는 경우
  @GetMapping("/myError.do")
  public String login2() {
    return "auth/error";
  }

  //권한이 부족한 경우
  @GetMapping("/denied.do")
  public String login3() {
    return "auth/denied";
  }
}


