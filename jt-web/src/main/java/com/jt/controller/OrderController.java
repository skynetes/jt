package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Reference
	private DubboCartService cartService;
	
	@Reference
	private DubboOrderService orderService;
	
	@RequestMapping("/create")
	public String create(Model model){
		
		//根据用户Id信息.获取全部购物车记录
		Long userId = UserThreadLocal.get().getId();
		List<Cart> cartList = 
				cartService.findCartListByUserId(userId);
		model.addAttribute("carts", cartList);
		
		return "order-cart";
	}
	
	
	@RequestMapping("/submit")
	@ResponseBody	//返回json数据
	public SysResult saveOrder(Order order) {
		//获取userId
		Long userId = UserThreadLocal.get().getId();
		order.setUserId(userId);
		
		//需要返回订单号orderId.
		String orderId = orderService.saveOrder(order);
		return SysResult.success(orderId);
	}
	
	/**
	 * 业务:根据orderId查询记录
	 * 参数: id=xxxxxxx
	 * 返回值: 跳转到success
	 * 携带参数:${order.orderId}
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("success")
	public String findOrderById(String id,Model model) {
		
		Order order = orderService.findOrderById(id);
		model.addAttribute("order", order);
		return "success";
	}
	
	
	
	
	
	
	
	
	
}
