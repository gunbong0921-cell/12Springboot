package com.edu.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.edu.springboot.jdbc.IMyFileService;
import com.edu.springboot.jdbc.MyFileDTO;
import java.util.List;

@SpringBootTest
class Ex15FileuploadApplicationTests {

	@Autowired
	private IMyFileService myFileService;

	@Test
	void contextLoads() {
	}

	@Test
	void testInsertAndSelect() {
		MyFileDTO dto = new MyFileDTO();
		dto.setTitle("테스트제목");
		dto.setCate("사진,과제");
		dto.setOfile("test_ofile.jpg");
		dto.setSfile("test_sfile_uuid.jpg");

		int insertResult = myFileService.insertFile(dto);
		System.out.println("=== INSERT RESULT: " + insertResult + " ===");

		List<MyFileDTO> list = myFileService.selectFileList();
		System.out.println("=== TOTAL FILES IN DB: " + list.size() + " ===");
		for (MyFileDTO item : list) {
			System.out.println("-> " + item);
		}
	}
}
