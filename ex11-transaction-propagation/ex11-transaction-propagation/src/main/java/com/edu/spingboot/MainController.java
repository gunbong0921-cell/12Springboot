package com.edu.spingboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.edu.spingboot.jdbc.BuyTicketProcess;
import com.edu.spingboot.jdbc.PayDTO;
import com.edu.spingboot.jdbc.TicketDTO;
import com.edu.spingboot.member.AddMemberProcess;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MainController {

  @Autowired
  BuyTicketProcess buyTicketProcess;
  @Autowired
  AddMemberProcess addMemberProcess;

  @GetMapping("/")
  public String main() {
      return "main";
  }

  //티켓 구매페이지 매핑
  @GetMapping("/buyTicket.do")
  public String buy1() {
      return "buy";
  }

  @PostMapping("/buyTicket.do")
  public String buy2(TicketDTO ticketDTO, PayDTO payDTO, HttpServletRequest req, Model model) {
    String viewPath = "success";

    try {
      //1번 서비스 : REQUIRES_NEW 트랜젝션 전파 설정. 티켓을 구매하기 전에 회원 등록을 먼저 처리한다.
      addMemberProcess.memberInsert(ticketDTO, req);

      //2번 서비스 : REQUIRED 트랜젝션 전파 설정. 결제와 티켓 입력을 하나의 트랜잭션으로 처리한다.
      buyTicketProcess.buyTicketAction(ticketDTO, payDTO, req);

      model.addAttribute("ticketDTO", ticketDTO);
      model.addAttribute("payDTO", payDTO);
    }
    catch (Exception e) {
      e.printStackTrace();
      viewPath = "error";
    }
    return viewPath;
  }
}
