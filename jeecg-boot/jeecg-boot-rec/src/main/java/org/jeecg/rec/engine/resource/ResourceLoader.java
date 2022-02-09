package org.jeecg.rec.engine.resource;

import org.jeecg.rec.engine.model.TradeData;

import java.util.Date;
import java.util.List;

/**
 * 元数据加载
 *
 * @Author: zhou x
 * @Date: 2022/1/22 15:29
 */
public interface ResourceLoader {
    List<TradeData> getList(Date date);
}
