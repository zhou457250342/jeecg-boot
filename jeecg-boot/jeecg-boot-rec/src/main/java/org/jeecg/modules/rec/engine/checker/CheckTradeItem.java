package org.jeecg.modules.rec.engine.checker;

import lombok.Data;

/**
 * 单条交易比对结果
 *
 * @Author: zhou x
 * @Date: 2022/2/17 11:07
 */
@Data
public class CheckTradeItem {
    private float amountSide;
    private float amountMain;
    private String opName;
    private CheckTradeOperation operation;
}
