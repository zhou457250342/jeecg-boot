package org.jeecg.rec.engine.model;

import lombok.Data;

import java.util.List;

/**
 * @Author: zhou x
 * @Date: 2022/2/7 16:20
 */
@Data
public class TradeDataRec extends TradeData {
    private List<TradeData> infos;
    private boolean main;
}
