package org.jeecg.modules.rec.engine.job;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.SpringContextHolder;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.rec.engine.model.RecException;
import org.jeecg.modules.rec.engine.model.TradeData;
import org.jeecg.modules.rec.engine.resource.ResourceLoader;
import org.jeecg.modules.rec.entity.ModRec;
import org.jeecg.modules.rec.entity.ModRecModel;
import org.jeecg.modules.rec.service.IModRecModelService;
import org.jeecg.modules.rec.service.IModRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: zhou x
 * @Date: 2022/2/10 15:55
 */
@Component
public class BillingSyncJobWork {

    @Autowired
    private IModRecModelService modRecModelService;
    @Autowired
    private IModRecService modRecService;

    @Transactional(rollbackFor = Exception.class)
    public void syncData(Date syncDate, ModRec modRec, String name) throws RecException {
        String beanName = name + "ResourceLoader";
        ResourceLoader resourceLoader = SpringContextHolder.getBean(beanName);
        if (resourceLoader == null) throw new RecException("ResourceLoader is null");
        List<TradeData> tradeDataList = resourceLoader.getList(syncDate);
        List<ModRecModel> models = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tradeDataList)) {
            tradeDataList.forEach(op -> {
                ModRecModel model = new ModRecModel();
                model.setTradeNo(op.getTradeNo());
                model.setOrderNo(op.getOrderNo());
                model.setBussNo(op.getOperationNo());
                model.setCreateTime(op.getCreateTime());
                model.setUniqueNo(op.getUniqueNo());
                model.setNote(op.getNote());
                model.setAmount(op.getAmount());
                models.add(model);
            });
        }
        modRecModelService.remove(Wrappers.<ModRecModel>lambdaQuery().apply("create_time BETWEEN {0} AND {1}",
                DateUtils.formatDate(syncDate, "yyyy-MM-dd 00:00:00"), DateUtils.formatDate(syncDate, "yyyy-MM-dd 23:59:59")));
        if (CollectionUtils.isNotEmpty(models)) {
            int flag = modRecModelService.insertBatchSomeColumn(models);
            if (flag != models.size()) throw new RecException("数据插入异常");
        }
        String date = DateUtils.formatDate(syncDate, "yyyy-MM-dd");
        modRec.getContent().put("recDate", date);
        modRec.setUptime(new Date());
        modRecService.saveOrUpdate(modRec);
    }
}
