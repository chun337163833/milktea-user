package com.milktea.milkteauser.service.impl;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.milktea.milkteauser.dao.TeaUserInfoMapper;
import com.milktea.milkteauser.domain.TeaUserInfo;
import com.milktea.milkteauser.exception.MilkTeaException;
import com.milktea.milkteauser.service.SmsService;
import com.milktea.milkteauser.service.UserRegisterService;
import com.milktea.milkteauser.vo.ResponseBody;


@Service("userRegisterService")
public  class UserRegisterServiceImpl implements UserRegisterService {

	@Autowired
	TeaUserInfoMapper TeaUserInfoMapper;
	
	@Autowired
	SmsService smsService;
	
	static Logger logger = LoggerFactory.getLogger(UserLoginServiceImpl.class);
	
	@Override
	public TeaUserInfo findRegisterOpenid(TeaUserInfo teaUserInfo) throws MilkTeaException {
		//用户是否已经注册，及是否已经能取得正确的手机号
		TeaUserInfo teaUserInfoRet = new TeaUserInfo () ;
		if(null != teaUserInfo.getWeixinOpenid()){
			//微信登入的场合返回客户已经登入的信息，如果返回空则说明没有注册过
			teaUserInfoRet = TeaUserInfoMapper.selectByWeixinOpenId(teaUserInfo.getWeixinOpenid());
		}
		
		
		return teaUserInfoRet;
		
	}

	@Override
	public void bindOpenidTel(TeaUserInfo teaUserInfo) throws MilkTeaException {
		
		//当手机号已经存在时 新的微信号和手机号绑定
		TeaUserInfoMapper.bindTelephoneWeixinOpenid(teaUserInfo.getTelephone(),teaUserInfo.getWeixinOpenid(),teaUserInfo.getWeixinId());
		
	}

	@Override
	public TeaUserInfo findRegisterTelephone(TeaUserInfo teaUserInfo) throws MilkTeaException {
		//用户是否已经注册，及是否已经能取得正确的手机号
				TeaUserInfo teaUserInfoRet = new TeaUserInfo () ;
				
				if(null != teaUserInfo.getTelephone()){
					//已经有了手机号 则直接返回详细信息，如果返回NULL则说明没有注册过
					teaUserInfoRet = TeaUserInfoMapper.selectByTelephone(teaUserInfo.getTelephone());
					return teaUserInfoRet;
				}
			return teaUserInfoRet;
	}

	@Override
	public String getPollCode(TeaUserInfo teaUserInfo) throws MilkTeaException {
		String returnStr = ""; 
		
		//调用短消息发送
		//生成随机码 4位
		long t = System.currentTimeMillis();
		Random r1 = new Random(t);
		int i;
		i=r1.nextInt(10);
		returnStr = returnStr + String.valueOf(i);
		i=r1.nextInt(10);
		returnStr = returnStr + String.valueOf(i);
		i=r1.nextInt(10);
		returnStr = returnStr + String.valueOf(i);
		i=r1.nextInt(10);
		returnStr = returnStr + String.valueOf(i);
		
		//调用阿里云短信发送
		// TODO: 调用阿里云短信发送 参数 电话号码 验证码为随机数
		//SMS_134326005 是认证模板号
		ResponseBody<String> responseStr = new ResponseBody<String>();
		responseStr = smsService.sendVerCodeSMS(teaUserInfo.getTelephone(),"SMS_134326005");
		
		
		//TODO：写入数据库 ？？？？
		
		return returnStr;
		
	}



	
    
}
