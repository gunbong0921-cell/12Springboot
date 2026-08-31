package com.edu.spingboot.jdbc;

import lombok.Data;

//transaction_pay 테이블과 동일한 컬럼으로 생성
@Data
public class PayDTO {
  private String userid;
  private int amount;
}
