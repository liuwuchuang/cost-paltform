package com.lwc.platform.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.lwc.platform.bean.CostRecord;
import com.lwc.platform.bean.User;
import com.lwc.platform.cache.DictCache;
import com.lwc.platform.common.ActionResult;
import com.lwc.platform.dto.AnalyDbDto;
import com.lwc.platform.dto.AnalyResDto;
import com.lwc.platform.dto.RecordReqDto;
import com.lwc.platform.dto.RecordResDto;
import com.lwc.platform.service.CostRecordService;
import com.lwc.platform.service.UserService;
import com.lwc.platform.utils.DateTimeUtils;

@Service
public class CostRecordServiceImpl implements CostRecordService{

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
		RecordReqDto record=null;
		try {
			record = gson.fromJson(json, RecordReqDto.class);
		} catch (Exception e) {
			return new ActionResult(400,"请输入合法数据",null);
		}
		Date createTime=DateTimeUtils.parseStringToDate(record.getCreateTime(), "yyyy-MM-dd");
		if(createTime==null || record.getMoney()==null || record.getType()==null || 
				StringUtils.isEmpty(record.getDictId()) || StringUtils.isEmpty(record.getComments())) {
			return new ActionResult(400,"信息不能为空",null);
		}
		CostRecord cr=new CostRecord(user.getId(), createTime, record.getMoney(), record.getDictId(),record.getType(), record.getComments());
		mongoTemplate.save(cr);
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
		mongoTemplate.remove(query, CostRecord.class);
		return new ActionResult(200,"success",null);
	}

	@Override
	public ActionResult findByPage(String token, Integer pageSize, Integer pageNum, String start, String end) {
		//先校验token
		User user = userService.getUserByToken(token);
		if(user == null) {
			return new ActionResult(405,"请先登录",null);
		}
		Criteria c=Criteria.where("user_id").is(user.getId());
		if(!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)) {
			String startTime=start+" 00:00:00";
			String endTime=end+" 23:59:59";
			c.andOperator(Criteria.where("create_time").gte(DateTimeUtils.parseStringToDate(startTime, "yyyy-MM-dd HH:mm:ss")),
					Criteria.where("create_time").lte(DateTimeUtils.parseStringToDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}else if(!StringUtils.isEmpty(start)) {
			String startTime=start+" 00:00:00";
			c.andOperator(Criteria.where("create_time").gte(DateTimeUtils.parseStringToDate(startTime, "yyyy-MM-dd HH:mm:ss")));
		}else if(!StringUtils.isEmpty(end)) {
			String endTime=end+" 23:59:59";
			c.andOperator(Criteria.where("create_time").lte(DateTimeUtils.parseStringToDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}
		Query query = Query.query(c);
		long totalCount = mongoTemplate.count(query, CostRecord.class);
		// 分页查询
		query.with(Sort.by(Sort.Order.desc("create_time")));
		query.skip((pageNum-1) * pageSize).limit(pageSize);
		List<CostRecord> rows = mongoTemplate.find(query, CostRecord.class);
		List<RecordResDto> list = parseToRes(rows);
		//返回数据
		Map<String,Object> map=new HashMap<>();
		map.put("totalCount", totalCount);
		map.put("rows", list);
		return new ActionResult(0,"success",map);
	}

	private List<RecordResDto> parseToRes(List<CostRecord> rows) {
		List<RecordResDto> list=new LinkedList<>();
		for(CostRecord cr : rows) {
			String dateStr = DateTimeUtils.parseDateToString(cr.getCreateTime(), "yyyy-MM-dd");
			String type=cr.getType().intValue()==-1 ? "支出" : "收入";
			RecordResDto dto=new RecordResDto(cr.getId(), dateStr, cr.getMoney(), dictCache.dictMap.get(cr.getDictId()), type,cr.getComments());
			list.add(dto);
		}
		return list;
	}

	@Override
	public ActionResult countByDict(String token, String start, String end) {
		//先校验token
		User user = userService.getUserByToken(token);
		if(user == null) {
			return new ActionResult(405,"请先登录",null);
		}
		Criteria c=Criteria.where("user_id").is(user.getId());
		if(!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)) {
			String startTime=start+" 00:00:00";
			String endTime=end+" 23:59:59";
			c.andOperator(Criteria.where("create_time").gte(DateTimeUtils.parseStringToDate(startTime, "yyyy-MM-dd HH:mm:ss")),
					Criteria.where("create_time").lte(DateTimeUtils.parseStringToDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}else if(!StringUtils.isEmpty(start)) {
			String startTime=start+" 00:00:00";
			c.andOperator(Criteria.where("create_time").gte(DateTimeUtils.parseStringToDate(startTime, "yyyy-MM-dd HH:mm:ss")));
		}else if(!StringUtils.isEmpty(end)) {
			String endTime=end+" 23:59:59";
			c.andOperator(Criteria.where("create_time").lte(DateTimeUtils.parseStringToDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}
		Aggregation agg = Aggregation.newAggregation(
				Aggregation.match(c),
			    Aggregation.group("dict_id").sum("money").as("sum"));
		AggregationResults<AnalyDbDto> results = mongoTemplate.aggregate(agg, "cost_record", AnalyDbDto.class);
		List<AnalyDbDto> list = results.getMappedResults();
		List<AnalyResDto> dtoList=parseDictList(list);
		//返回数据
		Map<String,Object> map=new HashMap<>();
		map.put("datas", dtoList);
		return new ActionResult(200,"success",map);
	}
	
	private List<AnalyResDto> parseDictList(List<AnalyDbDto> list) {
		List<AnalyResDto> dtoList=new LinkedList<>();
		if(list.isEmpty()) {
			dtoList.add(new AnalyResDto("无",0.0));
			return dtoList;
		}
		for(AnalyDbDto db : list) {
			AnalyResDto dto=new AnalyResDto(dictCache.dictMap.get(db.getId()), db.getSum());
			dtoList.add(dto);
		}
		return dtoList;
	}

	@Override
	public ActionResult countByType(String token, String start, String end) {
		//先校验token
		User user = userService.getUserByToken(token);
		if(user == null) {
			return new ActionResult(405,"请先登录",null);
		}
		Criteria c=Criteria.where("user_id").is(user.getId());
		if(!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)) {
			String startTime=start+" 00:00:00";
			String endTime=end+" 23:59:59";
			c.andOperator(Criteria.where("create_time").gte(DateTimeUtils.parseStringToDate(startTime, "yyyy-MM-dd HH:mm:ss")),
					Criteria.where("create_time").lte(DateTimeUtils.parseStringToDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}else if(!StringUtils.isEmpty(start)) {
			String startTime=start+" 00:00:00";
			c.andOperator(Criteria.where("create_time").gte(DateTimeUtils.parseStringToDate(startTime, "yyyy-MM-dd HH:mm:ss")));
		}else if(!StringUtils.isEmpty(end)) {
			String endTime=end+" 23:59:59";
			c.andOperator(Criteria.where("create_time").lte(DateTimeUtils.parseStringToDate(endTime, "yyyy-MM-dd HH:mm:ss")));
		}
		Aggregation agg = Aggregation.newAggregation(
				Aggregation.match(c),
			    Aggregation.group("type").sum("money").as("sum"));
		AggregationResults<AnalyDbDto> results = mongoTemplate.aggregate(agg, "cost_record", AnalyDbDto.class);
		List<AnalyDbDto> list = results.getMappedResults();
		List<AnalyResDto> dtoList=parseTypeList(list);
		//返回数据
		Map<String,Object> map=new HashMap<>();
		map.put("datas", dtoList);
		return new ActionResult(200,"success",map);
	}
	
	private List<AnalyResDto> parseTypeList(List<AnalyDbDto> list) {
		List<AnalyResDto> dtoList=new LinkedList<>();
		if(list.isEmpty()) {
			dtoList.add(new AnalyResDto("无",0.0));
			return dtoList;
		}
		for(AnalyDbDto db : list) {
			String name=db.getId().equals("1") ? "收入" : "支出";
			AnalyResDto dto=new AnalyResDto(name , db.getSum());
			dtoList.add(dto);
		}
		return dtoList;
	}
}
