package com.edu.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import java.util.Map;
import com.edu.springboot.files.FileService;
import jakarta.servlet.http.HttpServletRequest;


@Controller
public class MainController {
  
  @GetMapping("/")
  public String main() {
      return "main";
  }

  //파일업로드를 위해 서비스 빈 자동 주입 
  @Autowired
  FileService fileService;

  //싱글 파일업로드 : 작성폼 매핑
  @GetMapping("/fileUpload.do")
  public String fileUpload() {
    return "fileUpload";
  }

  //싱글 파일업로드 : 업로드 처리
  @PostMapping("/uploadProcess.do")
  public String uploadProcess(HttpServletRequest req, Model model) {
    Map<String, Object> uploadMap = fileService.uploadFile(req);

    if (uploadMap != null) {
      uploadMap.put("title", req.getParameter("title"));
      uploadMap.put("cate", req.getParameterValues("cate"));

      model.addAllAttributes(uploadMap);
      return "fileUploadOk";
    }
    else {
      return "redirect:/fileUpload.do?error=upload_failed";
    }
  }

  //멀티 파일업로드 : 작성폼 매핑
  @GetMapping("/multiFileUpload.do")
  public String multiFileUpload() {
    return "multiFileUpload";
  }

  //파일업로드 : 업로드 처리 
  @PostMapping("/multiUploadProcess.do")
  public String multiUploadProcess(HttpServletRequest req, Model model) {
    //서비스 호출을 통해 싱글 파일업로드 로직 수행
    Map<String, Object> uploadMap = fileService.uploadMultiFiles(req);

    //업로드 된 파일이 있다면..
    if (uploadMap != null) {
      //첨부파일 외 나머지 폼값을 받은 후 Map에 저장 
      //text 입력상자와 같은 단일값은 getParameter() 메서드를 통해 가져올 수 있다.
      uploadMap.put("title", req.getParameter("title"));
      //checkbox 입력상자와 같은 다중값은 getParameterValues() 메서드를 통해 가져올 수 있다.
      uploadMap.put("cate", req.getParameterValues("cate"));

      model.addAllAttributes(uploadMap);
      return "multiFileUploadOk";
    }
    else {
      /*
      파일 업로드에 실패한다면 error 라는 파라미터를 추가한 후 업로드폼으로 이동한다. */
      return "redirect:/fileUpload.do?error=upload_failed";
    }
  }
}
