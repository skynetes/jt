package com.jt.service;

import java.util.List;

import com.jt.pojo.User;

public interface UserService {

	List<User> findAll();

	boolean findUserByType(String param, Integer type);

}
