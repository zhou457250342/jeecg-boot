package org.jeecg.modules.rec.engine.resource;

import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.rec.engine.model.RecException;
import org.jeecg.modules.rec.engine.model.TradeData;
import org.jeecg.modules.rec.engine.resource.alipay.AlipayService;
import org.jeecg.modules.rec.engine.resource.zlhis.entity.ZlHisTransData;
import org.jeecg.modules.rec.engine.resource.zlhis.enums.BussServiceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: zhou x
 * @Date: 2022/1/22 15:38
 */
@Service
public class AlipayResourceLoader implements ResourceLoader {
    @Autowired
    private AlipayService alipayService;

    @Override
    public List<TradeData> getList(Date date) {
        if (!DateUtils.isAfterT1Hours(date, 10)) throw new RecException("账单还未出");
        try {
            return alipayService.tradeQuery(date);
        } catch (Exception e) {
            throw new RecException("alipay账单获取失败", e);
        }
    }
}
