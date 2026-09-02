package com.edu.springboot.jsontype4;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


/*
@ResonseBody + @Controller 와 같은 형태로 RestAPI를 ㅁ나들기 위한 컨트롤러에서 사용된다. 내부 메서드에서 반환되는
값은 웹브라우저에 즉시 출력된다. (일반적인 컨트롤러에서는 반환값이 View의 경로(템플릿)로 사용된다.)
*/
@RestController
public class MyRestController {
  
  /*
  JSON 객체 : Map은 key와 Value로 데이터를 저장하게 되므로 JSON객체와 형식이 동일하다. 따라서 Map을 반호나하면 JSON
  객체형식으로 출력된다. 
  */
  @GetMapping("/restApi01.do")
  public Map<String, Object> restApi01() {
    //Map 인스턴스 생성
     Map<String, Object> maps = new HashMap<>();

     //key-value 형식으로 데이터 저장
     maps.put("key01", "홍길동");
     maps.put("key02", "유비");
     maps.put("key03", "손오공");
     maps.put("key04", "강백호");
     maps.put("key05", "둘리");

     return maps;
  }
 /*
 JSON 배열 : 배열은 List와 동일한 구조를 가진다. 요소의 접근은 인덱스를 통해 할 수 있다. 
 */
  @GetMapping("/restApi02.do")
  public List<String> restApi02() {
    //List 인스턴스 생성 
    List<String> lists = new ArrayList<>();

    //입력되는 순서대로 0부터 인덱스가 지정됨 
    lists.add("홍길동");
    lists.add("유비");
    lists.add("손오공");
    lists.add("강백호");
    lists.add("둘리");

    return lists;
  }
  /*
  JSON 객체이지만 Value로 배열을 포함하는 형태로 실무에서 가장 많이 사용하는 형식이다. 
  */
  @GetMapping("/restApi03.do")
  public Map<String, Object> restApi03() {
    Map<String, Object> maps = new HashMap<>();

    //List를 map의 Value로 저장한다. 
    List<String> lists1 = new ArrayList<>(Arrays.asList("이순신", "세종대왕", "신사임당"));
    //"위인"이라는 Key에 앞에서 생성한 List를 추가한다. 
    maps.put("한국의위인", lists1);

    List<String> lists2 = new ArrayList<>(Arrays.asList("유비", "관우", "장비"));
    maps.put("삼국지", lists2);

    List<String> lists3 = new ArrayList<>(Arrays.asList("손오공", "저팔계", "사오정"));
    maps.put("서유기", lists3);

    return maps;
  }
  /*
  JSON 배열이지만 객체를 인자로 가지는 형식. 게시판, 회원목록 등을 구현할때 많이 사용하는 형식이다. 
  */
  @GetMapping("/restApi04.do")
  public List<PersonVO> restApi04() {
    //List 인스턴스 생성 
    List<PersonVO> lists = new ArrayList<>();

    //데이터로 사용할 객체 생성 
    PersonVO vo1 = new PersonVO("강백호", 21, "파워포워드");
    PersonVO vo2 = new PersonVO("서태웅", 21, "파워포워드");
    PersonVO vo3 = new PersonVO("송태섭", 22, "포인트가드");
    PersonVO vo4 = new PersonVO("정대만", 23, "슈팅가드");
    PersonVO vo5 = new PersonVO("채치수", 24, "센터");

    //VO객체를 List에 추가한다. 
    lists.add(vo1);
    lists.add(vo2);
    lists.add(vo3);
    lists.add(vo4);
    lists.add(vo5);

    return lists;
  } 
}
