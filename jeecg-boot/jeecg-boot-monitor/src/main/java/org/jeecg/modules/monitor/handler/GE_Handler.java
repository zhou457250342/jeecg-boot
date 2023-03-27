package org.jeecg.modules.monitor.handler;

import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.monitor.EngineMonitor;
import org.jeecg.modules.monitor.common.RegexUtil;
import org.jeecg.modules.monitor.engine.ISocketHandler;
import org.jeecg.modules.monitor.entity.MonitorMac;
import org.jeecg.modules.monitor.entity.MonitorModel;
import org.jeecg.modules.monitor.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Optional;

/**
 * @Author : nadir
 * @create 2023/3/22 18:32
 */
@ChannelHandler.Sharable
@Slf4j
public class GE_Handler implements ISocketHandler {

    private final MonitorService service;
    private final String _REG_SPO2 = "150456\\^MDC_PULS_OXIM_SAT_O2[^|]*?[|][^|]*?[|](?<ctn>[^|]*)";
    private final String _REG_ECG = "147842\\^MDC_ECG_HEART_RATE[^|]*?[|][^|]*?[|](?<ctn>[^|]*)";
    private final String _REG_BP_S = "150021\\^MDC_PRESS_BLD_NONINV_SYS[^|]*?[|][^|]*?[|](?<ctn>[^|]*)";
    private final String _REG_BP_D = "150022\\^MDC_PRESS_BLD_NONINV_DIA[^|]*?[|][^|]*?[|](?<ctn>[^|]*)";

    public GE_Handler() {
        service = SpringContextUtils.getBean(MonitorService.class);
    }

    @Override
    public void msgRead(InetSocketAddress address, String msg) {
        Optional<MonitorMac> mac = EngineMonitor.monitorCnf.getMac().stream().filter(op -> StringUtils.equals(op.getIp(), address.getAddress().getHostAddress())).findAny();
        if (!mac.isPresent()) return;
        if (EngineMonitor.monitorCnf.isDebug()) log.info(address.getAddress().getHostAddress() + ">" + msg);
        MonitorModel model = new MonitorModel();
        model.setMacId(mac.get().getId());
        model.setMacName(mac.get().getName());
        model.setBussNo(mac.get().getBussNo());
        model.setSpo2(RegexUtil.matchAny(msg, _REG_SPO2, "ctn"));
        model.setEcg(RegexUtil.matchAny(msg, _REG_ECG, "ctn"));
        model.setBpSys(RegexUtil.matchAny(msg, _REG_BP_S, "ctn"));
        model.setBpDia(RegexUtil.matchAny(msg, _REG_BP_D, "ctn"));
        if (StringUtils.isAllBlank(model.getSpo2(), model.getEcg(), model.getBpSys(), model.getBpDia())) return;
        service.save(model);
    }
}
