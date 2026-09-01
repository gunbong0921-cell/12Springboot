package com.edu.springboot.auth;

import org.springframework.stereotype.Component;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import jakarta.servlet.ServletException;

//자동으로 생성되는 빈 암을 명시하는 어노테이션
@Component
public class MyAuthFailureHandler implements AuthenticationFailureHandler {
  //핸들러 클래스 정의를 위해 인터페이스를 구현한 추상메서드 오버라이딩 
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, 
    AuthenticationException exception) throws IOException, ServletException {
      exception.printStackTrace();
      /*
      오버라이딩 한 매서드는 request, response 내장객체와 예외처리를 위한 클래스가 매개변수로 선언되어있다. 
      */
      String errorMsg = "";

      /*
      인증에 실패한 경우 매개변수로 전달되는 예외객체를 instanceof 연산자를 이용해서 파악한다. 그리고 적절한
      에러메세지를 지정한다. 단 인증관련 메세지는 너무 자세히 기술하지 않는 것이 좋다. 
      */
      if(exception instanceof BadCredentialsException) {
        /*
        인증에 실패했을때 별도로 처리할 내용이 있다면 사용자 정의 매서드를 통해 구현할 수 있다. 여기에서는 
        로그인에 사용한 아이디를 인수로 전달하고 있다. 
        */
        loginFailureCnt(request.getParameter("my_id"));
        errorMsg = "아이디 또는 비밀번호가 일치하지 않습니다.";
      } 
      else if(exception instanceof InsufficientAuthenticationException) {
        errorMsg = "시스템 문제로 로그인 요청을 처리할 수 없습니다.";
      }
      else if(exception instanceof DisabledException) {
        errorMsg = "계정이 비활성화되어 있습니다.";
      }
      else if(exception instanceof AccountExpiredException) {
        errorMsg = "계정이 유효기간이 만료되었습니다.";
      }
      else if(exception instanceof CredentialsExpiredException) {
        errorMsg = "비밀번호 유효기간이 만료되었습니다.";
      }
      else if(exception instanceof LockedException) {
        errorMsg = "계정이 잠겼습니다.";
      }

      //메세지에 한글이 포함되므로 UTF-8 인코딩이 필요하다. 
      String encodedMsg = URLEncoder.encode(errorMsg, "UTF-8");
      //라디에이트로 로그인 페이지로 이동하도록 처리 
      response.sendRedirect(request.getContextPath() + "/myLogin.do?error=true&errorMsg=" + encodedMsg);
    }

    public void loginFailureCnt(String username) {
      System.out.println("요청 아이디:" + username);
      /* 
      틀린 횟수 업데이트 및 조회
      만약 3회 이상 실패했다면 계정 잠금처리 등의 로직을 추가할 수 있다. 
      */
  }
}
