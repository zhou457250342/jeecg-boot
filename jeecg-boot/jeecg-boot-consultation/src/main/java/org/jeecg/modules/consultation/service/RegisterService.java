package org.jeecg.modules.consultation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.consultation.entity.RegisterModel;
import org.jeecg.modules.consultation.mapper.RegisterMapper;
import org.springframework.stereotype.Service;

/**
 * @Author : nadir
 * @create 2023/3/29 12:04
 */
@Service
public class RegisterService extends ServiceImpl<RegisterMapper, RegisterModel> implements IService<RegisterModel> {
}
