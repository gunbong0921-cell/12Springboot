package com.edu.spingboot.jdbc;

import org.apache.ibatis.annotations.Mapper;

//구매한 티켓과 금액에 대한 insert 처리를 위한 추상매서드 정의
@Mapper
public interface ITicketService {
  //폼값 처리를 위해 커맨드객체를 매개변수로 선언
  public int ticketInsert(TicketDTO ticketDTO);
  public int payInsert(PayDTO payDTO);
  //티켓구매 시도를 한 회원의 이력을 테이블에 추가
  public int memberRegist(TicketDTO ticketDTO);
}
