package com.edu.springboot.restboard;

import org.apache.ibatis.annotations.Mapper;
import java.util.ArrayList;

//컨트롤러와 XML매퍼 사이를 연결해주는 인터페이스 
@Mapper
public interface IBoardService {
  //게시물 갯수
  public int totalCount();
  //게시물 가져오기
  public ArrayList<BoardDTO> list(ParameterDTO parameterDTO);
  //게시물 검색하기
  public ArrayList<BoardDTO> search(ParameterDTO parameterDTO);
  //게시물 내용보기
  public BoardDTO view(ParameterDTO parameterDTO);
  //게시물 작성하기
  public int write(BoardDTO boardDTO);
}
