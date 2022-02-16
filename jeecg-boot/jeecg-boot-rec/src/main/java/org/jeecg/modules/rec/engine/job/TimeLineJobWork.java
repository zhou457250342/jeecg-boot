package org.jeecg.modules.rec.engine.job;

import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.rec.engine.config.RecConfig;
import org.jeecg.modules.rec.entity.ModRec;
import org.jeecg.modules.rec.service.IModRecModelService;
import org.jeecg.modules.rec.service.IModRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 时间线执行
 *
 * @Author: zhou x
 * @Date: 2022/2/15 19:25
 */
@Component
public abstract class TimeLineJobWork {

    @Autowired
    protected IModRecModelService modRecModelService;
    @Autowired
    protected IModRecService modRecService;
    @Autowired
    protected RecConfig recConfig;


    @Transactional(rollbackFor = Exception.class)
    public void work(Date syncDate, ModRec modRec, String name) {
        execute(syncDate, modRec, name);
        down(syncDate, modRec);
    }


    protected void down(Date wDate, ModRec modRec) {
        String date = DateUtils.formatDate(wDate, "yyyy-MM-dd");
        modRec.getContent().put("excDate", date);
        modRec.setUptime(new Date());
        modRecService.saveOrUpdate(modRec);
    }

    protected abstract void execute(Date syncDate, ModRec modRec, String name);

    public abstract Date startDate();

    public abstract String thresholdName();
}
