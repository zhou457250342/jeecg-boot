package org.jeecg.modules.rec.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: mod_rec_zlhis
 * @Author: jeecg-boot
 * @Date: 2022-02-10
 * @Version: V1.0
 */
@Data
@TableName("mod_rec_model")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ModRecModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @Excel(name = "唯一标识", width = 15)
    @ApiModelProperty(value = "唯一标识")
    private String uniqueNo;
    /**
     * 交易流水号
     */
    @Excel(name = "交易流水号", width = 15)
    @ApiModelProperty(value = "交易流水号")
    private String tradeNo;
    /**
     * 商户流水号
     */
    @Excel(name = "商户流水号", width = 15)
    @ApiModelProperty(value = "商户流水号")
    private String orderNo;
    /**
     * 业务描述
     */
    @Excel(name = "业务描述", width = 15)
    @ApiModelProperty(value = "业务描述")
    private String note;
    /**
     * 发生时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "发生时间")
    private Date createTime;
    /**
     * 发生金额
     */
    @Excel(name = "发生金额", width = 15)
    @ApiModelProperty(value = "发生金额")
    private float amount;
    /**
     * 业务流水号
     */
    @Excel(name = "业务流水号", width = 15)
    @ApiModelProperty(value = "业务流水号")
    private String bussNo;
}
