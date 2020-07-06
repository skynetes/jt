package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImageVO {
	
	private Integer error;	//0上传正确 1上传有误
	private String url;		//图片的虚拟地址
	private Integer width;	//宽
	private Integer height;	//高
	
	
	public static ImageVO success(String url,Integer width,Integer height) {
		
		return new ImageVO(0, url, width, height);
	}
	//根据分析,需要定义一个失败的方法
	public static ImageVO fail() {
		
		
		return new ImageVO(1, null, null, null);
	}
	
}
