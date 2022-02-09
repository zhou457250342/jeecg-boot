package org.jeecg.rec.engine.model;

import lombok.Data;

import java.util.Date;

/**
 * 交易数据
 *
 * @Author: zhou x
 * @Date: 2022/1/22 15:33
 */
@Data
public class TradeData {
    /**
     * 交易流水号
     */
    private String tradeNo;
    /**
     * 商户订单号
     */
    private String orderNo;
    /**
     * 业务流水号
     */
    private String operationNo;
    private float amount;
    private Date createTime;
    private String note;
    private boolean main;
}
