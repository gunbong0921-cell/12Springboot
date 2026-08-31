package com.edu.spingboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.edu.spingboot.jdbc.ITicketService;
import com.edu.spingboot.jdbc.PayDTO;
import com.edu.spingboot.jdbc.TicketDTO;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MainController {

  private final Ex09TransactionManagerApplication ex09TransactionManagerApplication;

  @GetMapping("/")
  public String main() {
      return "main";
  }

  //Mybatis 처리를 위한 매퍼 인터페이스 자동주입
  @Autowired
  ITicketService dao;
  /*
  트랜젝션 처리를 위한 빈을 자동주입받음.
   */
  @Autowired
  PlatformTransactionManager transactionManager;
  @Autowired
  TransactionDefinition definition;

  MainController(Ex09TransactionManagerApplication ex09TransactionManagerApplication) {
    this.ex09TransactionManagerApplication = ex09TransactionManagerApplication;
  }

  @GetMapping("/buyTicket.do")
  public String buy1() {
    return "buy";
  }

  @PostMapping("/buyTicket.do")
  public String buy2(TicketDTO ticketDTO, PayDTO payDTO,
    HttpServletRequest req, Model model) {

    //구매에 성공한 경우 렌더링 할 템플릿의 경로 설정 
    String viewPath = "success";
    //자동주입 받은 빈을 통해 트랜젝션 처리를 위한 status 인스턴스 생성
    TransactionStatus status = transactionManager.getTransaction(definition);
    //트랜젝션 처리 시작 
    try {
      //1.DB처리1 : 구매 금액에 대한 insert처리. 구매 수량 * 10000원
      payDTO.setAmount(ticketDTO.getT_count() * 10000);
      int result = dao.insertPay(payDTO);
      //insert에 성공하면 로그 출력 
      if(result == 1)
        System.out.println("transaction_pay 입력성공");

      //2.비즈니스 로직 처리(의도적인 에러 발생 부분)
      String errFlag = req.getParameter("err_flag");
      //구매 페이지에서 체크박스에 체크한 경우..
      if(errFlag != null) {
        /* '100원' 이라는 문자열을 정수로 변환을 시도하므로 NumberFormationException이 발생된다.
        즉 비즈니스 로직에서 에러가 발생한다. */
        int money = Integer.parseInt("100원");
      }

      //3.DB처리2 : 구매한 티켓 매수에 대한 처리로 5장 이하로만 구매가능 
      int result2 = dao.insertTicket(ticketDTO);
      //만약 5장을 초과하면 check제약조건 위배로 DB에러가 발생된다. 
      if(result2 == 1)
        System.out.println("transaction_ticket 입력성공");

      model.addAttribute("ticketDTO", ticketDTO);
      model.addAttribute("payDTO", payDTO);

      /*
      앞에서 문제 발생없이 3개의 업무가 정상적으로 처리되었다면 모든 DB작업을 실제 테이블에 반영한다. 즉 
      '커밋' 처리를 한다. */
      transactionManager.commit(status);
    }
    catch (Exception e) {
      e.printStackTrace();
      //3개의 작업중 하나라도 문제가 생기면 error 페이지 렌더링  
      viewPath = "error";
      //모든 작업을 롤백 처리 한다. 
      transactionManager.rollback(status);
    }
    return viewPath;
  }
}