package org.jeecg.modules.app;

import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.service.IAppDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zhou x
 * @Date: 2022/2/9 17:44
 */
@Service
public class AppDictItemService implements IAppDictItemService {
    @Autowired
    private ISysDictItemService sysDictItemService;

    @Override
    public String getDicItemValue(String id) {
        return sysDictItemService.getById(id).getItemValue();
    }
}
