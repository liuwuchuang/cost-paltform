package com.lwc.platform.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.lwc.platform.bean.CostDict;
import com.lwc.platform.bean.RoleMenu;
import com.lwc.platform.bean.User;
import com.lwc.platform.bean.UserToken;
import com.lwc.platform.common.ActionResult;
import com.lwc.platform.dto.MenuItemDto;
import com.lwc.platform.dto.MenuReqDto;
import com.lwc.platform.dto.MenuResDto;
import com.lwc.platform.dto.UserResDto;
import com.lwc.platform.service.UserService;
import com.lwc.platform.utils.DateTimeUtils;

@Service
public class UserServiceImpl implements UserService{
	
	private Gson gson=new Gson();
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public ActionResult addUser(String username, String password) {
		//判断是否为空
		if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return new ActionResult(400,"用户名和密码不能为空",null);
		}
		//先查询用户名是否已存在
		Query query = new Query(Criteria.where("username").is(username));
		User user = mongoTemplate.findOne(query, User.class);
		if(user != null) {
			return new ActionResult(400,"用户名已存在",null);
		}
		//保存到数据库，1表示普通用户，0表示管理员
		user=new User(username, password, 1, new Date());
		mongoTemplate.save(user);
		return new ActionResult(200,"注册成功",null);
	}

	@Override
	public ActionResult addUserToken(String username, String password) {
		//判断是否为空
		if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return new ActionResult(400,"用户名和密码不能为空",null);
		}
		//根据当前用户名和密码查询用户
		Query query = new Query(Criteria.where("username").is(username).and("password").is(password));
		User user = mongoTemplate.findOne(query, User.class);
		if(user == null) {
			return new ActionResult(400,"未找到对应用户",null);
		}
		//生成token并进行返回
		String encode=username+password+System.currentTimeMillis();
		String md5 = DigestUtils.md5DigestAsHex(encode.getBytes(StandardCharsets.UTF_8));
		UserToken userToken=new UserToken(user.getId(), md5, new Date());
		mongoTemplate.save(userToken);
		//返回值
		Map<String,String> map=new HashMap<>();
		map.put("accessToken", md5);
		return new ActionResult(200, "登录成功", map);
	}

	@Override
	public ActionResult getUserInfo(String accessToken) {
		User user = getUserByToken(accessToken);
		if(user == null) {
			return new ActionResult(405,"请先登录",null);
		}
		//返回用户信息
		Map<String,String> map=new HashMap<>();
		map.put("username", user.getUsername());
		return new ActionResult(200,"success",map);
	}

	@Override
	public ActionResult getMenuList(String accessToken) {
		User user = getUserByToken(accessToken);
		if(user == null) {
			return new ActionResult(405,"请先登录",null);
		}
		Query query = new Query(Criteria.where("role_id").is(user.getRoleId()));
		List<RoleMenu> list= mongoTemplate.find(query, RoleMenu.class);
		List<MenuResDto> resultList=convertMenu(list);
		//返回结果
		Map<String,Object> map=new HashMap<>();
		map.put("menuList", resultList);
		return new ActionResult(200,"success",map);
	}
	
	private List<MenuResDto> convertMenu(List<RoleMenu> list) {
		
		Map<String,MenuResDto> mapParam=new HashMap<>();
		
		for(RoleMenu menu : list) {
			if(menu.getMenuId().intValue()==menu.getParentId().intValue()) {
				Map<String, MenuResDto> map = createHashMap(mapParam,menu.getMenuId());
				MenuResDto dto=map.get(menu.getMenuId().toString());
				dto.setMenuName(menu.getMenuName());
				dto.setClassName(menu.getClassName());
			}else {
				Map<String, MenuResDto> map = createHashMap(mapParam,menu.getParentId());
				MenuResDto dto=map.get(menu.getParentId().toString());
				MenuItemDto item=new MenuItemDto(menu.getMenuName(), menu.getMenuUrl());
				dto.getItemList().add(item);
			}
		}
		
		Collection<MenuResDto> collect=mapParam.values();
		List<MenuResDto> menuList=new LinkedList<>(collect);
		
		return menuList;
	}
	
	private Map<String,MenuResDto> createHashMap(Map<String,MenuResDto> map,Integer menuId) {
		String key=menuId.toString();
		MenuResDto dto=map.get(key);
		if(dto==null) {
			map.put(key, new MenuResDto());
		}
		dto=map.get(key);
		if(dto.getItemList()==null) {
			dto.setItemList(new LinkedList<>());
		}
		return map;
	}

	public User getUserByToken(String accessToken) {
		//判断是否为空
		if(StringUtils.isEmpty(accessToken)) {
			return null;
		}
		//查询user_token表
		Query query = new Query(Criteria.where("access_token").is(accessToken));
		UserToken ut = mongoTemplate.findOne(query, UserToken.class);
		if(ut == null) {
			return null;
		}
		//根据用户id查询用户信息
		Query userQuery = new Query(Criteria.where("_id").is(ut.getUserId()));
		return mongoTemplate.findOne(userQuery, User.class);
	}

	@Override
	public ActionResult addMenu(String accessToken, String json) {
		if(StringUtils.isEmpty(json)) {
			return new ActionResult(400, "请求体不能为空", null);
		}
		User user = getUserByToken(accessToken);
		if(user == null) {
			return new ActionResult(405, "请先登录", null);
		}
		MenuReqDto dto = gson.fromJson(json, MenuReqDto.class);
		RoleMenu menu=new RoleMenu(dto.getMenuId(), dto.getParentId(), dto.getMenuName(), dto.getMenuUrl(), dto.getRoleId());
		mongoTemplate.save(menu);
		return new ActionResult(200,"success",null);
	}

	@Override
	public ActionResult findByPage(String token, Integer pageSize, Integer pageNum) {
		//先校验token
		User user = getUserByToken(token);
		if(user == null) {
			return new ActionResult(405,"请先登录",null);
		}
		Query query = new Query();
		// 总条数
		long totalCount = mongoTemplate.count(query, CostDict.class);
		// 数据总页数
		long totalPage = (totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1);
		// 分页查询
		query.skip((pageNum-1) * pageSize).limit(pageSize);
		List<User> rows = mongoTemplate.find(query, User.class);
		List<UserResDto> list = parseList(rows);
		//返回数据
		Map<String,Object> map=new HashMap<>();
		map.put("totalCount", totalCount);
		map.put("totalPage", totalPage);
		map.put("rows", list);
		return new ActionResult(0,"success",map);
	}
	
	private List<UserResDto> parseList(List<User> rows) {
		List<UserResDto> list=new LinkedList<>();
		for(User user : rows) {
			UserResDto dto=new UserResDto(user.getId(), user.getUsername(), user.getPassword(), 
					user.getRoleId().intValue()==1 ? "普通用户" : "管理员", DateTimeUtils.parseDateToString(user.getCreateTime(), "yyyy-MM-dd"));
			list.add(dto);
		}
		return list;
	}
}
