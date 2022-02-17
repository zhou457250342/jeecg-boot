package org.jeecg.modules.rec.engine.resource;

import org.jeecg.modules.rec.engine.model.TradeData;

import java.util.Date;
import java.util.List;

/**
 * 元数据加载
 *
 * @Author: zhou x
 * @Date: 2022/1/22 15:29
 */
public interface ResourceLoader {
    /**
     * 获取账单
     *
     * @param date
     * @return
     */
    List<TradeData> getList(Date date);

    /**
     * 验证交易真实性
     *
     * @param data
     * @return
     */
    boolean validateTradeUnKnown(TradeData data);
}
