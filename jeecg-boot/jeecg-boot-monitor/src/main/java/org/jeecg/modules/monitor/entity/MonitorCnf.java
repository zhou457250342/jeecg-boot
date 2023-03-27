package org.jeecg.modules.monitor.entity;

import lombok.Data;

import java.util.List;

/**
 * 监控model
 *
 * @Author : nadir
 * create 2023/3/24 11:29
 */
@Data
public class MonitorCnf {
    private List<MonitorCts> cts;
    private List<MonitorMac> mac;
    private String note;
    private boolean debug;
}



