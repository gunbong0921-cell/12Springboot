package com.edu.spingboot.jdbc;

import lombok.Data;

//transaction_ticket 테이블과 동일하게 생성
@Data
public class TicketDTO {
  private String userid;
  private int t_count;
}
