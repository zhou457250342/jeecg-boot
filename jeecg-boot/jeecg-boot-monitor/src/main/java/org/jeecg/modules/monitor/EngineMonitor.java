package org.jeecg.modules.monitor;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jeecg.modules.monitor.entity.MonitorCnf;
import org.jeecg.modules.monitor.handler.IMonitor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : nadir
 * @create 2023/3/22 15:23
 */
@Slf4j
public class EngineMonitor {

    private static final List<IMonitor> _monitors = new ArrayList<>();
    private static final String _monitorPrefix = "org.jeecg.modules.monitor.handler.";

    public static void start() {
        if (monitorCnf == null || CollectionUtils.isEmpty(monitorCnf.getCts())) return;
        monitorCnf.getCts().forEach(op -> {
            try {
                if (op.getStatus() != 1) return;
                IMonitor monitor = (IMonitor) Class.forName(_monitorPrefix + op.getHandler()).newInstance();
                monitor.listener(op);
                _monitors.add(monitor);
                log.info(op.getId() + "|" + op.getMacId() + " 启动监听");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static MonitorCnf monitorCnf;

    static {
        Path path = Paths.get(System.getProperty("user.dir"), "monitor.json");
        try {
            byte[] data = Files.readAllBytes(path);
            String content = new String(data, "utf-8");
            monitorCnf = JSONObject.parseObject(content, MonitorCnf.class);
        } catch (Exception e) {
            log.debug("监护配置数据读取失败:" + e.getMessage());
        }
    }
}
