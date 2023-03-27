package org.jeecg;

import org.jeecg.modules.monitor.EngineMonitor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author : nadir
 * @create 2023/3/22 18:48
 */
@Order(1)
@Component
public class JeecgApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        EngineMonitor.start();
    }
}
