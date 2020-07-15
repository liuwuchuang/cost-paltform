package com.lwc.platform.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.lwc.platform.bean.CostDict;

@Component
@Order(1)
public class DictCache implements CommandLineRunner{
	
	public Map<String,String> dictMap=new HashMap<>(64);
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void run(String... args) throws Exception {
		List<CostDict> list = mongoTemplate.findAll(CostDict.class);
		for(CostDict cd : list) {
			dictMap.put(cd.getId(), cd.getName());
		}
	}
}
