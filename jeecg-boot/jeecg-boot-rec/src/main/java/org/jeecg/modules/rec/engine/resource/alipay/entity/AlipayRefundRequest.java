package org.jeecg.modules.rec.engine.resource.alipay.entity;

import lombok.Data;

/**
 * 退款参数
 */
@Data
public class AlipayRefundRequest {
    /**
     * 支付商户id(多商户)
     */
    private String merchantId;
    /**
     * 退款批次号
     */
    private String refundBatchNo;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 商户订单号
     */
    private String tradeNo;
    /**
     * 退款金额
     */
    private float refundPrice;
    /**
     * 订单金额
     */
    private float price;
    /**
     * 备注
     */
    private String intro;
}
