package org.jeecg.modules.rec.engine.job;

import org.jeecg.modules.rec.engine.model.RecException;
import org.jeecg.modules.rec.engine.service.RecService;
import org.jeecg.modules.rec.entity.ModRec;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 系统对账-天
 *
 * @Author: zhou x
 * @Date: 2022/2/15 15:21
 */
@Component
public class RecDayTimeJobWork extends TimeLineJobWork {

    @Override
    protected void execute(Date syncDate, ModRec modRec, String name) {
        String[] split = name.split("_");
        new RecService(split[0], split[1]).recDay(syncDate);
    }

    @Override
    public Date startDate() {
        return recConfig.recStartDate();
    }

    @Override
    public String thresholdName() {
        return "recDay";
    }
}
