package com.ltu.schedule;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(99)
public class StaffTableSchedule {


	/**
	 * 更新设备在线状态
	 */
//	@Scheduled(cron = "0 */10 * * * ?")
	public void upDeviceOnLineStatus() {
		
		
		
	}


}
