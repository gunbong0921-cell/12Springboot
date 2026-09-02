package com.edu.springboot.restboard;

import lombok.Data;

@Data
public class BoardDTO {
  private String idx;
  private String name;
  private String title;
  private String content;
  private java.sql.Date postdate;
  private String visitcount;
}
