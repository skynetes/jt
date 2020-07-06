package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	/**
	 * 根据ItemCatId查询数据
	 * url: /item/cat/queryItemName
	 * 参数:{itemCatId:val}
	 * 返回值: itemCat名称
	 */
	@RequestMapping("/queryItemName")
	public String findItemCatNameById(Long itemCatId) {
		
		ItemCat itemCat = 
				itemCatService.findItemCatById(itemCatId);
		
		return itemCat.getName();
	}
	
	
	/**
	 * 商品分类List<ItemCat>信息树形结构展现List<EasyUITree>. 
	 * 
	 * url:/item/cat/list
	 * 参数: 第一次没有传递数据   id
	 * 		 点击一级菜单查询二级菜单时传递id
	 * 返回值结果:  List<EasyUITree>
	 */
	@RequestMapping("/list")
	public List<EasyUITree> findItemCat(
			@RequestParam(name="id",defaultValue = "0")Long parentId){
		
		//执行数据库调用
		return itemCatService.findItemCat(parentId);
		//return itemCatService.findItemCatCache(parentId);
	}
	
	
	
}
