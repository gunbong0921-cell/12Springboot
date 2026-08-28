package com.edu.springboot.jdbc;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParameterDTO {
  private List<String> searchField;
  private String searchKeyword;
}
