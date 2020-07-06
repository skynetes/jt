package com.jt.quartz;

import java.util.Calendar;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.OrderMapper;
import com.jt.pojo.Order;

//准备订单定时任务
@Component
public class OrderQuartz extends QuartzJobBean{

	@Autowired
	private OrderMapper orderMapper;
	
	/**
	 * 实现超时任务的处理
	 * 条件:  status=1  and now -created > 30分钟  
	 * 变形:  status=1 and  created < now-30 
	 * 结果:	  status=6交易关闭 updated = now
	 */
	@Override
	@Transactional
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//1.Calendar对象 采用格林威治时间
		Calendar calendar = Calendar.getInstance(); //获取当前时间
		calendar.add(Calendar.MINUTE, -30);
		//利用工具API进行计算,获取时间
		Date timeOut = calendar.getTime();	
		
		Order order = new Order();
		order.setStatus(6).setUpdated(new Date());
		UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("status", 1)
					 .lt("created", timeOut);
		orderMapper.update(order, updateWrapper);
		System.out.println("定时任务执行完成!!!!!");
	}
	
}
