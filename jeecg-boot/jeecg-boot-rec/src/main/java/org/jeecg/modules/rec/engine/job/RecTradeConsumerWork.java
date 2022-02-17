package org.jeecg.modules.rec.engine.job;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.rec.engine.checker.CheckResult;
import org.jeecg.modules.rec.engine.checker.CheckStatus;
import org.jeecg.modules.rec.engine.checker.CheckType;
import org.jeecg.modules.rec.engine.model.RecException;
import org.jeecg.modules.rec.engine.model.RecParameter;
import org.jeecg.modules.rec.engine.service.RecService;
import org.jeecg.modules.rec.entity.ModRec;
import org.jeecg.modules.rec.entity.ModRecComparison;
import org.jeecg.modules.rec.service.IModRecComparisonService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 交易追踪对账
 *
 * @Author: zhou x
 * @Date: 2022/2/16 11:14
 */
@Component
public class RecTradeConsumerWork {

    @Transactional(rollbackFor = Exception.class)
    public void work(ModRecComparison modRecComparison, RecService recService, RecParameter parameter) {
        CheckResult result = recService.recTrade(modRecComparison, parameter);
        if (result == null || result.getStatus() == CheckStatus.success)
            recService.removeById(modRecComparison.getTradeNo());
        modRecComparison.setResult(JSONObject.toJSONString(result));
        modRecComparison.setStatus(1);
        recService.saveRecResult(modRecComparison);
    }
}
