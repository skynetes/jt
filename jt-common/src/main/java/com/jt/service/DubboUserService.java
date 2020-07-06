package com.jt.service;

import com.jt.pojo.User;

/**
 * 定义中立的第三方接口.
 * @author Administrator
 *
 */
public interface DubboUserService {

	void insertUser(User user);

	String findUserByUP(User user, String userIP);

}
