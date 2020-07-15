package com.lwc.platform.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.lwc.platform.bean.CostDict;
import com.lwc.platform.bean.User;
import com.lwc.platform.cache.DictCache;
import com.lwc.platform.common.ActionResult;
import com.lwc.platform.dto.DictDto;
import com.lwc.platform.service.CostDictService;
import com.lwc.platform.service.UserService;

@Service
public class CostDictServiceImpl implements CostDictService{

	private Gson gson=new Gson();
	
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private UserService userService;
	@Autowired
	private DictCache dictCache;
	
	@Override
	public ActionResult add(String token, String json) {
		//先校验token
		User user = userService.getUserByToken(token);
		if(user == null) {
			return new ActionResult(405,"请先登录",null);
		}
		//再校验json
		if(StringUtils.isEmpty(json)) {
			return new ActionResult(400,"填写的信息有误",null);
		}
		DictDto dto = gson.fromJson(json, DictDto.class);
		if(StringUtils.isEmpty(dto.getName()) || StringUtils.isEmpty(dto.getComments())) {
			return new ActionResult(400,"信息不能为空",null);
		}
		CostDict cd=new CostDict(dto.getName(), dto.getComments(),user.getId());
		cd=mongoTemplate.save(cd);
		dictCache.dictMap.put(cd.getId(), cd.getName());
		return new ActionResult(200,"success",null);
	}

	@Override
	public ActionResult delete(String token, String id) {
		//先校验token
		User user = userService.getUserByToken(token);
		if(user == null) {
			return new ActionResult(405,"请先登录",null);
		}
		Query query = new Query(Criteria.where("_id").is(id));
		mongoTemplate.remove(query, CostDict.class);
		dictCache.dictMap.remove(id);
		return new ActionResult(200,"success",null);
	}

	@Override
	public ActionResult findByPage(String token, Integer pageSize, Integer pageNum) {
		//先校验token
		User user = userService.getUserByToken(token);
		if(user == null) {
			return new ActionResult(405,"请先登录",null);
		}
		Query query = Query.query(Criteria.where("user_id").is(user.getId()));
		// 总条数
		long totalCount = mongoTemplate.count(query, CostDict.class);
		// 数据总页数
		long totalPage = (totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1);
		// 分页查询
		query.skip((pageNum-1) * pageSize).limit(pageSize);
		List<CostDict> rows = mongoTemplate.find(query, CostDict.class);
		//返回数据
		Map<String,Object> map=new HashMap<>();
		map.put("totalCount", totalCount);
		map.put("totalPage", totalPage);
		map.put("rows", rows);
		return new ActionResult(0,"success",map);
	}

	@Override
	public ActionResult findList(String token) {
		//先校验token
		User user = userService.getUserByToken(token);
		if(user == null) {
			return new ActionResult(405,"请先登录",null);
		}
		Query query = Query.query(Criteria.where("user_id").is(user.getId()));
		List<CostDict> list = mongoTemplate.find(query, CostDict.class);
		List<DictDto> dtoList=parseCostDictList(list);
		Map<String,Object> map=new HashMap<>();
		map.put("rows", dtoList);
		return new ActionResult(200,"success",map);
	}

	private List<DictDto> parseCostDictList(List<CostDict> list) {
		List<DictDto> dtoList=new LinkedList<>();
		for(CostDict cd : list) {
			DictDto dto=new DictDto(cd.getId(), cd.getName());
			dtoList.add(dto);
		}
		return dtoList;
	}
}
