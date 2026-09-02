package com.edu.springboot.jsontype4;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonVO {
  private String name;
  private int age;
  private String job;
}
