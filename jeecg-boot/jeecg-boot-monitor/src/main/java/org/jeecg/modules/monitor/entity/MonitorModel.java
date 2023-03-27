package org.jeecg.modules.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author : nadir
 * @create 2023/3/27 15:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mod_monitor")
public class MonitorModel {
    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String macId;
    private String macName;
    private String bussNo;
    private String spo2;
    private String ecg;
    private String bpSys;
    private String bpDia;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
