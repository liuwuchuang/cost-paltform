package com.lwc.platform.service;

import com.lwc.platform.bean.User;
import com.lwc.platform.common.ActionResult;

public interface UserService {

	ActionResult addUser(String username,String password);
	
	ActionResult addUserToken(String username,String password);
	
	ActionResult getUserInfo(String accessToken);
	
	ActionResult addMenu(String accessToken,String json);
	
	ActionResult getMenuList(String accessToken);
	
	User getUserByToken(String accessToken);
	
	ActionResult findByPage(String token,Integer limit,Integer pageNum);
}
