package com.edu.springboot.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import java.time.LocalDateTime;
import lombok.AccessLevel;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity(name = "JpaMember01")
public class Member {
  @Id
  @GeneratedValue
  private Long id;
  private String username;
  @Column(name = "create_date")
  private LocalDateTime createDate;
}
