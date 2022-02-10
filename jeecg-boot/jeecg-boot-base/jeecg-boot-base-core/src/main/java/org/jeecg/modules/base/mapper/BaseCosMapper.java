package org.jeecg.modules.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Author: zhou x
 * @Date: 2022/2/10 15:19
 */
public interface BaseCosMapper<T> extends BaseMapper<T> {
    /**
     * 批量插入 仅适用于mysql
     *
     * @param entityList 实体列表
     * @return 影响行数
     */
    int insertBatchSomeColumn(List<T> entityList);
}
