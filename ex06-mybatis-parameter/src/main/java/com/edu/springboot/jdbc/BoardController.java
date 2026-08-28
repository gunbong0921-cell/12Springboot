package com.edu.springboot.jdbc;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//클래스에서 상위 경로에 대한 매핑 처리 
@RequestMapping("/myboard")
public class BoardController {
	/**
  데이터베이스 작업을 위해 DAO를 자동주입 받는다. Mybatis를 사용중이므로 매퍼 인터페이스를 자동주입
  받은 후 XML 매퍼를 호출하게된다.
   */
	@Autowired
	IBoardService dao;
	
	//게시판 목록. /myboard의 하위 경로 매핑. 
	@GetMapping("/list.do")
	public String boardList(Model model) {
    //게시물의 전체 개수 카운트 
		int totalCount = dao.getTotalCount();
		model.addAttribute("totalCount", totalCount);
		
    //현재 페이지에 출력할 게시물 인출 
		ArrayList<BoardDTO> lists = dao.listPage();
		model.addAttribute("lists", lists);
		
    //View로 전달 
		return "list";
	}
	
	// 작성 매핑 1 : 매핑을 단순하게 페이지 이동만 처리
	@GetMapping("/write.do")
	public String boardWrite() {
		return "write";
	}
	
	// 작성 매핑 2 : 전송된 폼값을 폼값을 수신한 후 오버로딩한 메서드 추가
	@PostMapping("/write.do")
	public String boardWritePost(BoardDTO boardDTO) {
		int result = dao.write(boardDTO);
    //전송된 폼값은 한번에 받아서 Mapper로 전달 
		System.out.println("글쓰기결과:" + result);
		//작성이 완료되면 목록으로 이동 
		return "redirect:list.do";
	}
	
	// 열람
	@GetMapping("/view.do")
	public String boardView(Model model, BoardDTO boardDTO) {
		//상세보기 진입 시 조회수 1 증가 (DB에 바로 반영)
		int visitResult = dao.visitCount(boardDTO);
		System.out.println("조회수증가결과:" + visitResult);
		//열람을 위해 전달되는 일련번호를 인수로 전달
		boardDTO = dao.view(boardDTO);
		//내용 작성시 언테키로 입력된 부분은 <br>태그로 줄바꿈 처리
		model.addAttribute("boardDTO", boardDTO);
		//View로 전달 
		return "view";
	}
	
	// 수정 1 : 수정폼에 기존내용 인출하기
	@GetMapping("/edit.do")
	public String boardEdit(Model model, BoardDTO boardDTO) {
		//열람에서 사용했던 매서드를 이용해서 레코드 인출
		boardDTO = dao.view(boardDTO);
		//Model객체에 저장 후 View 로 전달 
		model.addAttribute("boardDTO", boardDTO);
		
		return "edit";
	}
	
	// 수정 2 : 처리
	@PostMapping("/edit.do")
	public String boardEditPost(BoardDTO boardDTO) {
		//전송된 폼값을 한번에 받은 후 Mapper의 함수 호출
		int result = dao.edit(boardDTO);
		System.out.println("글수정결과:" + result);
		//수정이 완료되면 열람 페이지로 이동 
		return "redirect:view.do?idx=" + boardDTO.getIdx();
	}
	
	// 삭제
	@PostMapping("/delete.do")
	public String boardDeletePost(BoardDTO boardDTO) {
		//삭제를 위한 일련번호를 DTO로 받은 후 매퍼 호출 
		int result = dao.delete(boardDTO);
		System.out.println("글삭제결과:" + result);
		
		return "redirect:list.do";
	}
}