package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EasyUITree {
	// [{id:1,text:"aaa",state:"open/closed"}]
	
	private Long id;
	private String text;
	private String state;	//状态信息
}



