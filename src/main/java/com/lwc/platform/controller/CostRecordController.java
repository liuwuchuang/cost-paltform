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
import com.lwc.platform.service.CostRecordService;

@CrossOrigin
@RestController
@RequestMapping(value="/cost")
public class CostRecordController {

	@Autowired
	private CostRecordService costRecordService;
	
	@PostMapping("/add")
	public ActionResult add(@RequestHeader(value="access_token") String accessToken,
			@RequestBody String json) {
		return costRecordService.add(accessToken, json);
	}
	
	@DeleteMapping("/delete/{id}")
	public ActionResult delete(@RequestHeader(value="access_token") String accessToken,
			@PathVariable("id") String id) {
		return costRecordService.delete(accessToken, id);
	}
	
	@GetMapping("/page")
	public ActionResult findByPage(@RequestHeader(value="access_token") String accessToken,
			@PathParam(value="limit") Integer limit,@PathParam(value="pageNum") Integer pageNum,
			@PathParam(value="start") String start,@PathParam(value="end") String end) {
		return costRecordService.findByPage(accessToken, limit, pageNum, start, end);
	}
	
	@GetMapping("/analyDict")
	public ActionResult countByDict(@RequestHeader(value="access_token") String accessToken,
			@PathParam(value="start") String start,@PathParam(value="end") String end) {
		return costRecordService.countByDict(accessToken, start, end);
	}
	
	@GetMapping("/analyType")
	public ActionResult countByType(@RequestHeader(value="access_token") String accessToken,
			@PathParam(value="start") String start,@PathParam(value="end") String end) {
		return costRecordService.countByType(accessToken, start, end);
	}
}
