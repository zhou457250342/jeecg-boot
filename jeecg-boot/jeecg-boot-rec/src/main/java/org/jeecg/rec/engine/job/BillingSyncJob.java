package org.jeecg.rec.engine.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.rec.engine.config.RecConfig;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 账单同步任务
 *
 * @Author: zhou x
 * @Date: 2022/2/9 15:34
 */
@Slf4j
public class BillingSyncJob implements Job {

    private String parameter;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Autowired
    private RecConfig recConfig;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        throw new JobExecutionException("测试异常");
//        String beanName = "mod.rec.engine.resource." + parameter + "ResourceLoader";
//        ResourceLoader resourceLoader = SpringContextHolder.getBean(beanName);
//        if (resourceLoader == null) throw new JobExecutionException("ResourceLoader is null");
//        Date recStartDate = recConfig.recStartDate();
//        if (recStartDate == null) throw new JobExecutionException("对账开始时间未配置");
    }
}
