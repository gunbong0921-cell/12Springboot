package com.edu.springboot.auth;

import org.springframework.context.annotation.Configuration;
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
/*
스프링 시큐리티에서 가장 핵심이 되는 설정 클래스로, 스프링 컨테이너가 시작될때 빈으로 생성되는 부분임을 
어노테이션으로 명시하고 있다. */
@Configuration
public class WebSecurityConfig {
  
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
    .failureUrl : 로그인에 실패한 경우 이동할 요청명
    .usernameParameter : 아이디 입력상자의 name 속성값
    .passwordParameter : 패스워드의 name 속성값 
    */
    http.formLogin((formLogin) -> formLogin
      .loginPage("/myLogin.do") //default : /login
      .loginProcessingUrl("/myLoginAction.do")
      .failureUrl("/myError.do") //default : /login?error
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

  //사용자의 아이디, 패스워드를 설정하고 권한을 부여하기 위한 빈 
  @Bean
  public UserDetailsService users() {
    //"1234" 문자열을 메서드를 이용해서 암호화한다. 이 함수는 실행할때마다 다른 문자열이 반환된다. 
    //System.out.println("패스워드:"+passwordEncoder().encode("1234"));

    //사용자 생성 : 아이디, 패스워드, 권한을 설정한다. 
    UserDetails user = User.builder()
      .username("user")
      .password(passwordEncoder().encode("1234"))
      .roles("USER")
      .build();
    UserDetails admin = User.builder()
      .username("admin")
      .password(passwordEncoder().encode("1234"))
      .roles("ADMIN")
      .build();
    //DB없이 인메모리 방식으로 사용자 정보를 저장 
    return new InMemoryUserDetailsManager(user, admin);
  }
}
