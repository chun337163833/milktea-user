package com.milktea.milkteauser.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.milktea.milkteauser.dao.TeaUserInfoMapper;
import com.milktea.milkteauser.domain.TeaUserInfo;
import com.milktea.milkteauser.exception.MilkTeaException;
import com.milktea.milkteauser.service.UserInfoService;


@Service
public  class UserInfoServiceImpl implements UserInfoService {

	@Autowired
	TeaUserInfoMapper teaUserInfoMapper;

	@Override
	public Integer insert(TeaUserInfo teaUserInfo) throws MilkTeaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TeaUserInfo selectByUserId(Integer userId) throws MilkTeaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer update(TeaUserInfo teaUserInfo) throws MilkTeaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TeaUserInfo> selectAll() throws MilkTeaException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
    
}
