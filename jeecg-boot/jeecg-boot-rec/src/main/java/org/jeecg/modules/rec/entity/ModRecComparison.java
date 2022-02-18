package org.jeecg.modules.rec.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
import java.util.Date;

/**
 * @Description: mod_rec_comparison
 * @Author: jeecg-boot
 * @Date: 2022-02-14
 * @Version: V1.0
 */
@Data
@TableName("mod_rec_comparison")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "mod_rec_comparison对象", description = "mod_rec_comparison")
public class ModRecComparison implements Serializable {
    private static final long serialVersionUID = 1L;

//	/**id*/
//	@TableId(type = IdType.ASSIGN_ID)
//    @ApiModelProperty(value = "id")
//    private String id;
    /**
     * tradeNo
     */
    @Excel(name = "tradeNo", width = 15)
    @TableId(type = IdType.NONE)
    @ApiModelProperty(value = "tradeNo")
    private String tradeNo;
    /**
     * orderNo
     */
    @Excel(name = "orderNo", width = 15)
    @ApiModelProperty(value = "orderNo")
    private String orderNo;
    /**
     * status
     */
    @Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private Integer status;
    /**
     * createTime
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "createTime")
    private Date createTime;
    /**
     * result
     */
    @Excel(name = "result", width = 15)
    @ApiModelProperty(value = "result")
    private String result;
}
