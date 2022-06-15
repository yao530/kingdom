package com.ltu;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;





/**
 *@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes= EntryApplication.class)
public class BaseApplicationTest {

	@Autowired
	public RedisTemplate redisTemplate;

	/*
	 * 腾讯云Cos（文件存储服务器私有访问）
	 */
	@Test
	public void aa() {
		String keyZ = "zset";
		redisTemplate.opsForZSet().add(keyZ, "zset测试1", 1);
		redisTemplate.opsForZSet().add(keyZ, "zset测试2", 3);
		redisTemplate.opsForZSet().add(keyZ, "zset测试3", 2);



		Long count = redisTemplate.opsForZSet().count(keyZ, 1, 2);
		System.out.println("count 获取指定区间总数 {} "+ count+"\n");

		Long zsize = redisTemplate.opsForZSet().size(keyZ);
		System.out.println("size 获取集合元素总数 {} "+zsize);

		Set<ZSetOperations.TypedTuple<String>> rangeWithScores = redisTemplate.opsForZSet().rangeWithScores(keyZ,0,-1);
		System.out.println("获取到的排行和分数列表:" + JSON.toJSONString(rangeWithScores));


		Double incrementScore = redisTemplate.opsForZSet().incrementScore(keyZ, "zset测试3", 2);
		System.out.println("incrementScore 给指定key的值加score分数 {}"+incrementScore);

		Double score = redisTemplate.opsForZSet().score(keyZ, "zset测试3");
		System.out.println("score 给指定key的值分数 {}"+ score);

		Long rank = redisTemplate.opsForZSet().rank(keyZ, "zset测试3");
		System.out.println("rank 返回指定成员的排名（从小到大） {}"+rank);
		Long reverseRank = redisTemplate.opsForZSet().reverseRank(keyZ, "zset测试3");
		System.out.println("reverseRank 返回指定成员的排名（从大到小） {}"+reverseRank);

	}
		

//	    @Test
	    public void testSendMessage(){
//	        try {
//	            //短信模板中的参数列表
//	            String[] params = {"3254","5"};
//	            SmsSingleSender sender = new SmsSingleSender(1400590027, "c5e63fc534900b7ece74808a3aa0baaf");
//	            SmsSingleSenderResult result = sender.sendWithParam("86", "15011678982",
//	            		1175306, params, "亨泽", "", "");
//	            System.out.println(com.alibaba.fastjson.JSONObject.toJSONString(result));
//	        } catch (HTTPException e) {
//	            // HTTP 响应码错误
//	            e.printStackTrace();
//	        } catch (JSONException e) {
//	            // JSON 解析错误
//	            e.printStackTrace();
//	        } catch (IOException e) {
//	            // 网络 IO 错误
//	            e.printStackTrace();
//	        }
	    }
	
	
	
}
