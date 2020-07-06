package com.jt.util;

import com.jt.pojo.User;

public class UserThreadLocal {
	
	private static ThreadLocal<User> thread  = new ThreadLocal<>();
	
	public static void set(User user) {
		
		thread.set(user);
	}
	
	public static User get() {
		
		return thread.get();
	}
	
	//切记添加防止内存泄漏的操作
	public static void remove() {
		
		thread.remove();
	}
	
}
