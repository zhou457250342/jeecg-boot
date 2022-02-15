package org.jeecg.modules.rec.engine.job;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.utils.StringUtils;
import net.bytebuddy.asm.Advice;
import org.jeecg.common.util.DateUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.rec.engine.config.RecConfig;
import org.jeecg.modules.rec.entity.ModRec;
import org.jeecg.modules.rec.service.IModRecService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: zhou x
 * @Date: 2022/2/15 10:54
 */
@Slf4j
@DisallowConcurrentExecution
public class RecJob implements Job {

    private String parameter;
    private String mainName;
    private String sideName;

    public void setParameter(String parameter) {
        this.parameter = parameter;
        String[] split = parameter.split("_");
        this.mainName = split[0];
        this.sideName = split[1];
    }

    @Autowired
    private RecConfig recConfig;
    @Autowired
    private IModRecService modRecService;
    @Autowired
    private RecJobWork recJobWork;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Date recStartDate = recConfig.recBillDownDate();
        if (recStartDate == null) throw new JobExecutionException("对账开始时间未配置");
        recStartDate = DateUtils.addDays(recStartDate, -1);
        ModRec modRec = modRecService.getOne(Wrappers.<ModRec>lambdaQuery().eq(ModRec::getName, parameter));
        if (modRec == null) {
            modRec = new ModRec();
            modRec.setName(parameter);
            modRec.setContent(new JSONObject());
        }
        String recDate = modRec.getContent().getString("recDate");
        if (StringUtils.isNotEmpty(recDate)) {
            Date date = DateUtils.parseDateDef(recDate, "yyyy-MM-dd");
            if (date.after(recStartDate)) recStartDate = date;
        }
        while (true) {
            recStartDate = DateUtils.addDays(recStartDate, 1);
            if (DateUtils.isSameDay(new Date(), recStartDate)) break;
            recJobWork.syncData(recStartDate, mainName, sideName);
        }
    }
}
