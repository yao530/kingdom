package com.ltu.service.schedule;

import com.ltu.enums.SchduleEnums.SchduleKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ltu.util.redis.RedistUtil;

import lombok.extern.slf4j.Slf4j;

/**  
 * @Description: 订单回滚从redis获取到的key拼接规则：产品类型_出售类型_订单Id example:CardOrder_1_232,表示卡项订单、常规出售、订单id为232。
 * @author: 若尘  
 * @date 2019年7月7日 下午2:09:23
 * @version V1.0  
 */
@Slf4j
@Component
public class RollBackRedisSchedule {

	@Autowired
	RedistUtil redistUtil;

	public Boolean getRollBackThing(SchduleKey rollBackKey,String id){
		String orderKey= getRollThingKey(rollBackKey,id);
		return redistUtil.hasKey(orderKey);
	}

	/**
	 * @date 2019年7月13日
	 * @author 若尘
	 * @Description 需要加入到redis队列监听key过期的方法
	 * @param rollBackKey
	 * @param id
	 * @param expireTime 过期时间 s 单位
	 * void
	 */
	public  void setOrderKey(SchduleKey rollBackKey,String id,Long expireTime){

		String orderKey= getRollThingKey(rollBackKey,id);
		if(!redistUtil.hasKey(orderKey)){
			redistUtil.setValue(orderKey, 1, expireTime);
		}
	}
	
	public void removerOrderKey(SchduleKey rollBackKey,String id){
		String orderKey= getRollThingKey(rollBackKey,id);
		if (redistUtil.hasKey(orderKey))
			redistUtil.removeKey(orderKey);
	}
	

	public String getRollThingKey(SchduleKey rollBackKey,String id){
		StringBuilder  key=new StringBuilder("RollBackKey");
		key.append(':');
		key.append(rollBackKey.getName());
		key.append(':');
		key.append(id);
		String orderKey= key.toString();

		return orderKey;
	}

	
	
}
