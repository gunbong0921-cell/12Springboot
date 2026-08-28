package com.edu.springboot.jdbc;

import java.util.ArrayList;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.edu.springboot.jdbc.BoardDTO;  

/*
컨트롤러에서 Mapper의 특정 메서드를 호출하기 위해 인터페이스에 정의된 추상매서드를 호출하게 된다. 인터페이스의
위치는 Mapperdml 폴더 하위에 있는 BoardDAO.xml 파일에 정의된 메서드를 호출하게 된다.
*/
@Mapper
public interface IBoardService {
    // 목록 개수 : 게시물의 개수를 카운트하여 정수로 변환
    public int getTotalCount(ParameterDTO parameterDTO);
    // 목록 가져오기 : 목록에 출력할 게시물을 List형태로 반환 
    public ArrayList<BoardDTO> listPage(ParameterDTO parameterDTO);
    // 작성 : request 내장객체로 받은 후 param1 ~3
    public int write(String name, String title, String content);
    // 열람 : @Parma 어노테이션
    public BoardDTO view(@Param("_idx") String idx);
    // 수정 : Map
    public int edit(Map<String, Object> map);
    // 삭제 : 인덱스사용(0 ~ 2)
    public int delete(String idx);
}
