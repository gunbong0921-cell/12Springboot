package com.edu.springboot.exam1;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class UserController {

	//get방식의 요청명으로 매핑 
    @GetMapping("/thymeleaf.do")
    public String thymeleaf(Model model) {
    	/*
    	MOdel갹체는 컨트롤러에서 처리된 내용을 뷰(View)로 전달할때 사용한다. Map과 같이 Key-Value형식으로 저장한다. */

    	//UserDTO를 여러개 저장할 수 있는 List생성 및 객체 저장
        List<UserDTO> users = List.of(
            new UserDTO("홍길동", 20),
            new UserDTO("김철수", 17),
            new UserDTO("이영희", 25)
        );

        /*
        Model 객체에 저장시 addAttribute(Key, Value) 형식ㅇ르 사용한다. 이객체에는 모든 형식의 데이터를 저장할 수 있다. */
        //문자열 저장
        model.addAttribute("title", "Thymeleaf 기본 문법");
        //List 저장
        model.addAttribute("users", users);
        //boolean 저장 
        model.addAttribute("isAdmin", true);

        //Model 객체에 저장된 데이터는 템플릿 파일로 전달된다. 
        return "thymeleaf";
    }
}