package org.jeecg.modules.rec.engine.checker;

import org.jeecg.modules.rec.engine.model.TradeData;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 校验结果
 *
 * @Author: zhou x
 * @Date: 2022/1/22 16:10
 */
@Data
public class CheckResult {
    private String tradeNo;
    private String orderNo;
    private Date createTime;
    private String note;
    private float amountSide;
    private float amountMain;
    private CheckStatus status;
    private CheckType type;
    private final List<TradeData> mainInfo = new ArrayList<>();
    private final List<TradeData> sideInfo = new ArrayList<>();
    private final List<CheckTradeItem> checkItems = new ArrayList<>();
}