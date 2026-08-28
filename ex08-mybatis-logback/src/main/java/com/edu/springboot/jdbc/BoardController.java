package com.edu.springboot.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	
	//마이바티스로 제작한 게시판 목록 출력
	@GetMapping("/list.do")
	public String boardList(Model model, ParameterDTO parameterDTO) {
		int totalCount = dao.getTotalCount(parameterDTO);
		model.addAttribute("totalCount", totalCount);
		
		ArrayList<BoardDTO> lists = dao.listPage(parameterDTO);
		model.addAttribute("lists", lists);
		
		return "list";
	}
	
	// 작성 매핑 1 : 쓰기페이지 매핑
	@GetMapping("/write.do")
	public String boardWriteGet(Model model) {
		return "write";
	}
	
	// 작성 매핑 2 : 폼값을 받아 insert 처리
	@PostMapping("/write.do")
	public String boardWritePost(Model model, BoardDTO boardDTO) {
		//이름, 제목, 내용을 개별적으로 전달
		int result = dao.write(boardDTO.getName(), boardDTO.getTitle(), boardDTO.getContent());
		System.out.println("글쓰기결과:" + result);
		
		return "redirect:list.do";
	}
	
	// 열람
	@GetMapping("/view.do")
	public String boardView(Model model, BoardDTO boardDTO) {
		boardDTO = dao.view(boardDTO.getIdx());
		boardDTO.setContent(boardDTO.getContent().replace("\r\n", "<br>"));
		model.addAttribute("boardDTO", boardDTO);

		return "view";
	}
	
	// 수정 1 : 수정폼에 기존내용 인출하기
	@GetMapping("/edit.do")
	public String boardEditGet(Model model, BoardDTO boardDTO) {
		//일련번호를 개별적으로 전달 
		boardDTO = dao.view(boardDTO.getIdx());
		model.addAttribute("boardDTO", boardDTO);
		
		return "edit";
	}
	
	// 수정 2 : 수정처리
	@PostMapping("/edit.do")
	public String boardEditPost(BoardDTO boardDTO) {
		//파라미터 저장을 위한 Map 생성
		Map<String, Object> paramMap = new HashMap<>();
		//파라미터 담기
		paramMap.put("name", boardDTO.getName());
		paramMap.put("idx", boardDTO.getIdx());
		paramMap.put("title", boardDTO.getTitle());
		paramMap.put("content", boardDTO.getContent());

		int result = dao.edit(paramMap);
		System.out.println("글수정결과:" + result);
		return "redirect:view.do?idx=" + boardDTO.getIdx();
	}
	
	// 삭제처리
	@PostMapping("/delete.do")
	public String boardDeletePost(BoardDTO boardDTO) {
		int result = dao.delete(boardDTO.getIdx());
		System.out.println("글삭제결과:" + result);
		
		return "redirect:list.do";
	}
}