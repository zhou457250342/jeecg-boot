package org.jeecg.rec.engine.checker;

import org.jeecg.rec.engine.model.TradeData;
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
    private double amountSide;
    private double amountMain;
    private CheckType type;
    private final List<TradeData> mainInfo = new ArrayList<>();
    private final List<TradeData> sideInfo = new ArrayList<>();
}