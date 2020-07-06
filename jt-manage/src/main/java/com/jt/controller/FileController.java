package com.jt.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.ImageVO;

@RestController
public class FileController {
	
	@Autowired
	private FileService fileService;
	
	/**
	 * 文件上传时,参数名称必须一致.
	 * MultipartFile:SpringMVC 专门处理文件上传的
	 * 工具API
	 * 文件上传语法:   a.jpg
	 * 		指定路径: D:\images\a.jpg
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping("/file")
	public String file(MultipartFile fileImage) throws IllegalStateException, IOException {
		//1.准备文件目录
		File fileDir = new File("D:/images");
		if(!fileDir.exists()) {
			//如果文件不存在,则创建目录
			fileDir.mkdirs();
		}
		//2.获取文件名称
		String name = fileImage.getOriginalFilename();
		
		//3.准备文件路径
		String filePath = "D:/images/"+name;
		
		//4.实现文件上传
		fileImage.transferTo(new File(filePath));
		
		System.out.println("程序执行成功!!!!");
		return "文件上传成功!!!!!";
	}
	
	/**
	 * 实现商品文件上传
	 * url:	 /pic/upload
	 * 参数: uploadFile名称
	 * 结果: ImageVO对象
	 */
	@RequestMapping("/pic/upload")
	public ImageVO upload(MultipartFile uploadFile) {
		
		return fileService.upload(uploadFile);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
