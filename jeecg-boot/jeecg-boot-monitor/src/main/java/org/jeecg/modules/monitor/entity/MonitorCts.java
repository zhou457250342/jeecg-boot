package org.jeecg.modules.monitor.entity;

import lombok.Data;

/**
 * @Author : nadir
 * create 2023/3/24 11:29
 */
@Data
public class MonitorCts {
    private String id;
    private String type;
    private String macId;
    private String ip;
    private int port;
    private String note;
    private String handler;
    private int status;
}