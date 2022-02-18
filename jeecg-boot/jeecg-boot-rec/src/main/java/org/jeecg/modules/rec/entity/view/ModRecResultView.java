package org.jeecg.modules.rec.entity.view;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: mod_rec_result
 * @Author: jeecg-boot
 * @Date: 2022-02-17
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "mod_rec_result对象", description = "mod_rec_result")
public class ModRecResultView implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 交易流水号
     */
    @Excel(name = "交易流水号", width = 15)
    @ApiModelProperty(value = "交易流水号")
    private java.lang.String tradeNo;
    /**
     * 订单号
     */
    @Excel(name = "订单号", width = 15)
    @ApiModelProperty(value = "订单号")
    private java.lang.String orderNo;
    /**
     * 主系统金额
     */
    @Excel(name = "主系统金额", width = 15)
    @ApiModelProperty(value = "主系统金额")
    private java.math.BigDecimal mainAmount;
    /**
     * 支付系统金额
     */
    @Excel(name = "支付系统金额", width = 15)
    @ApiModelProperty(value = "支付系统金额")
    private java.math.BigDecimal sideAmount;
    /**
     * 对账结果
     */
    @Excel(name = "对账结果", width = 15)
    @ApiModelProperty(value = "对账结果")
    private java.lang.Integer status;
    /**
     * 处理时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "处理时间")
    private java.util.Date createTime;
}
