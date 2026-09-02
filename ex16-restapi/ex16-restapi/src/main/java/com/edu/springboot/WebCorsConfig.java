package com.edu.springboot;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
동일 출처가 아닌 경우 요청을 허용하기 위한 설정 파일. 컨테이너 시작시 자동으로 빈이 생성되어 적용된다. */
@Configuration
public class WebCorsConfig implements WebMvcConfigurer {
  /*
  addMapping() : 설정된 경로에 대해 CRUD를 허용한다. 만약 특정 경로만 허용하고 싶다면 "/apis/*" 와 같이 작성하면 된다. 
  allowedOriginPatterns() : 특정 오리진에 대해 허용한다. 이 경우 "hppt://sample.com" 과 같이 작성하면 된다.
  */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .maxAge(3600);
  }
}
