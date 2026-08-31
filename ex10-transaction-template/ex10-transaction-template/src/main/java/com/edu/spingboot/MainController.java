package com.edu.spingboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionTemplate;
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
  //트랜젝션 템플릿을 사용해서 처리를 하기 위해 자동주입. 
  @Autowired
  TransactionTemplate transactionTemplate;

  MainController(Ex09TransactionManagerApplication ex09TransactionManagerApplication) {
    this.ex09TransactionManagerApplication = ex09TransactionManagerApplication;
  }

  //티켓 구매페이지 매핑
  @GetMapping("/buyTicket.do")
  public String buy1() {
      return "buy";
  }

  //티켓 구매 처리 매핑
  @PostMapping("/buyTicket.do")
  public String buy2(TicketDTO ticketDTO, PayDTO payDTO,
      HttpServletRequest req, Model model) {

      String viewPath = "success";

      try {
        /*
        템플릿 빈의 execute() 매서드는 transactionCallback() 이라는 함수형 인터페이스를 매개변수로 받는다.
        이것을 status -> {..} 와 같은 람다식으로 표현할 수 있다. */
        transactionTemplate.execute(status -> {
          //1.DB처리1
          payDTO.setAmount(ticketDTO.getT_count() * 10000);
          int result = dao.insertPay(payDTO);
          if(result == 1)
            System.out.println("transaction_pay 입력성공");

          //2.비즈니스 로직 처리(의도적인 에러 발생 부분)
          String errFlag = req.getParameter("err_flag");
          if(errFlag != null) {
            int money = Integer.parseInt("100원");
          }

          //3.DB처리2 : 구매한 티켓 매수에 대한 처리로 5장 이하로만 구매가능
          int result2 = dao.insertTicket(ticketDTO);
          if(result2 == 1)
            System.out.println("transaction_ticket 입력성공");

          model.addAttribute("ticketDTO", ticketDTO);
          model.addAttribute("payDTO", payDTO);
          //모든 작업에 성공한 경우 commit() 메서드를 호출하지 않아도 된다. 템플릿이 자동으로
          //처리해준다.
          return null;
        });
      }
      catch (Exception e) {
        e.printStackTrace();
        viewPath = "error";
        //작업에 실패한 경우에도 rollback() 호출은 자동으로 처리된다. 
      }
      return viewPath;
  }
}
