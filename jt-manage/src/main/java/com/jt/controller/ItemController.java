package com.jt.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;


@RestController
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * 实现商品列表展现
	 * url地址:/item/query
	 * 参数说明: page=1&rows=10
	 * 返回值: EasyUITable的JSON数据
	 */
	
	@RequestMapping("/query")
	public EasyUITable findItemByPage
							(Integer page,Integer rows) {
		
		return itemService.findItemByPage(page,rows);
	}
	
	/**
	 * 商品新增
	 * 
	 * url:/item/save
	 * 参数: key=value&key2=value2 post
	 * 返回值结果: SysResult VO对象
	 * 
	 * 定义全局异常处理机制!!!! AOP原理
	 */
	@RequestMapping("/save")
	public SysResult saveItem(Item item,ItemDesc itemDesc) {
		
		itemService.saveItem(item,itemDesc); //throws
		return SysResult.success();
	}
	
	
	/**
	 * 完成商品更新操作
	 * url: /item/update
	 * 参数: key=value&key2=value2
	 * 返回值: SysResult
	 */
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {
		
		itemService.updateItem(item,itemDesc);
		return SysResult.success();
	}
	
	
	/**
	 * 完成商品更新操作
	 * url: /item/delete
	 * 参数: "ids":ids   
	 * 返回值: SysResult
	 * 
	 */
	@RequestMapping("/delete")
	public SysResult deleteItems(Long[] ids) {
		
		itemService.deleteItems(ids);
		return SysResult.success();
	}
	
	
	/**
	 * 下架/item/instock   GET
	 */
	@RequestMapping("instock")
	public SysResult instock(Long[] ids) {
		
		int status = 2;
		itemService.updateStatus(ids,status);
		return SysResult.success();
	}
	
	//上架			PUT
	@RequestMapping("reshelf")
	public SysResult reshelf(Long[] ids) {
		
		int status = 1;
		itemService.updateStatus(ids,status);
		return SysResult.success();
	}
	
	/**
	 * url:/item/query/item/desc/1231212
	 * 参数:
	 * 返回值类型: SysResult 必须包含商品详情信息
	 * JSON格式:{"status":200,"msg":"成功",
	 * 			data:{k1:v1,k2:v2,itemDesc:"html代码"
	 * 			}}
	 */
	
	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescById
				(@PathVariable Long itemId) {
		
		ItemDesc itemDesc = 
				itemService.findItemDescById(itemId);
		return SysResult.success(itemDesc);
	}
	
	
	
	
	
	
	
	
	
	
}
