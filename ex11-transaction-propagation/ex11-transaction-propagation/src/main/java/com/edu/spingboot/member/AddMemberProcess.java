package com.edu.spingboot.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.edu.spingboot.jdbc.ITicketService;
import com.edu.spingboot.jdbc.TicketDTO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AddMemberProcess {

  //DB작업을 위해 매퍼 인터페이스 자동주입 
  @Autowired
  ITicketService dao;

  /*
  항상 새로운 트랜젝션을 생성한다. 기존 트랜젝션은 잠시 보류됨. 호출한 쪽(부모)에서 예외가 발생하더라도, 
  이 매서드(자식)의 작업은 영향을 받지않고 Commit될 수 있다. 반대로 이 메서드에서 예외가 발생하더라도, 
  호출한 쪽에서 예외를 처리한다면 호출한 쪽의 트랜젝션은 유지될 수 있다. 즉 각각의 트랜젝션이 독립적으로
  운영되어 서로 전파되지 않는다. 
  */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void memberInsert(TicketDTO ticketDTO, HttpServletRequest req) {
    
    //1. 의도적 에러 발생 부분 (체크박스2 선택 시)
    String errFlag = req.getParameter("err_flag");
    if(errFlag != null && errFlag.equals("2")) {
      Integer.parseInt("200원");
    }

    //2. 회원 내역 추가 DB 작업
    int result3 = dao.memberRegist(ticketDTO);
    if(result3 == 1) {
      System.out.println("member 입력성공");
    }
  }
}
