package org.jeecg.modules.rec.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.base.mapper.BaseCosMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zhou x
 * @Date: 2022/2/10 15:35
 */
public class BaseServiceImpl<M extends BaseCosMapper<T>, T> extends ServiceImpl<M, T> {
    public int insertBatchSomeColumn(List<T> entityList) {
        return baseMapper.insertBatchSomeColumn(entityList);
    }
}
