package com.logbasex.aop.service;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

	@Override
	public void hello() {
		System.out.println("Say hello!");
	}
}