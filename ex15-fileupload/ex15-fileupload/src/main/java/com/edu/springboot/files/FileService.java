package com.edu.springboot.files;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.io.File;

//서비스 역할을 클래스로 컨테이너 시작시 자동으로 빈 생성
@Service
public class FileService {
  
  //싱글 파일 업로드 처리
  public Map<String, Object> uploadFile(HttpServletRequest req) {
    Map<String, Object> resultMap = new HashMap<>();
    try {
      String uploadDir = ResourceUtils.getFile("classpath:static/uploads/").toPath()
      .toString();

      Part part = req.getPart("ofile");
      String partHeader = part.getHeader("content-disposition");
      String[] phArr = partHeader.split("filename=");
      String originalFileName = phArr[1].trim().replaceAll("\"", "");
      if (!originalFileName.isEmpty()) {
        part.write(uploadDir + File.separator + originalFileName);
        String savedFileName = FileUtils.renameFile(uploadDir, originalFileName);
        resultMap.put("originalFileName", originalFileName);
        resultMap.put("savedFileName", savedFileName);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return resultMap;
  }

  //멀티 파일 업로드 처리. request 내장객체를 매개변수로 선언.  
  public Map<String, Object> uploadMultiFiles(HttpServletRequest req) {
    //2개 이상의 파일의 정보 저장을 위해 Map컬렉션 생성 
    Map<String, Object> resultMap = new HashMap<>();
    Map<String, String> saveFileMaps = new HashMap<>();
    try {
      //1. 물리적 경로 설정
      /*
      업로드를 위한 파일들은 대부분 이미지나 문서와 같은 정적파일이므로 static 하위로 지정한다. */
      String uploadDir = ResourceUtils.getFile("classpath:static/uploads/").toPath()
      //전송된 값을 request내장객체의 getParts()를 이요해서 컬렉션으로 얻어온다.
      .toString();

      Collection<Part> parts = req.getParts();
      //확장 for문으로 갯수만큼 반복한다. 
      for (Part part : parts) {
        //파라미터가 파일(ofile)인 경우에만 아래 코드 실행. 
        if (!part.getName().equals("ofile")) {
          continue;
          //파일이 아니라면 for문의 처음으로 돌아간다. 
        }

        //헤더값을 통해 파일명 추출 
        String partHeader = part.getHeader("content-disposition");
        String[] phArr = partHeader.split("filename=");
        String originalFileName = phArr[1].trim().replaceAll("\"", "");

        //파일명이 빈값이 아니라면 내부 블럭 실행 
        if (!originalFileName.isEmpty()) {
          //1.임시저장
          part.write(uploadDir + File.separator + originalFileName);
          //2.파일명 변경(UUID 등)
          String savedFileName = FileUtils.renameFile(uploadDir, originalFileName);
          //3.맵에 저장(원본파일명, 변경된파일명)
          saveFileMaps.put(originalFileName, savedFileName);
        }
      }
      //결과 데이터 조립
      resultMap.put("saveFileMaps", saveFileMaps);
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return resultMap;
  }
}
