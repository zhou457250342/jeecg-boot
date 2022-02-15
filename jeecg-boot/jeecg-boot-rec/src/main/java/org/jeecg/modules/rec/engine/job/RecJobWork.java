package org.jeecg.modules.rec.engine.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.rec.engine.model.RecException;
import org.jeecg.modules.rec.engine.service.RecService;
import org.jeecg.modules.rec.entity.ModRec;
import org.jeecg.modules.rec.entity.ModRecModel;
import org.jeecg.modules.rec.service.IModRecModelService;
import org.jeecg.modules.rec.service.IModRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author: zhou x
 * @Date: 2022/2/15 15:21
 */
@Component
public class RecJobWork {

    /**
     * 对账
     *
     * @param syncDate
     * @throws RecException
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncData(Date syncDate, String mainName, String sideName) throws RecException {
        new RecService(mainName, sideName).execute(syncDate);
    }
}
