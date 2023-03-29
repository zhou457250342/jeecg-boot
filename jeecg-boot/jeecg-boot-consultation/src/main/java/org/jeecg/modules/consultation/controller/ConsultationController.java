package org.jeecg.modules.consultation.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.consultation.entity.RegisterModel;
import org.jeecg.modules.consultation.mapper.RegisterMapper;
import org.jeecg.modules.consultation.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author : nadir
 * @create 2023/3/28 19:26
 */
@Slf4j
@RestController
@RequestMapping("/external/consul")
public class ConsultationController {

    @Autowired
    RegisterService registerService;

    @PostMapping("/register")
    public Object register(String cardId) {
        if (StringUtils.isBlank(cardId)) return 0;
        return registerService.save(new RegisterModel(cardId)) ? 1 : 0;
    }
}
