package org.jeecg.modules.monitor.handler;

import org.jeecg.modules.monitor.engine.SocketListener;
import org.jeecg.modules.monitor.entity.MonitorCts;

/**
 * GE监护仪
 *
 * @Author : nadir
 * @create 2023/3/24 12:01
 */
public class GE_Monitor implements IMonitor {
    private SocketListener socketListener;

    @Override
    public void listener(MonitorCts monitorCts) {
        if (socketListener == null)
            socketListener = SocketListener.server(new GE_Handler(), monitorCts.getIp(), monitorCts.getPort());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
