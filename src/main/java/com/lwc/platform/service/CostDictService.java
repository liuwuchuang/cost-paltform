package com.lwc.platform.service;

import com.lwc.platform.common.ActionResult;

public interface CostDictService {

	ActionResult add(String token,String json);
	
	ActionResult delete(String token,String id);
	
	ActionResult findByPage(String token,Integer limit,Integer pageNum);
	
	ActionResult findList(String token);
}
