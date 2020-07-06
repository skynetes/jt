package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service	//dubbo的注解
public class DubboCartServiceImpl implements DubboCartService {
	
	@Autowired
	private CartMapper cartMapper;

	
	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", userId);
		return cartMapper.selectList(queryWrapper);
	}


	/**
	 * 购物车唯一标识: user_id和item_id
	 * 约束条件: 用户重复购物,不会生成新的购物信息,只会数量相加
	 * 步骤:
	 * 	1.根据userId和itemId查询数据库.
	 * 	true有结果: 数量相加 更新
	 * 	false没有结果 : 新增
	 */
	@Override
	public void addCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id",cart.getUserId())
				.eq("item_id", cart.getItemId());
		//查询数据库是否有记录
		Cart cartDB = cartMapper.selectOne(queryWrapper); 
		if(cartDB ==null) {
			cart.setCreated(new Date())
				.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else {
			int num = cartDB.getNum() + cart.getNum();
			cartDB.setNum(num)
				  .setUpdated(new Date());
			//根据主键更新,其他的属性当做set条件.
			cartMapper.updateById(cartDB);
		}
	}

	
	/**
	 * 条件: user_id/item_id num
	 */
	@Override
	public void updateCartNum(Cart cart) {
		//需要修改的数据
		Cart cartTemp = new Cart();
		cartTemp.setNum(cart.getNum())
				.setUpdated(new Date());
		//条件构造器
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<Cart>();
		updateWrapper.eq("item_id", cart.getItemId())
					 .eq("user_id", cart.getUserId());
		
		cartMapper.update(cartTemp, updateWrapper);
	}

	//sql: delete from tb_cart where user_id=xxx and item_id=xxxx
	@Override
	public void deleteCart(Cart cart) {
		
		QueryWrapper<Cart> queryWrapper = 
				new QueryWrapper<Cart>(cart);
		//将属性中不为null的元素当做where条件
		cartMapper.delete(queryWrapper);
		
	}

}
