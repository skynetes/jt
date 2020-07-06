package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.ImageVO;

@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService {
	
	@Value("${image.localDir}")
	private String localDir;
	@Value("${image.httpUrl}")
	private String httpUrl;
	
	/**
	 * 真是图片信息: abc.jpg
	 * 实现文件上传思路
	 * 1.判断是否为图片类型  jpg|png|gif
	 * 2.判断是否为恶意程序  1024 *1024 =1048576 
	 * 3.图片必须分目录保存 类目/时间 yyyy/MM/dd
	 * 4.防止图片重名,自定义名称 UUID.类型
	 * 
	 * 正则案例:
	 * 	要求:名称是英文的   [a-z]+
	 *  要求:名称都是中文   [^a-z][^A-Z][^0-9]
	 */
	@Override
	public ImageVO upload(MultipartFile uploadFile) {
		
		//1.获取图片名称 abc.jpg
		String imageName = uploadFile.getOriginalFilename();
		//方式1:list集合 2.map 3.set 4.正则表达式\
		imageName = imageName.toLowerCase();
		if(!imageName.matches("^.+\\.(jpg|png|gif)$")) {
			
			return ImageVO.fail();
		}
		
		//2.是否为恶意程序 主要判断图片属性width和height
		try {
			BufferedImage bufferedImage = 
					ImageIO.read(uploadFile.getInputStream());
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			
			if(width ==0 || height==0) {
				
				return ImageVO.fail();
			}
			
			//3.实现分目录储存 本地磁盘路径+日期目录
			String dateDir = 
					new SimpleDateFormat("/yyyy/MM/dd/")
					.format(new Date());
			
			//D:/images/yyyy/MM/dd/
			String fileDirPath = localDir + dateDir;
			File dirFile = new File(fileDirPath);
			if(!dirFile.exists()) {
				
				//如果目录不存在,则创建目录
				dirFile.mkdirs();	
			}
			
			//4.防止文件重名 文件名称 uuid.类型
			String uuid = UUID.randomUUID().toString();
			int index = imageName.lastIndexOf(".");
			//.jpg
			String fileType = imageName.substring(index);
			String fileName = uuid + fileType;
			
			
			//5.实现文件上传     目录 + 文件名称
			//D:\images\2019\12\05/uuid.jpg
			String realFilePath = fileDirPath+fileName;
			uploadFile.transferTo(new File(realFilePath));
			
			//拼接图片虚拟地址
			String url = httpUrl + dateDir+fileName;
			
			return new ImageVO(0, url, width, height);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ImageVO.fail();
		}
		
	}
}
