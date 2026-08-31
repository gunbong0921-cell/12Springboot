package com.edu.spingboot.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

/*
컨트롤러가 받은 요청에 대해 실제 비즈니스로직을 처리하는 역할을 하는 서비스 역할의 클래스 생성.
이 어노테이션도 스프링 컨테이너가 시작될때 자동으로 빈을 생성하게 된다. */
@Service
public class BuyTicketProcess {

  //메퍼 인터페이스 주입 
  @Autowired
  private ITicketService dao;

  /*
  트랜젝션 전체의 기본 설정으로 REQUIRED 사용
  기존에 진행중인 트랜젝션이 있다면, 해당 트랜젝션에 합류(Join)한다.
  합류된 트랜젝션 중 어느 한곳이라도 예외가 발생되면 전체를 Rollback 처리한다.
  즉 모든 트랜젝션을 하나의 작업 단위로 묶어서 커밋 or 롤백으로 처리한다. 
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public void buyTicketAction(TicketDTO ticketDTO, PayDTO payDTO, HttpServletRequest req) {

    /*
    기존 컨트롤러에서 처리했던 코드를, 서비스로 옮겨서 처리하도록 수정함. 
    */
    //1. DB처리1 : 구매 금액 처리 (1장당 10,000원)
    payDTO.setAmount(ticketDTO.getT_count() * 10000);
    int result1 = dao.payInsert(payDTO);
    if(result1 == 1) {
      System.out.println("transaction_pay 입력성공");
    }

    //2. 비즈니스 로직 처리 (의도적인 런타임 에러 발생)
    String errFlag = req.getParameter("err_flag");
    if(errFlag != null && errFlag.equals("1")) {
      Integer.parseInt("100원");
    }

    //3. DB처리2 : 구매한 티켓 매수 처리
    int result2 = dao.ticketInsert(ticketDTO);
    if(result2 == 1) {
      System.out.println("transaction_ticket 입력성공");
    }
  }
}

