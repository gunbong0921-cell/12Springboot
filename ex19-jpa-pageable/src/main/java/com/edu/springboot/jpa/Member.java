package com.edu.springboot.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import jakarta.persistence.GeneratedValue;

@Getter 
@AllArgsConstructor 
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Builder 
@Entity (name = "JPAMember03")
@Table(name = "JPAMEMBER03")
public class Member {
  @Id 
  //시퀀그 생성에 대한 커스텀 설정
  @SequenceGenerator (
    //JPA내부에서 시퀀스를 식별하기 위한 이름
    name = "mySequence03",
    //DB에 생성되는 시퀀스 객체 이름
    sequenceName = "JpaMember03_seq",
    //시작값. start by N
    initialValue = 1,
    //시퀀스 증가값 increment by N
    allocationSize = 1
  )
  
  @GeneratedValue (generator = "mySequence03")
  private Long id;
  private String name;
  private String email;
}
