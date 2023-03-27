package org.jeecg.modules.monitor.handler;

import org.jeecg.modules.monitor.engine.SocketListener;
import org.jeecg.modules.monitor.entity.MonitorCts;

/**
 * @Author : nadir
 * @create 2023/3/24 15:08
 */
public class MR_IPM7_Monitor implements IMonitor {
    private SocketListener socketListener;

    @Override
    public void listener(MonitorCts monitorCts) {
        if (socketListener == null)
            socketListener = SocketListener.client(new MR_IPM7_Handler(), monitorCts.getIp(), monitorCts.getPort());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
