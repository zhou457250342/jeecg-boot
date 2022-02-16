package org.jeecg.modules.rec.engine.job;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.rec.engine.model.RecException;
import org.jeecg.modules.rec.engine.service.RecService;
import org.jeecg.modules.rec.entity.ModRec;
import org.jeecg.modules.rec.entity.ModRecComparison;
import org.jeecg.modules.rec.entity.ModRecModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 交易追踪对账
 *
 * @Author: zhou x
 * @Date: 2022/2/16 11:15
 */
@Component
public class RecTradeProducerWork extends TimeLineJobWork {

    /**
     * @param syncDate
     * @param modRec
     * @param name     alipay|zlhis_apliay
     */
    @Override
    protected void execute(Date syncDate, ModRec modRec, String name) {
        String[] split = name.split("\\|");
        String[] splitH = split[1].split("_");
        RecService recService = new RecService(splitH[0], splitH[1]);
        Date billDownDate = recService.billingDownExcDate(split[0]);
        if (billDownDate == null || syncDate.after(billDownDate)) throw new RecException("账单未下载，无法处理");
        List<ModRecModel> models = recService.searchBillList(split[0], syncDate, null);
        if (CollectionUtils.isEmpty(models)) return;
        List<ModRecComparison> comList = new ArrayList<>();
        models.forEach(op -> {
            ModRecComparison com = new ModRecComparison();
            com.setTradeNo(op.getTradeNo());
            com.setCreateTime(op.getCreateTime());
            com.setStatus(0);
            comList.add(com);
        });
        recService.saveRecResult(comList);
    }

    @Override
    public Date startDate() {
        return recConfig.recStartDate();
    }

    @Override
    public String thresholdName() {
        return "recTradeProducer";
    }
}
