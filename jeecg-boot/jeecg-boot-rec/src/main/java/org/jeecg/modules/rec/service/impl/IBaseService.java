package org.jeecg.modules.rec.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author: zhou x
 * @Date: 2022/2/10 15:41
 */
public interface IBaseService<T> extends IService<T> {
    int insertBatchSomeColumn(List<T> entityList);
}
