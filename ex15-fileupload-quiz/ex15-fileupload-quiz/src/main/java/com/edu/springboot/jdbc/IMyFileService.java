package com.edu.springboot.jdbc;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMyFileService {
    // 업로드 파일 정보 저장 (INSERT)
    public int insertFile(MyFileDTO dto);

    // 업로드 파일 목록 조회 (SELECT)
    public ArrayList<MyFileDTO> selectFileList();
}
