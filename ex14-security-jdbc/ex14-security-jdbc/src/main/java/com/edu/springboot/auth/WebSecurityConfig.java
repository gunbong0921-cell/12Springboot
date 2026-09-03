package com.edu.springboot.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import jakarta.servlet.DispatcherType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import javax.sql.DataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
/*
스프링 시큐리티에서 가장 핵심이 되는 설정 클래스로, 스프링 컨테이너가 시작될때 빈으로 생성되는 부분임을 
어노테이션으로 명시하고 있다. */
@Configuration
public class WebSecurityConfig {

  //로그인 실패시 처리를 위한 핸들러 빈 자동 주입 
  @Autowired
  private MyAuthFailureHandler myAuthFailureHandler;
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.csrf((csrf) -> csrf.disable())
    .cors((cors) -> cors.disable())
    .authorizeHttpRequests((request) -> request
      //타임리프나 JSP로 내부 푸워딩을 할때 시큐리티 검사를 건너뜀
      .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
      //루트 경로는 누구든 접근 가능  
      .requestMatchers("/").permitAll()
      //정적리소스(static) 허용(폴더 직접 지정)  
      .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
      //정적리소스 static 하위의 모든 경로를 한꺼번에 지정하는 방법 
      //.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
      /*
      아래 formLogin()의 permitAll()은 경로뿐 아니라 쿼리스트링까지 일치해야만 URL을 허용한다. 즉 /myLogin.do?
      파라미터와 같은 URL은 허용하지 않으므로 쿼리스트링이 전부 사라지게 된다.
      따라서 authorizeHttpRequests()에서 페이지를 허용해야 에러핸들러가 정상적으로 작동한다.  
       */
      .requestMatchers("/myLogin.do", "/myLoginAction.do").permitAll()
      //guest와 같이 누구나 접근할 수 있도록 지정한다. 
      .requestMatchers("/guest/**").permitAll()
      //부여된 2가지 중 하나의 권한을 획득하면 접근할 수 있다. 
      .requestMatchers("/member/**").hasAnyRole("USER", "ADMIN")
      //해당 권한의 사용자만 접근할 수 있다. 
      .requestMatchers("/admin/**").hasRole("ADMIN")
      .anyRequest().authenticated()
    );

    /*
    로그인 페이지 UI커스텀을 위한 설정
    .loginPage : 로그인 페이지의 요청명
    .loginProcessingUrl : 로그인 폼에서 submit했을때 action에 지정된 경로 및 요청명
    .usernameParameter : 아이디 입력상자의 name 속성값
    .passwordParameter : 패스워드의 name 속성값 
    */
    http.formLogin((formLogin) -> formLogin
      .loginPage("/myLogin.do") //default : /login
      .loginProcessingUrl("/myLoginAction.do")
      //로그인에 실패 시 이동할 요청명 설정
      //로그인에 실패한 경우 핸들러를 통해 처리(3단계) 
      .failureHandler(myAuthFailureHandler) 
      .usernameParameter("my_id") //default : username
      .passwordParameter("my_pass") //default : password
      .permitAll());

    /*
    로그아웃 처리에 대한 커스텀
    .logoutUrl : 로그아수 링크 설정
    .logoutSuccessUrl : 로그아웃 성공시 이동할 페이지의 요청명 
    */  
    http.logout((logout) -> logout
      .logoutUrl("/myLogout.do") //default : /logout
      .logoutSuccessUrl("/")
      .permitAll());

    //권한이 부족한 경우 이동할 페이지의 요청명 설정   
    http.exceptionHandling((exceptionHandling) -> exceptionHandling
      .accessDeniedPage("/denied.do"));

    return http.build();
  }

  //패스워드 인코더. DB나 메모리에 저장하기 전 암호화를 진행한다. 
  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Autowired
  private DataSource dataSource;

  //오라클 연결을 위한 빈 자동주입 
  @Autowired
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication()
    //오라클DB에 연결
    .dataSource(dataSource)
    //아이디를 조건으로 패스워드, 활성화 상태를 확인 후 인증 처리 
    .usersByUsernameQuery("SELECT user_id, user_pw, enabled FROM security_admin WHERE user_id = ?")
    //아이디를 조건으로 인가 처리. 즉 권한을 확인한다. 
    .authoritiesByUsernameQuery("SELECT user_id, authority FROM security_admin WHERE user_id = ?")
    //패스워드 암호화 
    .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
  }
}
