package com.edu.spring.jpa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import lombok.AccessLevel;
import lombok.Builder;

@Getter 
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity(name = "jpamember04")
@Table(name = "JPAMEMBER04")
public class Member {
  //PK로 지정
  @Id
  //시퀀스 생성 설정(시퀀스명도 언더바가 아니라 언더바로 연결됨) 
  @SequenceGenerator(
    name = "mySequence04",
    sequenceName = "member_seq",
    initialValue = 1,
    allocationSize = 1
  )
  //시퀀스 사용 설정
  @GeneratedValue(generator = "mySequence04")
  private Long id;
  private String name;
  private String email;
}
