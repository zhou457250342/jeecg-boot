package org.jeecg.modules.monitor.entity;

import lombok.Data;

/**
 * @Author : nadir
 * create 2023/3/24 11:29
 */
@Data
public class MonitorMac {
    private String id;
    private String name;
    private String bussNo;
    private String ip;
    private int port;
    private String note;
}