package com.lwc.platform.controller;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lwc.platform.common.ActionResult;
import com.lwc.platform.service.UserService;

@CrossOrigin
@RestController
@RequestMapping(value="/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	/**
	 * 用户注册
	 * @param username
	 * @param password
	 * @return
	 */
	@PostMapping(value="/register")
	public ActionResult register(@RequestParam(value="username")String username,
			@RequestParam(value="password")String password) {
		return userService.addUser(username, password);
	}
	
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @return
	 */
	@PostMapping(value="/login")
	public ActionResult login(@RequestParam(value="username")String username,
			@RequestParam(value="password")String password) {
		return userService.addUserToken(username, password);
	}
	
	/**
	 * 获取用户信息
	 * @param accessToken
	 * @return
	 */
	@GetMapping(value="/info")
	public ActionResult getUserInfo(@RequestHeader(value="access_token") String accessToken) {
		return userService.getUserInfo(accessToken);
	}
	
	/**
	 * 添加菜单
	 * @param accessToken
	 * @param json
	 * @return
	 */
	@PostMapping(value="/menu")
	public ActionResult addMenu(@RequestHeader(value="access_token") String accessToken,
			@RequestBody String json) {
		return userService.addMenu(accessToken,json);
	}
	
	/**
	 * 获取菜单列表
	 * @param accessToken
	 * @return
	 */
	@GetMapping(value="/menu")
	public ActionResult getMenuList(@RequestHeader(value="access_token") String accessToken) {
		return userService.getMenuList(accessToken);
	}
	
	@GetMapping("/page")
	public ActionResult findByPage(@RequestHeader(value="access_token") String accessToken,
			@PathParam(value="limit") Integer limit,@PathParam(value="pageNum") Integer pageNum) {
		return userService.findByPage(accessToken, limit, pageNum);
	}
}
