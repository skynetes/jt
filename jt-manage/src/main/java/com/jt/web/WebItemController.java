package com.jt.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
@RestController
@RequestMapping("/web/item")
public class WebItemController {

	@Autowired
	private ItemService itemService;

	//查询商品信息
	@RequestMapping("/findItemById")
	public Item findItemById(Long itemId) {

		return itemService.findItemById(itemId);
	}

	//查询商品详情信息
	@RequestMapping("/findItemDescById")
	public ItemDesc findItemDescById(Long itemId) {

		return itemService.findItemDescById(itemId);
	}

}
