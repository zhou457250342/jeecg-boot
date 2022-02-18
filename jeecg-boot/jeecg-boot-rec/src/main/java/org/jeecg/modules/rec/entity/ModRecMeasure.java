package org.jeecg.modules.rec.entity;

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
 * @Description: mod_rec_measure
 * @Author: jeecg-boot
 * @Date:   2022-02-18
 * @Version: V1.0
 */
@Data
@TableName("mod_rec_measure")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="mod_rec_measure对象", description="mod_rec_measure")
public class ModRecMeasure implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**通道名*/
	@Excel(name = "通道名", width = 15)
    @ApiModelProperty(value = "通道名")
    private java.lang.String name;
	/**交易流水号*/
	@Excel(name = "交易流水号", width = 15)
    @ApiModelProperty(value = "交易流水号")
    private java.lang.String tradeNo;
	/**补救内容*/
	@Excel(name = "补救内容", width = 15)
    @ApiModelProperty(value = "补救内容")
    private java.lang.String content;
	/**唯一批次号*/
	@Excel(name = "唯一批次号", width = 15)
    @ApiModelProperty(value = "唯一批次号")
    private java.lang.String batchNo;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新时间*/
	@Excel(name = "更新时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date upTime;
	/**处理人*/
	@Excel(name = "处理人", width = 15)
    @ApiModelProperty(value = "处理人")
    private java.lang.String userName;
}
