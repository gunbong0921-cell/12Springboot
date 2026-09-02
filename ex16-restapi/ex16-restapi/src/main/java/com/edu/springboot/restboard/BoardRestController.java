package com.edu.springboot.restboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*")
@RestController
public class BoardRestController {
  
  //매퍼 인터페이스 자동 주입
  @Autowired
  IBoardService dao;

  /* 프로퍼티 파일에 설정해둔 값을 얻어와서 멤버변수에 저장한다. @value 어노테이션은 이와 같이 값을 
  지정하는 역할을 한다. */
  @Value("${board.pageSize}")
  private int pageSize;

  //각 페이지의 목록을 객체를 포함한 배열로 출력 
  @GetMapping("/restBoardList.do")
  public List<BoardDTO> restBoardList(ParameterDTO parameterDTO) {
    //페이지번호, 파라미터가 없을때는 1로 설정한다. 만약 값이 있다면 정수로 변환 후 저장한다. 
    int pageNum = parameterDTO.getPageNum()==null ? 1 : Integer.parseInt(parameterDTO.getPageNum());
    //게시물의 구간을 계산. pageNum이 1일때 1, 3이 된다. 
    int start = (pageNum - 1) * pageSize + 1;
    int end = pageNum * pageSize;
    //DTO에 계산 결과 저장 
    parameterDTO.setStart(start);
    parameterDTO.setEnd(end);
    //DAO(매퍼)로 전달하면서 메서드 호출 
    List<BoardDTO> boardList = dao.list(parameterDTO);
    //List를 반환하므로 JSON 배열이 화면에 출력된다. 
    return boardList;
  }

  @GetMapping("/restBoardSearch.do")
  public List<BoardDTO> restBoardSearch(HttpServletRequest req, ParameterDTO parameterDTO) {
    //검색어를 입력한 경우라면..
    if(req.getParameter("searchWord")!=null) {
      //검색 파라미터를 스페이스로 나눠서 배열로 저장
      String[] sTxtArray = req.getParameter("searchWord").split(" ");
      //List 인스턴스 생성 
      parameterDTO.setSearchWord(new ArrayList<>());
      for(String sTxt : sTxtArray) {
        System.out.println(sTxt);
        parameterDTO.getSearchWord().add(sTxt);
      }  
    }
    //DAO(매퍼)로 전달하면서 메서드 호출 
    List<BoardDTO> searchList = dao.search(parameterDTO);
    //List를 반환하므로 JSON 배열이 화면에 출력된다. 
    return searchList;
  }

  //게시물 상세보기
  @GetMapping("/restBoardView.do")
  public BoardDTO restBoardView(ParameterDTO parameterDTO) {
    //매개변수로 전달된 idx(일련번호)를 DAO로 전달해서 게시물 인출
    BoardDTO board = dao.view(parameterDTO);
    //DTO도 Map과 동일한 객체형식이므로 출력시 JSON으로 변환되어 웹브라우저에 출력된다. 
    return board;
  }

  //작성하기
  @PostMapping("/restBoardWrite.do")
  public Map<String, Integer> restBoardWrite(BoardDTO boardDTO) {
    Map<String, Integer> map = new HashMap<>();
    try {
      //insert 쿼리 실행 후 결과는 정수로 반환된다.
      int result = dao.write(boardDTO);
      //결과를 map에 저장 
      map.put("result", result);
    } 
    catch (Exception e) {}
    return map;
  }
}
