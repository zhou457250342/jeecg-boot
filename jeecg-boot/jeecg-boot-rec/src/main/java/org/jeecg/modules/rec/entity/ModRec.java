package org.jeecg.modules.rec.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: mod_rec
 * @Author: jeecg-boot
 * @Date: 2022-02-10
 * @Version: V1.0
 */
@Data
@TableName(value = "mod_rec", autoResultMap = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "mod_rec对象", description = "mod_rec")
public class ModRec implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private java.lang.String id;
    /**
     * name
     */
    @Excel(name = "name", width = 15)
    @ApiModelProperty(value = "name")
    private String name;
    /**
     * content
     */
    @Excel(name = "content", width = 15)
    @ApiModelProperty(value = "content")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private JSONObject content;
    /**
     * uptime
     */
    @Excel(name = "uptime", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "uptime")
    private Date uptime;

    /**
     * type
     */
    @Excel(name = "type", width = 15)
    @ApiModelProperty(value = "type")
    private String type;
}
