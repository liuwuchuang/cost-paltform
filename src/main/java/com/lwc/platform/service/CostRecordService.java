package com.lwc.platform.service;

import com.lwc.platform.common.ActionResult;

public interface CostRecordService {

	ActionResult add(String token,String json);
	
	ActionResult delete(String token,String id);
	
	ActionResult findByPage(String token,Integer limit,Integer pageNum,String start,String end);
	
	ActionResult countByDict(String token,String start,String end);
	
	ActionResult countByType(String token,String start,String end);
}
