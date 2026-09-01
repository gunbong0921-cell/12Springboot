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
  
  @GetMapping("/")
  public String main() {
      return "main";
  }

  //파일업로드를 위해 서비스 빈 자동 주입 
  @Autowired
  FileService fileService;

  //MyBatis 매퍼 서비스 빈 자동 주입
  @Autowired
  IMyFileService myFileService;

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
      // DB에서 전체 파일 목록 조회하여 모델에 저장
      model.addAttribute("fileList", myFileService.selectFileList());
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
    //서비스 호출을 통해 멀티 파일업로드 로직 수행
    Map<String, Object> uploadMap = fileService.uploadMultiFiles(req);

    //업로드 된 파일이 있다면..
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
      // DB에서 전체 파일 목록 조회하여 모델에 저장
      model.addAttribute("fileList", myFileService.selectFileList());
      return "multiFileUploadOk";
    }
    else {
      /*
      파일 업로드에 실패한다면 error 라는 파라미터를 추가한 후 업로드폼으로 이동한다. */
      return "redirect:/multiFileUpload.do?error=upload_failed";
    }
  }

  // 업로드된 파일 목록 보기
  @GetMapping("/fileList.do")
  public String fileList(Model model) {
    model.addAttribute("fileList", myFileService.selectFileList());
    return "fileList";
  }
}
