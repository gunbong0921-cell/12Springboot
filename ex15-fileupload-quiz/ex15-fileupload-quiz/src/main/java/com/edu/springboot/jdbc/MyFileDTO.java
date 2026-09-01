package com.edu.springboot.jdbc;

import lombok.Data;

@Data
public class MyFileDTO {
    private int idx;
    private String title;
    private String cate;
    private String ofile;
    private String sfile;
    private java.sql.Date postdate;
}
