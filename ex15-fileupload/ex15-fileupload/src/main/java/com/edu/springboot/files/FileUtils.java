package com.edu.springboot.files;

import java.io.File;
import java.util.UUID;

public class FileUtils {

  /*
  UUID(Universally Unique Identifier) : 법용 고유 식별자로 번역할 수 있다. JDK에서 기본클래스로
  제공되며, 32자의 영문과 숫자를 포함한 고유한 문자열을 생성해준다. 
  */
  public static String getUuid() {
    //UUID 객체 문자열로 생성
    String uuid = UUID.randomUUID().toString();
    //문자열 사이에 하이픈(-)을 제거한 후 파일명으로 사용한다. 
    uuid = uuid.replaceAll("-", "");
    return uuid;
  }

  public static String renameFile(String sDirectory, String fileName) {
    //파일의 확장자를 파일명의 끝에서부터 잘라내기
    String ext = fileName.substring(fileName.lastIndexOf("."));
    //UUID 기반 파일명 생성
    String now = getUuid();
    //확장자와 합쳐서 새로운 파일명으로 만들어준다.
    String newFileName = now + ext;

    //원본 파일명으로로 File객체 생성
    File oldFile = new File(sDirectory, fileName);
    //변경할 파일명으로 File객체 생성
    File newFile = new File(sDirectory, newFileName);
    //파일명 변경
    oldFile.renameTo(newFile);

    return newFileName;
  }
}