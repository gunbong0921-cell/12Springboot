package com.edu.springboot.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;

@Getter 
@Setter
@AllArgsConstructor 
@NoArgsConstructor(access = AccessLevel.PROTECTED) 
@Builder 
@Entity(name = "JpaMember01")
public class Member {
  //PK지정 및 기본 시퀀스 생성
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String email;
}
