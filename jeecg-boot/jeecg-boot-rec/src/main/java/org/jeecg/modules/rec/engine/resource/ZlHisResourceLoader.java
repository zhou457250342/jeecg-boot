package org.jeecg.modules.rec.engine.resource;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.rec.engine.model.RecException;
import org.jeecg.modules.rec.engine.model.TradeData;
import org.jeecg.modules.rec.engine.resource.zlhis.common.ZlHisService;
import org.jeecg.modules.rec.engine.resource.zlhis.entity.ZlHisTransData;
import org.jeecg.modules.rec.engine.resource.zlhis.enums.BussServiceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: zhou x
 * @Date: 2022/1/22 15:56
 */
@Service
public class ZlHisResourceLoader implements ResourceLoader {

    @Autowired
    private ZlHisService zlHisService;


    @Override
    public List<TradeData> getList(Date date) {
        if (!DateUtils.isAfterT1Hours(date, 2)) throw new RecException("账单还未出");
        List<ZlHisTransData> result;
        try {
            String param = "<RQ>" + DateUtils.parseDate(date, "yyyy-MM-dd") + "</RQ>";
            result = zlHisService.doService(BussServiceEnum.Business_TransData_Query, param, "JYMXLIST", ZlHisTransData.class);
        } catch (Exception e) {
            throw new RecException("zlHis账单获取失败", e);
        }
        List<TradeData> tradeData = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(result)) {
            result.forEach(op -> {
                TradeData data = new TradeData();
                data.setTradeNo(op.getLSH());
                data.setCreateTime(op.getCZSJ());
                data.setNote(op.getLX());
                data.setAmount("1".equals(op.getZFZT()) ? op.getJE() : op.getJE() * -1);
                tradeData.add(data);
            });
        }
        return tradeData;
    }
}
