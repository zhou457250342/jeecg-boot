package org.jeecg.rec.engine.config;

import org.jeecg.common.util.DateUtils;
import org.jeecg.service.IAppDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 配置
 *
 * @Author: zhou x
 * @Date: 2022/2/9 16:44
 */
@Component
public class RecConfig {

    @Autowired
    private IAppDictItemService appDictItemService;

    /**
     * 对账开始时间
     *
     * @return
     */
    public Date recStartDate() {
        try {
            return DateUtils.parseDate(appDictItemService.getDicItemValue("1491329897692807170"), "yyyy-MM-dd");
        } catch (Exception ex) {
            return null;
        }
    }
}
