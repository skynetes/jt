package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Reference(check = false)
	private DubboCartService cartService;
	
	/**
	 *   跳转到购物车展现页面
	 *   返回数据: ${cartList}
	 *   查询条件: userId
	 *   	
	 * @return
	 */
	@RequestMapping("/show")
	public String show(Model model,HttpServletRequest request) {
		User user = (User) request.getAttribute("JT_USER");
		Long userId = user.getId(); //暂时固定,后期维护.
		List<Cart> cartList = 
				cartService.findCartListByUserId(userId);
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	
	
	/**
	 * 参数:itemId商品id号
	 * 返回值:重定向到列表页面
	 * @param cart
	 * @return
	 */
	@RequestMapping("/add/{itemId}")
	public String addCart(Cart cart) {
		
		Long userId = UserThreadLocal.get().getId();
		cart.setUserId(userId);
		cartService.addCart(cart);
		//跳转到购物车展现页面
		return "redirect:/cart/show.html"; 
	}
	
	
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(Cart cart) {
		
		Long userId = UserThreadLocal.get().getId();
		cart.setUserId(userId);
		cartService.updateCartNum(cart);
		return SysResult.success();
	}
	
	/**
	 * url:http://www.jt.com/cart/delete/562379.html
	 * 业务说明:根据userId和ItemId删除数据.之后重定向到
	 *  购物车展现页面
	 */
	@RequestMapping("/delete/{itemId}")
	public String deleteCarts(Cart cart) {
		
		Long userId = UserThreadLocal.get().getId();
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		return "redirect:/cart/show.html"; 
	}
	
	@RequestMapping("/xxx")
	public String xxx(User user) {
		
		return "xxx";
	}
	
}
