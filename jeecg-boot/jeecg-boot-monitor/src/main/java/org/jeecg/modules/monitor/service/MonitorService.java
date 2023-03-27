package org.jeecg.modules.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.monitor.entity.MonitorModel;
import org.jeecg.modules.monitor.mapper.MonitorMapper;
import org.springframework.stereotype.Service;

/**
 * @Author : nadir
 * @create 2023/3/27 15:23
 */
@Service
public class MonitorService extends ServiceImpl<MonitorMapper, MonitorModel> implements IService<MonitorModel> {
}
