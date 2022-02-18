package org.jeecg.modules.rec.engine.job;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.annotation.Obsolete;
import org.jeecg.common.util.SpringContextHolder;
import org.jeecg.modules.rec.engine.async.AsyncService;
import org.jeecg.modules.rec.engine.config.RecConfig;
import org.jeecg.modules.rec.engine.model.RecException;
import org.jeecg.modules.rec.engine.model.RecExecType;
import org.jeecg.modules.rec.engine.model.RecParameter;
import org.jeecg.modules.rec.engine.service.RecService;
import org.jeecg.modules.rec.entity.ModRecComparison;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 交易追踪对账，后续调整为消息队列处理
 *
 * @Author: zhou x
 * @Date: 2022/2/16 11:14
 */
@DisallowConcurrentExecution
@Slf4j
@Obsolete
public class RecTradeConsumerJob implements Job {

    protected String parameter;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Autowired
    private AsyncService asyncService;
    @Autowired
    private RecConfig recConfig;
    @Autowired
    private RecTradeConsumerWork recTradeConsumerWork;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        RecService recService = new RecService(parameter);
        if (recService.validateCanRec()) throw new RecException("目前不可进行系统对账");
        RecParameter parameter = new RecParameter();
        parameter.setDownDate(recService.excDate(recService.mainName, RecExecType.billingDown));
        parameter.setStartDate(recConfig.recBillDownDate());
        if (parameter.getDownDate() == null || parameter.getStartDate() == null)
            throw new RecException("对账参数异常，无法进行系统对账");
        List<ModRecComparison> list = recService.searchRecResult(0, 1000);
        if (CollectionUtils.isEmpty(list)) return;
        try {
            asyncService.asyncWorkBatch(list, op -> {
                op.forEach(it -> recTradeConsumerWork.work(it, recService, parameter));
            });
        } catch (Exception ex) {
            throw new RecException("系统追踪对账异常", ex);
        }
    }
}
