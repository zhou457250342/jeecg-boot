package org.jeecg.rec.engine.resource;

import org.jeecg.rec.engine.model.TradeData;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: zhou x
 * @Date: 2022/1/22 15:38
 */
@Service
public class AlipayResourceLoader implements ResourceLoader {

    @Override
    public List<TradeData> getList(Date date) {
        return null;
    }
}
