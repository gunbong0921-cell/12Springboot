package com.edu.springboot.restboard;

import lombok.Data;
import java.util.ArrayList;

//여러가지 다양한 파라미터를  한꺼번에 받을 때 사용  
@Data
public class ParameterDTO {
  //일련번호와 페이지번호
  private String idx;
  private String pageNum;
  //검색필드와 검색어(2개 이상의 단어를 받으므로 List로 선언)
  private String searchField;
  private ArrayList<String> searchWord;
  //각 페이지의 레코드 구간(Rownum 컬럼의 값을 지정함) 
  private int start;
  private int end;
}
