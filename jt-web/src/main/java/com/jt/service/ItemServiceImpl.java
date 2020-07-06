package com.jt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.anno.CacheFind;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.util.HttpClientService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private HttpClientService httpClient;
	
	@Override
	@CacheFind
	public Item findItemById(Long itemId) {
		
		String url = "http://manage.jt.com/web/item/findItemById?itemId="+itemId;
		return httpClient.doGet(url, Item.class);
	}

	@Override
	@CacheFind
	public ItemDesc findItemDescById(Long itemId) {
		
		String url = "http://manage.jt.com/web/item/findItemDescById?itemId="+itemId;
		return httpClient.doGet(url, ItemDesc.class);
	}
	
}
