package com.edu.spingboot.jdbc;

import org.apache.ibatis.annotations.Mapper;

//구매한 티켓과 금액에 대한 insert 처리를 위한 추상매서드 정의
@Mapper
public interface ITicketService {
  public int insertTicket(TicketDTO ticketDTO);
  public int insertPay(PayDTO payDTO);
}