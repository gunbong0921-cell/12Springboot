# 📂 파일 업로드 및 MyBatis DB 연동 퀴즈 풀이 워크스루 (WALKTHROUGH)

본 문서는 **Spring Boot + MyBatis + Oracle DB** 환경에서 파일 업로드 정보 저장 및 결과 목록을 구현한 **[퀴즈 1]**과 **[퀴즈 2]**의 전체 구현 과정과 해결 방법을 정리한 문서입니다.

---

## 📌 목차
1. [퀴즈 요구사항 정리](#1-퀴즈-요구사항-정리)
2. [오라클 DB 테이블 및 시퀀스 설계](#2-오라클-db-테이블-및-시퀀스-설계)
3. [프로젝트 설정 (의존성 및 환경설정)](#3-프로젝트-설정-의존성-및-환경설정)
4. [MyBatis 데이터 계층 구현](#4-mybatis-데이터-계층-구현)
5. [컨트롤러 및 뷰 구현](#5-컨트롤러-및-뷰-구현)
6. [검증 및 실행 결과](#6-검증-및-실행-결과)
7. [트러블슈팅 가이드](#7-트러블슈팅-가이드)

---

## 1. 퀴즈 요구사항 정리

### 📝 퀴즈 1
- `MYFILE` 테이블을 생성하고, 파일 업로드 시 업로드된 파일의 정보(제목, 카테고리, 원본파일명, 저장파일명 등)를 **MyBatis**를 사용하여 DB에 `INSERT`한다.
- 테이블 컬럼 크기에 문제가 있다면 수정/보완 후 진행한다.

### 📝 퀴즈 2
- 등록이 완료된 후 이동하는 결과 페이지(`fileUploadOk.html`, `multiFileUploadOk.html`)에서 단순 텍스트 출력을 **게시판 형태의 목록 테이블**(No, Title, 카테고리, 원본파일명, 이미지썸네일, 날짜)로 변경한다.

---

## 2. 오라클 DB 테이블 및 시퀀스 설계

### 🔍 컬럼 크기 문제점 분석 및 개선
1. **`OFILE VARCHAR2(100 BYTE)` 크기 부족 문제**:
   - Oracle `AL32UTF8` 캐릭터셋 환경에서 한글 1글자는 **3 Byte**를 차지합니다.
   - `100 BYTE`는 한글 약 33자까지만 저장 가능하여 긴 한글 파일명(예: `2026학년도_1학기_컴퓨터공학과_졸업작품_중간보고서_홍길동.docx`) 업로드 시 `ORA-12899: value too large for column` 오류가 발생합니다.
   - **해결**: `OFILE VARCHAR2(200 BYTE)`로 확장.
2. **`SFILE VARCHAR2(50 BYTE)` 여유 공간 확보**:
   - UUID(32자) + 확장자(3~5자)는 약 37 Byte로 50 Byte 내에 저장 가능하나, 확장자가 길거나 파일명 규칙 확장 시를 대비하여 `SFILE VARCHAR2(100 BYTE)`로 확장.
3. **`IDX` 일련번호 자동 증가**:
   - `seq_myfile_num` 시퀀스를 생성하여 `seq_myfile_num.NEXTVAL`로 주입.

### 📄 실행 DDL 스크립트 (`src/main/resources/myfile.sql`)
```sql
-- 1. 시퀀스 생성 (IDX 자동 증가용)
DROP SEQUENCE seq_myfile_num;
CREATE SEQUENCE seq_myfile_num
    INCREMENT BY 1
    START WITH 1
    NOMINVALUE
    NOMAXVALUE
    NOCACHE;

-- 2. MYFILE 테이블 생성
DROP TABLE MYFILE CASCADE CONSTRAINTS;
CREATE TABLE MYFILE (
    IDX       NUMBER              NOT NULL,
    TITLE     VARCHAR2(200 BYTE)  NOT NULL,
    CATE      VARCHAR2(100 BYTE),
    OFILE     VARCHAR2(200 BYTE)  NOT NULL,  -- 한글 파일명 용량 확보 (100 -> 200)
    SFILE     VARCHAR2(100 BYTE)  NOT NULL,  -- 저장 파일명 용량 확보 (50 -> 100)
    POSTDATE  DATE                DEFAULT SYSDATE NOT NULL,
    CONSTRAINT myfile_pk PRIMARY KEY (IDX)
);
```

---

## 3. 프로젝트 설정 (의존성 및 환경설정)

### 1) `build.gradle` 의존성 추가
```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-jdbc'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    implementation 'org.springframework.boot:spring-boot-starter-webmvc'
    implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:4.0.1'
    compileOnly 'org.projectlombok:lombok'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    runtimeOnly 'com.oracle.database.jdbc:ojdbc17'
    annotationProcessor 'org.projectlombok:lombok'
    providedRuntime 'org.springframework.boot:spring-boot-starter-tomcat-runtime'
    testImplementation 'org.springframework.boot:spring-boot-starter-thymeleaf-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-webmvc-test'
    testImplementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter-test:4.0.1'
    testCompileOnly 'org.projectlombok:lombok'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
    testAnnotationProcessor 'org.projectlombok:lombok'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
}
```

### 2) `src/main/resources/application.properties` 설정
```properties
spring.application.name=ex15-fileupload

# 포트설정
server.port=8282

# Spring Boot Dashboard JMX 활성화
spring.jmx.enabled=true
spring.application.admin.enabled=true

# 파일업로드를 위한 multipart 설정
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=2MB
spring.servlet.multipart.max-request-size=20MB

# oracle 접속 정보
spring.datasource.driver-class-name=oracle.jdbc.driver.OracleDriver
spring.datasource.url=jdbc:oracle:thin:@localhost:1523/XEPDB1
spring.datasource.username=boot_user
spring.datasource.password=1234

# mybatis 매퍼 위치 설정
mybatis.mapper-locations=classpath:mapper/**/*.xml
```

---

## 4. MyBatis 데이터 계층 구현

### 1) `MyFileDTO.java` (`com.edu.springboot.jdbc`)
```java
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
```

### 2) `IMyFileService.java` (`com.edu.springboot.jdbc`)
```java
package com.edu.springboot.jdbc;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMyFileService {
    // 업로드 파일 정보 DB 저장 (INSERT)
    public int insertFile(MyFileDTO dto);

    // 업로드 파일 목록 조회 (SELECT)
    public ArrayList<MyFileDTO> selectFileList();
}
```

### 3) `MyFileDAO.xml` (`src/main/resources/mapper/MyFileDAO.xml`)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
    PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.edu.springboot.jdbc.IMyFileService">

    <!-- 업로드 파일 정보 DB 등록 -->
    <insert id="insertFile" parameterType="com.edu.springboot.jdbc.MyFileDTO">
        INSERT INTO myfile (idx, title, cate, ofile, sfile)
        VALUES (seq_myfile_num.NEXTVAL, #{title}, #{cate, jdbcType=VARCHAR}, #{ofile}, #{sfile})
    </insert>

    <!-- 업로드된 파일 전체 목록 조회 -->
    <select id="selectFileList" resultType="com.edu.springboot.jdbc.MyFileDTO">
        SELECT idx, title, cate, ofile, sfile, postdate
        FROM myfile
        ORDER BY idx DESC
    </select>

</mapper>
```

---

## 5. 컨트롤러 및 뷰 구현

### 1) `MainController.java` 핵심 로직
```java
package com.edu.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import java.util.Map;
import com.edu.springboot.files.FileService;
import com.edu.springboot.jdbc.IMyFileService;
import com.edu.springboot.jdbc.MyFileDTO;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MainController {
  
  @Autowired
  FileService fileService;

  @Autowired
  IMyFileService myFileService;

  @GetMapping("/")
  public String main() {
      return "main";
  }

  @GetMapping("/fileUpload.do")
  public String fileUpload() {
    return "fileUpload";
  }

  // [퀴즈 1 & 2] 싱글 파일 업로드 및 DB INSERT 후 목록 전달
  @PostMapping("/uploadProcess.do")
  public String uploadProcess(HttpServletRequest req, Model model) {
    Map<String, Object> uploadMap = fileService.uploadFile(req);

    if (uploadMap != null) {
      String title = req.getParameter("title");
      String[] cateArr = req.getParameterValues("cate");
      String cate = (cateArr != null) ? String.join(",", cateArr) : "";

      uploadMap.put("title", title);
      uploadMap.put("cate", cateArr);

      // MyBatis를 이용한 DB Insert
      MyFileDTO dto = new MyFileDTO();
      dto.setTitle(title);
      dto.setCate(cate);
      dto.setOfile((String) uploadMap.get("originalFileName"));
      dto.setSfile((String) uploadMap.get("savedFileName"));

      int result = myFileService.insertFile(dto);
      System.out.println("Single File DB Insert Result: " + result);

      model.addAllAttributes(uploadMap);
      // DB 전체 목록 조회하여 모델에 저장
      model.addAttribute("fileList", myFileService.selectFileList());
      return "fileUploadOk";
    } else {
      return "redirect:/fileUpload.do?error=upload_failed";
    }
  }

  @GetMapping("/multiFileUpload.do")
  public String multiFileUpload() {
    return "multiFileUpload";
  }

  // [퀴즈 1 & 2] 멀티 파일 업로드 및 DB INSERT 후 목록 전달
  @PostMapping("/multiUploadProcess.do")
  public String multiUploadProcess(HttpServletRequest req, Model model) {
    Map<String, Object> uploadMap = fileService.uploadMultiFiles(req);

    if (uploadMap != null) {
      String title = req.getParameter("title");
      String[] cateArr = req.getParameterValues("cate");
      String cate = (cateArr != null) ? String.join(",", cateArr) : "";

      uploadMap.put("title", title);
      uploadMap.put("cate", cateArr);

      // 업로드된 파일 정보들을 각각 DB에 Insert
      @SuppressWarnings("unchecked")
      Map<String, String> saveFileMaps = (Map<String, String>) uploadMap.get("saveFileMaps");
      if (saveFileMaps != null) {
        for (Map.Entry<String, String> entry : saveFileMaps.entrySet()) {
          MyFileDTO dto = new MyFileDTO();
          dto.setTitle(title);
          dto.setCate(cate);
          dto.setOfile(entry.getKey());
          dto.setSfile(entry.getValue());

          int result = myFileService.insertFile(dto);
          System.out.println("Multi File DB Insert (" + entry.getKey() + "): " + result);
        }
      }

      model.addAllAttributes(uploadMap);
      // DB 전체 목록 조회하여 모델에 저장
      model.addAttribute("fileList", myFileService.selectFileList());
      return "multiFileUploadOk";
    } else {
      return "redirect:/multiFileUpload.do?error=upload_failed";
    }
  }

  // DB에 저장된 파일 목록 전체 보기
  @GetMapping("/fileList.do")
  public String fileList(Model model) {
    model.addAttribute("fileList", myFileService.selectFileList());
    return "fileList";
  }
}
```

### 2) 결과 뷰 템플릿 (`fileUploadOk.html` / `multiFileUploadOk.html`)
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="UTF-8">
<title>파일업로드 성공</title>
<style>
  table {
    border-collapse: collapse;
    width: 85%;
    margin-top: 15px;
  }
  th, td {
    border: 1px solid #333;
    padding: 8px 12px;
    text-align: center;
  }
  th {
    background-color: #f9f9f9;
    font-weight: bold;
  }
  td.title {
    text-align: left;
  }
  .thumb {
    width: 100px;
    max-height: 100px;
    vertical-align: middle;
  }
</style>
</head>
<body>
  <h2>파일 업로드 성공</h2>

  <p>
    <a href="/fileUpload.do">파일 및 폼값 전송하기</a> | 
    <a href="/">메인으로</a>
  </p>

  <!-- [퀴즈 2] 게시판 목록형 테이블 -->
  <table border="1">
    <thead>
      <tr>
        <th style="width: 8%;">No</th>
        <th style="width: 22%;">Title</th>
        <th style="width: 22%;">카테고리</th>
        <th style="width: 18%;">원본파일명</th>
        <th style="width: 16%;">이미지썸네일</th>
        <th style="width: 14%;">날짜</th>
      </tr>
    </thead>
    <tbody>
      <tr th:if="${#lists.isEmpty(fileList)}">
        <td colspan="6">등록된 파일이 없습니다.</td>
      </tr>
      <tr th:each="row, status : ${fileList}">
        <td th:text="${row.idx}">1</td>
        <td th:text="${row.title}" class="title">하이룽~</td>
        <td th:text="${row.cate}">사진,과제,워드 음원</td>
        <td th:text="${row.ofile}">리포트.jpg</td>
        <td>
          <img th:src="@{|/uploads/${row.sfile}|}" alt="썸네일" class="thumb" 
               onerror="this.style.display='none'" />
        </td>
        <td th:text="${row.postdate}">2024-01-01</td>
      </tr>
    </tbody>
  </table>
</body>
</html>
```

---

## 6. 검증 및 실행 결과

1. **Gradle 빌드 및 테스트**:
   - `./gradlew compileJava testClasses`: 컴파일 및 리소스 빌드 정상 완료 (에러 0건)
   - `Ex15FileuploadApplicationTests` 테스트 실행 결과 `INSERT` 및 `SELECT` 쿼리 정상 동작 검증 완료
2. **실제 DB 저장 데이터**:
   ```
   idx=1 | title=저장1 | cate=사진,과제,워드 | ofile=apple.png        | sfile=ebf16f2d0fcb406c89e13f57cbb12d30.png
   idx=2 | title=저장2 | cate=사진,과제,워드 | ofile=ElvisPresley.png | sfile=546a8a67c3df49c29b55ba894e1cbe57.png
   idx=3 | title=저장2 | cate=사진,과제,워드 | ofile=banana.png       | sfile=c8ae15e62d5341beafa88c13f61fdc14.png
   idx=4 | title=저장2 | cate=사진,과제,워드 | ofile=dog.png          | sfile=ebb7daedd5554212921b268debc361a1.png
   idx=5 | title=저장2 | cate=사진,과제,워드 | ofile=bird.jpg         | sfile=25f6e68df03d47a398307d41bb72d502.jpg
   idx=6 | title=저장2 | cate=사진,과제,워드 | ofile=girl.png         | sfile=40c68f7b361b444f8297762459f5d1af.png
   ```

---

## 7. 트러블슈팅 가이드

| 발생 오류 / 증상 | 원인 | 해결 방법 |
| :--- | :--- | :--- |
| **`ORA-02289: 시퀀스가 존재하지 않습니다`** | Oracle DB에 `seq_myfile_num` 시퀀스가 생성되지 않음 | `CREATE SEQUENCE seq_myfile_num INCREMENT BY 1 START WITH 1 NOCACHE;` 실행 |
| **`ORA-12899: value too large for column`** | 한글 파일명(3Byte)으로 인해 `OFILE VARCHAR2(100)` 크기 초과 | `ALTER TABLE MYFILE MODIFY (OFILE VARCHAR2(200 BYTE));` 실행 |
| **SQL Developer에서 데이터가 안 보임** | 캐시 또는 뷰 미갱신, 다른 DB 접속 계정 조회 | 1) 테이블 데이터 탭에서 **새로고침(F5)** 클릭<br>2) `SELECT * FROM MYFILE;` 직접 실행<br>3) 접속 계정이 `boot_user`인지 확인 |
