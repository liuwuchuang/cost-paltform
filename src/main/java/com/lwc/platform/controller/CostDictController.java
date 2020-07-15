package com.lwc.platform.controller;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lwc.platform.common.ActionResult;
import com.lwc.platform.service.CostDictService;

@CrossOrigin
@RestController
@RequestMapping(value="/dict")
public class CostDictController {
	
	@Autowired
	private CostDictService costDictService;
	
	@PostMapping("/add")
	public ActionResult add(@RequestHeader(value="access_token") String accessToken,
			@RequestBody String json) {
		return costDictService.add(accessToken, json);
	}
	
	@DeleteMapping("/delete/{id}")
	public ActionResult delete(@RequestHeader(value="access_token") String accessToken,
			@PathVariable("id") String id) {
		return costDictService.delete(accessToken, id);
	}
	
	@GetMapping("/page")
	public ActionResult findByPage(@RequestHeader(value="access_token") String accessToken,
			@PathParam(value="limit") Integer limit,@PathParam(value="pageNum") Integer pageNum) {
		return costDictService.findByPage(accessToken, limit, pageNum);
	}
	
	@GetMapping("/list")
	public ActionResult findList(@RequestHeader(value="access_token") String accessToken) {
		return costDictService.findList(accessToken);
	}
}
