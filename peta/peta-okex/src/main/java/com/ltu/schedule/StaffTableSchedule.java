package com.ltu.schedule;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ltu.domain.mp_entity.ContractMintEntity;
import com.ltu.service.ContractMintService;
import com.ltu.service.ContractService;
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


    @Autowired
    ContractService contractService;
    @Autowired
    ContractMintService contractMintService;

	/**
	 * 更新设备在线状态
	 */
	@Scheduled(cron = "0 0/3 * * * ?")
	public void bacthMint() {

		System.out.println("定时任务铸造");
        List<ContractMintEntity> mintEntityList = contractMintService.getUnfinshMint();
        if (mintEntityList.size()>0){
           try {
               contractService.mint(mintEntityList.get(0));
           }catch (Exception e){
           }
        }
	}
}
