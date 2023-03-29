package org.jeecg.modules.consultation.entity;

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
 * @create 2023/3/28 19:37
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mod_consul_register")
public class RegisterModel {
    public RegisterModel(String cardId) {
        this.cardId = cardId;
    }

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String cardId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
