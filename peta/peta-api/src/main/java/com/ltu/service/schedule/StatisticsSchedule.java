package com.ltu.service.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 数据统计定时器
 *  CRON规则示例：
 * 	0 0 3 * * ?     每天3点执行
 * 	0 5 3 * * ?     每天3点5分执行
 * 	0 5 3 ? * *     每天3点5分执行，与上面作用相同
 * 	0 5/10 3 * * ?  每天3点的 5分，15分，25分，35分，45分，55分这几个时间点执行
 *  0 10 3 ? * 1    每周星期天，3```````````````````````个月的第三个星期，星期天 执行，#号只能出现在星期的位置
 */

@Slf4j
@Component
public class StatisticsSchedule {

}
