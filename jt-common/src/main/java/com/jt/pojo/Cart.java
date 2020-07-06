package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("tb_cart")
@Data
@Accessors(chain = true)
public class Cart extends BasePojo{
	@TableId(type = IdType.AUTO)
	private Long id;
	private Long userId;	//用户ID号
	private Long itemId;	//商品ID号
	private String itemTitle;	
	private String itemImage;
	private Long itemPrice;
	private Integer num;

}
