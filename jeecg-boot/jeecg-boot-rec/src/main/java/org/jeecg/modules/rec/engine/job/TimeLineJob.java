package org.jeecg.modules.rec.engine.job;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.utils.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.SpringContextHolder;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.rec.engine.config.RecConfig;
import org.jeecg.modules.rec.entity.ModRec;
import org.jeecg.modules.rec.service.IModRecService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * 时间线任务
 *
 * @Author: zhou x
 * @Date: 2022/2/15 19:00
 */
@DisallowConcurrentExecution
@Slf4j
public abstract class TimeLineJob<T extends TimeLineJobWork> implements Job {

    protected String parameter;
    protected T worker;

    public void setParameter(String parameter) {
        this.parameter = parameter;
        Type type = getClass().getGenericSuperclass();
        Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];
        worker = SpringContextHolder.getBean((Class<T>) trueType);
    }

    @Autowired
    protected RecConfig recConfig;
    @Autowired
    protected IModRecService modRecService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Date startDate = worker.startDate();
        if (startDate == null) throw new JobExecutionException("执行开始时间未配置");
        startDate = DateUtils.addDays(startDate, -1);
        ModRec modRec = modRecService.getOne(Wrappers.<ModRec>lambdaQuery()
                .eq(ModRec::getName, parameter)
                .eq(ModRec::getType, worker.thresholdName()));
        if (modRec == null) {
            modRec = new ModRec();
            modRec.setName(parameter);
            modRec.setType(worker.thresholdName());
            modRec.setContent(new JSONObject());
        }
        String recDate = modRec.getContent().getString("excDate");
        if (StringUtils.isNotEmpty(recDate)) {
            Date date = DateUtils.parseDateDef(recDate, "yyyy-MM-dd");
            if (date.after(startDate)) startDate = date;
        }
        while (true) {
            startDate = DateUtils.addDays(startDate, 1);
            if (DateUtils.isSameDay(new Date(), startDate)) break;
            worker.work(startDate, modRec, parameter);
        }
    }
}
